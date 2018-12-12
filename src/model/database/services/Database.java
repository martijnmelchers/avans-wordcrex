package model.database.services;

import model.database.annotations.*;
import model.database.classes.*;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.database.enumerators.ResultMethod;
import model.helper.Log;

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
 */
public class Database {
    private Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
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


    public <T> List<T> select(Class<T> output) throws Exception {
        return this.select(output, new ArrayList<>());
    }

    public <T> List<T> select(Class<T> output, List<Clause> clauses) throws Exception {
        Log.info("Building select query for class: " + output + " with " + clauses.size() + " clauses");
        var tableName = this.getTableName(output);
        var result = this.findForeignKeys(output, new TableAlias(tableName, -1));

        var query = QueryBuilder.buildSelect(tableName, result.getSelects(), clauses, result.getJoins());

        Log.info("Successfully built select query for class " + output);
        Log.query(query);

        return this.select(output, result.getJoins(), query);
    }


    public <T> void update(T item) throws Exception {
        this.update(item, this.getTableName(item.getClass()));
    }

    public <T> void update(List<T> items) throws Exception {
        for (T item : items)
            this.update(item);
    }


    public <T> void delete(T item) throws Exception {
        this.delete(item, this.getTableName(item.getClass()));
    }

    public <T> void delete(List<T> items) throws Exception {
        for (T item : items)
            this.delete(item);
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    private <T> ArrayList<InsertedKeys> insert(T item, String table) throws Exception {
        Log.info("Building insert query for class " + item.getClass());
        /* Store the keys to insert and values separately */
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        ArrayList<String> expectedKeys = new ArrayList<>();
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
                expectedKeys.add(this.getColumnName(field));
                continue;
            }


            if (field.isAnnotationPresent(Nullable.class) && field.get(item) == null)
                continue;


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

        Log.info("Successfully built insert query for class " + item.getClass());
        Log.query(sql);

        Log.info("Executing insert query for class " + item.getClass());
        var statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        var rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException(item.getClass() + " could not be created!");
        }

        if (hasGeneratedId) {
            var generatedKeys = statement.getGeneratedKeys();
            int size = 0;
            while (generatedKeys.next()) {
                keys.add(new InsertedKeys(table, expectedKeys.get(size), generatedKeys.getInt(size + 1)));
                size++;
            }

            if (size == 0)
                throw new Exception("Please check your annotations. There should have been IDs generated but none were found.");
        }


