package model.database.services;

import model.database.annotations.*;
import model.database.classes.*;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.JoinMethod;
import model.database.enumerators.LinkMethod;
import model.database.enumerators.ResultMethod;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * TODO: Add comments
 * TODO: Clean up code
 * TODO: Add delete and fix update recusrive call
 */
public class Database {
    private Connection connection;
    private boolean debug;

    public Database(Connection connection) {
        this.connection = connection;
    }

    public Database(Connection connection, boolean debug) {
        this.connection = connection;
        this.debug = debug;
    }


    private <T> ArrayList<InsertedKeys> insert(T item, String table) throws Exception {
        /* Store the keys to insert and values separately */
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        ArrayList<InsertedKeys> keys = new ArrayList<>();
        boolean hasGeneratedId = false;

        /* Loop through each field in the class */
        for (Field field : item.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            /* Only continue if the field is marked as a column */
            if (!field.isAnnotationPresent(Column.class)) continue;

            /* If the field is auto increment, we don't have to insert a value */
            if (field.isAnnotationPresent(AutoIncrement.class) && field.get(item) == null) {
                hasGeneratedId = true;
                continue;
            }



            /* If the field is a foreign key to another table we have to create that first. */
            if (field.isAnnotationPresent(ForeignKey.class)) {
                var annotation = field.getAnnotation(ForeignKey.class);
                var f = item.getClass().getDeclaredField(annotation.output());
                f.setAccessible(true);

                var v = field.get(item);


                if (v != null) {
                    keys.add(this.findPrimaryKey(annotation.type(), v));
                } else {
                    keys.addAll(this.insert(f.get(item)));

                    for (InsertedKeys key : keys) {
                        var c = f.get(item).getClass();
                        var oTable = c.getAnnotation(Table.class).value();

                        if (key.getTable().equals(oTable) && key.getColumn().equals(annotation.field()))
                            field.set(item, key.getValue());

                    }

                }


            }

            /* Get the column name */
            var columnName = this.getColumnName(field);

            /* Get the value of the field */
            var v = field.get(item);

            /* If the field is not allowed to be null and is null throw an exception */
            if (!field.isAnnotationPresent(Nullable.class) && v == null)
                throw new Exception("Field " + field.getName() + " is not allowed to be null!");

            if (field.isAnnotationPresent(PrimaryKey.class))
                keys.add(new InsertedKeys(table, columnName, v));

            /* Add the column name to the list */
            columns.add(columnName);

            /* If not, add the value to the list */
            values.add(v);

        }

        var sql = QueryBuilder.buildInsert(table, columns, values);

        if (this.debug)
            LogWriter.PrintLog(sql);

        var statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        var rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException(item.getClass() + " could not be created!");
        }

        if (hasGeneratedId) {
            var generatedKeys = statement.getGeneratedKeys();
            var metadata = statement.getMetaData();
            int size = 0;
            while (generatedKeys.next()) {
                size++;
                keys.add(new InsertedKeys(metadata.getTableName(size + 1), metadata.getColumnName(size + 1), generatedKeys.getInt(size + 1)));
            }

            if (size == 0)
                throw new Exception("No IDs were generated! Items were still inserted");
        }


