package model.database.services;

import model.database.annotations.*;
import model.database.classes.Clause;
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

    public Database(Connection connection) {
        this.connection = connection;
    }


    private <T> int insert(T item, String table) throws Exception {
        /* Store the keys to insert and values separately */
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();
        var hasGeneratedId = false;

        /* Loop through each field in the class */
        for (Field field : item.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            /* Only continue if the field is marked as a column */
            if (!field.isAnnotationPresent(Column.class)) continue;

            /* If the field is auto increment and not forced continue */
            if (field.isAnnotationPresent(AutoIncrement.class) && field.get(item) == null)
                continue;
            else
                hasGeneratedId = true;


            if (field.isAnnotationPresent(ForeignKey.class)) {
                var annotation = field.getAnnotation(ForeignKey.class);
                var f = item.getClass().getDeclaredField(annotation.output());
                f.setAccessible(true);
                var id = this.insert(f.get(item));
                field.set(item, id);
            }

            /* Add the column name to the list */
            columns.add(this.getColumnName(field));

            /* Get the value of the field */
            var v = field.get(item);

            /* If the field is not allowed to be null and is null throw an exception */
            if (!field.isAnnotationPresent(Nullable.class) && v == null)
                throw new Exception("Field " + field.getName() + " is not allowed to be null!");

            /* If not, add the value to the list */
            values.add(v);

        }

        var sql = QueryBuilder.buildInsert(table, columns, values);
        System.out.println(sql);
        var statement = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        var rowsAffected = statement.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException(item.getClass() + " could not be created!");
        }

        if (hasGeneratedId) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new Exception("No ID has been generated! Insertion has failed!");
            }
        }

        return -1;
    }


    public <T> int insert(T item) throws Exception {
        if (!item.getClass().isAnnotationPresent(Table.class))
            throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");

        var tableName = item.getClass().getAnnotation(Table.class).name();

        if (tableName.equals(""))
            throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
        else
            return this.insert(item, tableName);

    }

    public <T> ArrayList<Integer> insert(List<T> items) throws Exception {
        var ids = new ArrayList<Integer>();
        for (T item : items) {
            ids.add(this.insert(item));
        }

        return ids;
    }

    public <T> List<T> select(Class<T> output, List<Clause> clauses) throws Exception {

        var query = QueryBuilder.buildSelect(output.getAnnotation(Table.class).name(), this.findNamings(output), clauses, this.findForeignKeys(output));

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
                clauses.add(new Clause(name, CompareMethod.EQUAL, field.get(item)));
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
            var table = item.getClass().getAnnotation(Table.class).name();

            if (table.equals(""))
                throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
            else
                this.update(item, table);
        } else {
            throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
        }
    }

    private String getColumnName(Field field) {
        String name = field.getAnnotation(Column.class).name();

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


                var tbl = type.getAnnotation(Table.class).name();
                var originTbl = input.getAnnotation(Table.class).name();

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

                var table = input.getAnnotation(Table.class).name();

                namings.add(new Select(table, this.getColumnName(field)));
                namings.addAll(this.findNamings(type));
            } else {

                if (!input.isAnnotationPresent(Table.class))
                    throw new Exception("Table annotation is missing! We can't automatically generate the select statement.");

                var table = input.getAnnotation(Table.class).name();

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

            String tableName = output.getAnnotation(Table.class).name();
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