        return keys;
    }

    private <T> ArrayList<T> select(Class<T> output, ArrayList<Join> join, String sql) throws Exception {
        Log.info("Executing select query for class " + output);
        var resultSet = this.connection.createStatement().executeQuery(sql);

        ArrayList<T> list = new ArrayList<>();
        Log.info("Select query success! Processing result for class " + output + "...");
        while (resultSet.next()) {
            list.add(this.processResult(output, resultSet, this.getTableName(output), list, join));
        }
        Log.info("Result processing finished. Found " + list.size() + " results");

        return list;
    }

    private <T> void delete(T item, String table) throws Exception {
        Log.info("Building delete query for class: " + item.getClass());
        ArrayList<Clause> clauses = new ArrayList<>();

        for (Field field : item.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) continue;

            field.setAccessible(true);

            String name = this.getColumnName(field);

            if (field.isAnnotationPresent(PrimaryKey.class))
                clauses.add(new Clause(new TableAlias(table, -1), name, CompareMethod.EQUAL, field.get(item)));
        }

        var query = QueryBuilder.buildDelete(table, clauses);

        Log.info("Successfully built delete query for class " + item.getClass());
        Log.query(query);

        Log.info("Executing delete query for class " + item.getClass());
        this.connection.prepareStatement(query).execute();
    }

    private <T> void update(T item, String table) throws Exception {
        Log.info("Building update query for class " + item.getClass());
        ArrayList<Clause> clauses = new ArrayList<>();
        HashMap<String, Object> updated = new HashMap<>();

        for (Field field : item.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) continue;

            field.setAccessible(true);

            String name = this.getColumnName(field);

            if (field.isAnnotationPresent(PrimaryKey.class))
                clauses.add(new Clause(new TableAlias(table, -1), name, CompareMethod.EQUAL, field.get(item)));

            if (field.isAnnotationPresent(PrimaryKey.class) || field.isAnnotationPresent(ForeignKey.class))
                continue;

            var v = field.get(item);

            if (!field.isAnnotationPresent(Nullable.class) && v == null)
                throw new Exception("Field content was null but this is now allowed! Add @nullable if the field is allowed to be null.");
            else if (field.isAnnotationPresent(Nullable.class) && v == null)
                continue;

            updated.put(name, field.get(item));

        }

        if (updated.size() == 0)
            return;

        var query = QueryBuilder.buildUpdate(table, updated, clauses);
        Log.info("Successfully built update query for class " + item.getClass());
        Log.query(query);

        Log.info("Executing update query for class " + item.getClass());
        this.connection.prepareStatement(query).execute();
    }


    private <T> T processResult(Class<T> output, ResultSet data, String table, ArrayList<T> existing, ArrayList<Join> joins) throws Exception {
        T dto = output.getConstructor().newInstance();
        var foreignKeyListFields = new ArrayList<Field>();


        for (Field field : output.getDeclaredFields()) {
            field.setAccessible(true);

            if (!field.isAnnotationPresent(Column.class))
                continue;

            String combinedName = table + "." + this.getColumnName(field);

            try {

                if (field.isAnnotationPresent(ForeignKey.class)) {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var type = annotation.type();


                    var join = this.findJoin(joins, annotation.output());

                    if (join == null)
                        throw new Exception("An error occured while processing the results. A join that should be present was not present.");

                    var foreignKeyField = output.getDeclaredField(annotation.output());
                    if (annotation.result() == ResultMethod.SINGLE) {
                        String value = data.getString(combinedName);

                        if ((value == null || value.equals(""))) {
                            if (field.isAnnotationPresent(Nullable.class))
                                continue;
                            else
                                throw new Exception("ForeignKey is not allowed to be null!");
                        }

                        foreignKeyField.setAccessible(true);
                        foreignKeyField.set(dto, this.processResult(type, data, join.getDestinationTable().build(), new ArrayList<>(), joins));
                    } else {
                        foreignKeyListFields.add(field);
                    }
                }

                String value = data.getString(combinedName);


                try {
                    var customParser = ObjectHelper.SQLToObject(field, data, combinedName);

                    if (!field.isAnnotationPresent(Nullable.class) && value == null)
                        throw new Exception("An error occurred! Field " + combinedName + " was null! (Add @nullable if a field can be null)");
                    else if (field.isAnnotationPresent(Nullable.class) && value == null)
                        continue;

                    if (customParser == null)
                        field.set(dto, field.getType().getConstructor(String.class).newInstance(value));
                    else
                        field.set(dto, customParser);
                } catch (Exception e) {
                    Log.error(e);
                    if (field.isAnnotationPresent(Nullable.class))
                        continue;

                    throw new Exception("Could not parse field " + combinedName + " to type " + field.getType() + ". This could be caused because the type is primitive, has no constructor or has no custom parser.");
                }


            } catch (Exception e) {
                Log.error(e);
            }

        }

        if (!foreignKeyListFields.isEmpty()) {
            T item = this.findExistingItem(dto, existing);

            for (Field field : foreignKeyListFields) {
                if (item == null) {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var type = annotation.type();

                    var join = this.findJoin(joins, annotation.output());

                    if (join == null)
                        throw new Exception("A join was not found in the foreign key list; error!");

                    var newList = new ArrayList<>();
                    var newItem = this.processResult(type, data, join.getDestinationTable().build(), new ArrayList<>(), joins);

                    newList.add(newItem);

                    var foreignKeyField = output.getDeclaredField(annotation.output());
                    foreignKeyField.set(dto, newList);

                } else {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var type = annotation.type();

                    var foreignKeyField = output.getDeclaredField(annotation.output());

                    var join = this.findJoin(joins, annotation.output());

                    if (join == null)
                        throw new Exception("A join was not found in the foreign key list; error!");

                    @SuppressWarnings("unchecked") /* Suppress this because we know it is a list */
                            ArrayList<Object> existingItems = (ArrayList<Object>) foreignKeyField.get(output);

                    var newItem = this.processResult(type, data, join.getDestinationTable().build(), new ArrayList<>(), joins);

                    existingItems.add(newItem);

                    return null;

                }
            }
        }

        return dto;
    }

    private Join findJoin(ArrayList<Join> joins, String outputVariable) {
        for (Join join : joins) {
            if (join.getOutputVariable().equals(outputVariable))
                return join;
        }

        return null;
    }

    private <T> T findExistingItem(T goal, List<T> possibilities) throws IllegalAccessException {
        for (T item : possibilities) {
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

    private <T> JoinResult findForeignKeys(Class<T> input, TableAlias table) throws Exception {
        return this.findForeignKeys(input, table, new JoinResult());
    }

    private <T> JoinResult findForeignKeys(Class<T> input, TableAlias table, JoinResult result) throws Exception {
        for (Field field : input.getDeclaredFields()) {
            field.setAccessible(true);

            if (!field.isAnnotationPresent(Column.class)) continue;

            if (!field.isAnnotationPresent(ForeignKey.class)) {
                result.addSelect(new Select(table, this.getColumnName(field)));
            } else {
                var annotation = field.getAnnotation(ForeignKey.class);

                var type = annotation.type();
                var output = annotation.output();
                var tblName = this.getTableName(type);

                result.addSelect(new Select(table, this.getColumnName(field)));

                if (result.foreignKeyFinished(annotation.output())) {
                    var existingJoin = this.findJoin(result.getJoins(), output);

                    /* If does not exist we need to throw an error */
                    if (existingJoin == null)
                        throw new Exception(
                                "A fatal error occurred could not build query.\n" +
                                        "Could not find a join for output variable " + output + " but it is marked as complete.");

                    /* Add a new field to join on */
                    existingJoin.addJoinColumn(this.getColumnName(field), annotation.field());
                    continue;
                }

                /* Get the table name of the Foreign Key and create an alias for it */
                var tbl = this.buildTableAlias(tblName, result.getAliases());

                /* Add a new alias with the one that was built above */
                result.addAlias(tbl);

                /* Create a new joinresult */
                var recursiveJoinResult = new JoinResult();

                /* Add all existing aliases so unique table aliases can be created */
                recursiveJoinResult.addAliases(result.getAliases());

                /* Recursively find the foreign keys in the foreign key */
                var recursiveResult = this.findForeignKeys(type, tbl, recursiveJoinResult);

                /* Add the new results to the parent result */
                result.addAliases(recursiveResult.getAliases());
                result.addSelects(recursiveResult.getSelects());

                /*  Add a new join for the current foreign key */
                var join = new Join(
                        table.build(),
                        tbl,
                        LinkMethod.AND,
                        annotation.joinMethod(),
                        output
                );

                /* Add the current column to the join */
                join.addJoinColumn(this.getColumnName(field), annotation.field());

                /* Add the join to the parent result */
                result.addJoin(join);

                /* Mark the foreign key as finished so it will not get iterated again */
                result.addFinishedForeignKey(annotation.output());

                /* Add the found joins AFTER all original joins have been added so they are in the right order */
                result.addJoins(recursiveResult.getJoins());
            }

        }


        return result;
    }

    private TableAlias buildTableAlias(String tableName, ArrayList<TableAlias> existing) {
        var currentIdentifier = 1;
        for (TableAlias alias : existing) {
            if (alias.getTable().equals(tableName))
                currentIdentifier = alias.getIdentifier() + 1;
        }

        return new TableAlias(tableName, currentIdentifier);
    }

    private String getColumnName(Field field) {
        String name = field.getAnnotation(Column.class).value();

        if (name.equals(""))
            name = field.getName();

        return name;
    }

    private <T> String getTableName(Class<T> item) throws Exception {
        if (!item.isAnnotationPresent(Table.class))
            throw new Exception("Table annotation is missing! Add it to " + item + " using: @Table(\"tablename\")");

        var tableName = item.getAnnotation(Table.class).value();

        if (tableName.equals(""))
            throw new Exception("Table annotation is missing! Add it to " + item + " using: @Table(\"tablename\")");

        return tableName;
    }


}
