package model.database.services;

import model.database.annotations.*;
import model.database.classes.Clause;
import model.database.classes.InsertedKeys;
import model.database.classes.Join;
import model.database.classes.Select;
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
            if (field.isAnnotationPresent(AutoIncrement.class) && field.get(item) == null)
                continue;
            else
                hasGeneratedId = true;



            /* If the field is a foreign key to another table we have to create that first. */
            if (field.isAnnotationPresent(ForeignKey.class)) {
                var annotation = field.getAnnotation(ForeignKey.class);
                var f = item.getClass().getDeclaredField(annotation.output());
                var oTable = item.getClass().getAnnotation(Table.class).value();
                f.setAccessible(true);
                var v = field.get(item);

                if (v != null) {
                    keys.addAll(this.findPrimaryKeys(v));
                } else {
                    keys.addAll(this.insert(f));

                    for (InsertedKeys key : keys) {
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

            if(field.isAnnotationPresent(PrimaryKey.class))
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

    private <T> List<InsertedKeys> findPrimaryKeys(T item) throws Exception {
        var keys = new ArrayList<InsertedKeys>();
        var table = item.getClass().getAnnotation(Table.class).value();

        for (Field field : item.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (!field.isAnnotationPresent(Column.class) || !field.isAnnotationPresent(PrimaryKey.class))
                continue;

            keys.add(new InsertedKeys(table, this.getColumnName(field), field.get(item)));

        }

        return keys;
    }


    public <T> ArrayList<InsertedKeys> insert(T item) throws Exception {
        if (!item.getClass().isAnnotationPresent(Table.class))
            throw new Exception("Table annotation is missing! Add it to " + item.getClass() + " using: @Table(\"tablename\")");

        var tableName = item.getClass().getAnnotation(Table.class).value();

        if (tableName.equals(""))
            throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
        else
            return this.insert(item, tableName);

    }

    public <T> ArrayList<InsertedKeys> insert(List<T> items) throws Exception {
        var ids = new ArrayList<InsertedKeys>();
        for (T item : items) {
            ids.addAll(this.insert(item));
        }

        return ids;
    }

    public <T> List<T> select(Class<T> output, List<Clause> clauses) throws Exception {

        var query = QueryBuilder.buildSelect(output.getAnnotation(Table.class).value(), this.findNamings(output), clauses, this.findForeignKeys(output));

        System.out.println(query);

        return this.select(output, query);
    }


    private <T> List<T> select(Class<T> output, String sql) throws Exception {
        var resultSet = this.connection.createStatement().executeQuery(sql);

        List<T> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(this.processResult(output, resultSet, list));

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
                clauses.add(new Clause(table, name, CompareMethod.EQUAL, field.get(item)));
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

    private <T> ArrayList<Join> findForeignKeys(Class<T> input) throws Exception {
        return this.findForeignKeys(input, new ArrayList<>());
    }

    private <T> ArrayList<Join> findForeignKeys(Class<T> input, ArrayList<Join> joins) throws Exception {
        for (Field field : input.getDeclaredFields()) {
            field.setAccessible(true);


            if (!field.isAnnotationPresent(Column.class)) continue;

            if (field.isAnnotationPresent(ForeignKey.class)) {
                var annotation = field.getAnnotation(ForeignKey.class);
                var type = annotation.type();

                if (!type.isAnnotationPresent(Table.class) || !input.isAnnotationPresent(Table.class))
                    throw new Exception("Table annotation is missing! We can't automatically generate the select statement.");

                joins.addAll(this.findForeignKeys(type));


                var tbl = type.getAnnotation(Table.class).value();
                var originTbl = input.getAnnotation(Table.class).value();

                var existingJoin = this.findJoin(joins, originTbl, tbl);

                if (existingJoin != null) {
                    existingJoin.addJoinColumn(this.getColumnName(field), annotation.field());
                } else {

                    var join = new Join(
                            originTbl,
                            tbl,
                            LinkMethod.AND,
                            JoinMethod.INNER
                    );

                    join.addJoinColumn(this.getColumnName(field), annotation.field());

                    joins.add(join);
                }


            }


        }

        return joins;
    }

    private <T> ArrayList<Select> findNamings(Class<T> input) throws Exception {
        return this.findNamings(input, new ArrayList<>());
    }

    private <T> ArrayList<Select> findNamings(Class<T> input, ArrayList<Select> namings) throws Exception {
        for (Field field : input.getDeclaredFields()) {
            field.setAccessible(true);


            if (!field.isAnnotationPresent(Column.class)) continue;

            if (field.isAnnotationPresent(ForeignKey.class)) {
                var annotation = field.getAnnotation(ForeignKey.class);
                var type = annotation.type();

                if (!type.isAnnotationPresent(Table.class) || !input.isAnnotationPresent(Table.class))
                    throw new Exception("Table annotation is missing! We can't automatically generate the select statement.");

                var table = input.getAnnotation(Table.class).value();

                namings.add(new Select(table, this.getColumnName(field)));
                namings.addAll(this.findNamings(type));
            } else {

                if (!input.isAnnotationPresent(Table.class))
                    throw new Exception("Table annotation is missing! We can't automatically generate the select statement.");

                var table = input.getAnnotation(Table.class).value();

                namings.add(new Select(table, this.getColumnName(field)));
            }


        }

        return namings;
    }

    private <T> T processResult(Class<T> output, ResultSet data, List<T> existing) throws Exception {
        T dto = output.getConstructor().newInstance();
        var foreignKeyListFields = new ArrayList<Field>();


        for (Field field : output.getDeclaredFields()) {
            field.setAccessible(true);

            if (!field.isAnnotationPresent(Column.class))
                continue;

            if (!output.isAnnotationPresent(Table.class))
                throw new Exception("");

            String tableName = output.getAnnotation(Table.class).value();
            String columnName = this.getColumnName(field);
            String combinedName = tableName + "." + columnName;

            try {

                if (field.isAnnotationPresent(ForeignKey.class)) {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var type = annotation.type();


                    var foreignKeyField = output.getDeclaredField(annotation.output());
                    if (annotation.result() == ResultMethod.SINGLE) {
                        foreignKeyField.setAccessible(true);
                        foreignKeyField.set(dto, this.processResult(type, data, new ArrayList<>()));
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
                    var newItem = this.processResult(type, data, new ArrayList<>());
                    newList.add(newItem);

                    var foreignKeyField = output.getDeclaredField(annotation.output());
                    foreignKeyField.set(dto, newList);

                } else {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var type = annotation.type();

                    var foreignKeyField = output.getDeclaredField(annotation.output());

                    @SuppressWarnings("unchecked") /* Suppress this because we know it is a list */
                            ArrayList<Object> existingItems = (ArrayList<Object>) foreignKeyField.get(output);

                    var newItem = this.processResult(type, data, new ArrayList<>());

                    existingItems.add(newItem);

                    return null;

                }
            }
        }

        return dto;
    }


    private Join findJoin(ArrayList<Join> joins, String originTable, String destinationTable) {
        for (Join join : joins) {
            if (join.getDestinationTable().equals(destinationTable) && join.getOriginTable().equals(originTable))
                return join;
        }

        return null;
    }

    private static <T> ArrayList<T> createListOfType(Class<T> type) {
        return new ArrayList<T>();
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