        return keys;
    }

    private <T> InsertedKeys findPrimaryKey(Class<T> c, Object value) {
        var table = c.getAnnotation(Table.class).value();

        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);

            if (!field.isAnnotationPresent(Column.class) || !field.isAnnotationPresent(PrimaryKey.class))
                continue;

            return new InsertedKeys(table, this.getColumnName(field), value);

        }

        return null;
    }


    public <T> ArrayList<InsertedKeys> insert(T item) throws Exception {
        var name = this.getTableName(item.getClass());
        return this.insert(item, name);

    }

    public <T> ArrayList<InsertedKeys> insert(List<T> items) throws Exception {
        var ids = new ArrayList<InsertedKeys>();
        for (T item : items) {
            ids.addAll(this.insert(item));
        }

        return ids;
    }

    public <T> List<T> select(Class<T> output, List<Clause> clauses) throws Exception {

        var tableName = this.getTableName(output);
        var result = this.findForeignKeys(output, tableName);
        var aliases = new ArrayList<>(result.getAliases());

        var query = QueryBuilder.buildSelect(tableName, this.findNamings(output, tableName, aliases), clauses, result.getJoins());

        if (this.debug)
            LogWriter.PrintLog(query);

        return this.select(output, aliases, query);
    }

    private TableAlias buildTableAlias(String tableName, ArrayList<TableAlias> existing) {
        var currentIdentifier = 1;
        for(TableAlias alias : existing) {
            if(alias.getTable().equals(tableName))
                currentIdentifier = alias.getIdentifier() + 1;
        }

        return new TableAlias(tableName, currentIdentifier);
    }

    private <T> String getTableName(Class<T> item) throws Exception {
        if (!item.isAnnotationPresent(Table.class))
            throw new Exception("Table annotation is missing! Add it to " + item + " using: @Table(\"tablename\")");

        var tableName = item.getAnnotation(Table.class).value();

        if (tableName.equals(""))
            throw new Exception("Table annotation is missing! Add it to " + item + " using: @Table(\"tablename\")");

        return tableName;
    }


    private <T> ArrayList<T> select(Class<T> output, ArrayList<TableAlias> aliases, String sql) throws Exception {
        var resultSet = this.connection.createStatement().executeQuery(sql);

        ArrayList<T> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(this.processResult(output, resultSet, list, aliases));
        }

        return list;
    }


    public <T> void update(T item, String table) throws Exception {
        ArrayList<Clause> clauses = new ArrayList<>();
        HashMap<String, Object> updated = new HashMap<>();

        for (Field field : item.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) continue;

            field.setAccessible(true);

            String name = this.getColumnName(field);

            if (field.isAnnotationPresent(PrimaryKey.class)) {
                clauses.add(new Clause(new TableAlias(table, -1), name, CompareMethod.EQUAL, field.get(item)));
                continue;
            }

            var v = field.get(item);

            if (!field.isAnnotationPresent(Nullable.class) && v == null)
                throw new Exception("Field content was null but this is now allowed! Add @nullable if the field is allowed to be null.");
            else if (field.isAnnotationPresent(Nullable.class) && v == null)
                continue;

            updated.put(name, field.get(item));

        }

        System.out.println(QueryBuilder.buildUpdate(table, updated, clauses));
    }

    public <T> void update(T item) throws Exception {
        if (item.getClass().isAnnotationPresent(Table.class)) {
            var table = item.getClass().getAnnotation(Table.class).value();

            if (table.equals(""))
                throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
            else
                this.update(item, table);
        } else {
            throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
        }
    }

    private String getColumnName(Field field) {
        String name = field.getAnnotation(Column.class).value();

        if (name.equals(""))
            name = field.getName();

        return name;
    }

    private <T> JoinResult findForeignKeys(Class<T> input, String table) throws Exception {
        return this.findForeignKeys(input, table, new JoinResult());
    }

    private <T> JoinResult findForeignKeys(Class<T> input, String table, JoinResult result) throws Exception {
        for (Field field : input.getDeclaredFields()) {
            field.setAccessible(true);


            if (!field.isAnnotationPresent(Column.class)) continue;

            if (!field.isAnnotationPresent(ForeignKey.class))
                continue;
            var annotation = field.getAnnotation(ForeignKey.class);
            var type = annotation.type();

            if (!type.isAnnotationPresent(Table.class) || !input.isAnnotationPresent(Table.class))
                throw new Exception("Table annotation is missing! We can't automatically generate the select statement.");

            if (result.foreignKeyFinished(annotation.output()))
                continue;

            /* Get the table name of the Foreign Key and create an alias for it */
            var tblName = this.getTableName(type);
            var tbl = this.buildTableAlias(tblName, result.getAliases());

            result.addAlias(tbl);

            var recursiveJoinResult = new JoinResult();

            for (String key : result.getFinishedForeignKeys())
                recursiveJoinResult.addFinishedForeignKey(key);

            var recursiveResult = this.findForeignKeys(type, tbl.build(), recursiveJoinResult);

            result.addAliases(recursiveResult.getAliases());

            var existingJoin = this.findJoin(result.getJoins(), table, tbl);

            if (existingJoin != null) {
                existingJoin.addJoinColumn(this.getColumnName(field), annotation.field());
            } else {

                var join = new Join(
                        table,
                        tbl,
                        LinkMethod.AND,
                        JoinMethod.INNER
                );

                join.addJoinColumn(this.getColumnName(field), annotation.field());

                result.addJoin(join);
            }

            result.addFinishedForeignKey(annotation.output());

            result.addJoins(recursiveResult.getJoins());

        }


        return result;
    }

    private <T> ArrayList<Select> findNamings(Class<T> input, String table, ArrayList<TableAlias> aliases) throws Exception {
        return this.findNamings(input, new ArrayList<>(), table, aliases);
    }

    private <T> ArrayList<Select> findNamings(Class<T> input, ArrayList<Select> namings, String table, ArrayList<TableAlias> tableAliases) throws Exception {
        for (Field field : input.getDeclaredFields()) {
            field.setAccessible(true);


            if (!field.isAnnotationPresent(Column.class)) continue;

            if (field.isAnnotationPresent(ForeignKey.class)) {
                var annotation = field.getAnnotation(ForeignKey.class);
                var type = annotation.type();
                var tableName = this.getTableName(type);
                var alias = this.findAlias(tableAliases, tableName);

                if(alias == null)
                    throw new Exception("A fatal error occured!");

                namings.add(new Select(alias, annotation.field()));
                namings.add(new Select(new TableAlias(table, -1), this.getColumnName(field)));
                namings.addAll(this.findNamings(type, alias.build(), tableAliases));
            } else {
                namings.add(new Select(new TableAlias(table, -1), this.getColumnName(field)));
            }


        }

        return this.filterOutDuplicateNamings(namings);
    }

    private ArrayList<Select> filterOutDuplicateNamings(ArrayList<Select> namings) {
        var distinctNamings = new ArrayList<Select>();
        var namingStrings = new ArrayList<String>();

        for (Select naming : namings) {
            var sql = naming.build();
            if (!namingStrings.contains(sql)) {
                namingStrings.add(sql);
                distinctNamings.add(naming);
            }
        }


        return distinctNamings;
    }

    private <T> T processResult(Class<T> output, ResultSet data, ArrayList<T> existing, ArrayList<TableAlias> aliases) throws Exception {
        T dto = output.getConstructor().newInstance();
        var foreignKeyListFields = new ArrayList<Field>();


        for (Field field : output.getDeclaredFields()) {
            field.setAccessible(true);

            if (!field.isAnnotationPresent(Column.class))
                continue;

            if (!output.isAnnotationPresent(Table.class))
                throw new Exception("");

            var alias = this.findAlias(aliases, this.getTableName(output));
            var name = alias == null ? this.getTableName(output) : alias.build();

            String combinedName = name + "." + this.getColumnName(field);

            try {

                if (field.isAnnotationPresent(ForeignKey.class)) {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var type = annotation.type();


                    var foreignKeyField = output.getDeclaredField(annotation.output());
                    if (annotation.result() == ResultMethod.SINGLE) {
                        foreignKeyField.setAccessible(true);
                        foreignKeyField.set(dto, this.processResult(type, data, new ArrayList<>(), aliases));
                    } else {
                        foreignKeyListFields.add(field);
                    }
                }

                String value = data.getString(combinedName);
                field.set(dto, field.getType().getConstructor(String.class).newInstance(value));


            } catch (Exception e) {
                if (!field.isAnnotationPresent(Nullable.class))
                    throw new Exception("An error occurred! Field " + combinedName + " was null! (Add @nullable if a field can be null)", e);
            }

        }

        if (!foreignKeyListFields.isEmpty()) {
            T item = this.findExistingItem(dto, existing);

            for (Field field : foreignKeyListFields) {
                if (item == null) {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var type = annotation.type();

                    var newList = new ArrayList<>();
                    var newItem = this.processResult(type, data, new ArrayList<>(), aliases);
                    newList.add(newItem);

                    var foreignKeyField = output.getDeclaredField(annotation.output());
                    foreignKeyField.set(dto, newList);

                } else {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var type = annotation.type();

                    var foreignKeyField = output.getDeclaredField(annotation.output());

                    @SuppressWarnings("unchecked") /* Suppress this because we know it is a list */
                            ArrayList<Object> existingItems = (ArrayList<Object>) foreignKeyField.get(output);

                    var newItem = this.processResult(type, data, new ArrayList<>(), aliases);

                    existingItems.add(newItem);

                    return null;

                }
            }
        }

        return dto;
    }

    private TableAlias findAlias(ArrayList<TableAlias> aliases, String tableName) {
        var candidates = new ArrayList<TableAlias>();
        var bestCandidate = new TableAlias(null, -1);

        for (TableAlias alias : aliases) {
            if (alias.getTable().equals(tableName))
                candidates.add(alias);
        }

        for (TableAlias candidate : candidates) {
            if (bestCandidate.getUsages() > candidate.getUsages() || bestCandidate.getUsages() == 0)
                bestCandidate = candidate;
        }

        return bestCandidate.build() == null ? null : bestCandidate;
    }


    private Join findJoin(ArrayList<Join> joins, String originTable, TableAlias destinationTable) {
        for (Join join : joins) {
            if (join.getDestinationTable().build().equals(destinationTable.build()) && join.getOriginTable().equals(originTable))
                return join;
        }

        return null;
    }

    private <T> T findExistingItem(T goal, List<T> possiblities) throws IllegalAccessException {
        for (T item : possiblities) {
            var isMatch = false;
            for (Field field : item.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(Column.class) || !field.isAnnotationPresent(PrimaryKey.class))
                    continue;

                isMatch = field.get(item) == field.get(goal);
            }

            if (isMatch)
                return item;
        }

        return null;
    }

}
