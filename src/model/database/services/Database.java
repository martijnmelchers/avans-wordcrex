package model.database.services;

import model.annotations.*;
import model.database.classes.Clause;
import model.database.enumerators.CompareMethod;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

public class Database {
    private Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
    }


    public <T> void insert(T item, String table) throws Exception {
        /* Store the keys to insert and values separately */
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        /* Loop through each field in the class */
        for (Field field : item.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            /* Only continue if the field is marked as a column */
            if (!field.isAnnotationPresent(Column.class)) continue;

            /* If the field is auto increment and not forced continue */
            if (field.isAnnotationPresent(AutoIncrement.class) && field.get(item) == null)
                continue;

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

        /* Build query & execute it */
        System.out.println(QueryBuilder.buildInsert(table, columns, values));
    }


    public <T> void insert(T item) throws Exception {
        if (!item.getClass().isAnnotationPresent(Table.class))
            throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");

        var tableName = item.getClass().getAnnotation(Table.class).name();

        if (tableName.equals(""))
            throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
        else
            this.insert(item, tableName);

    }

    public <T> void insert(List<T> items) throws Exception {
        for (T item : items) {
            this.insert(item);
        }
    }

    public <T> void insert(List<T> items, String table) throws Exception {
        for (T item : items) {
            this.insert(item, table);
        }
    }

    public <T> List<T> select(Class<T> output, List<Clause> clauses) throws Exception {
        System.out.println(QueryBuilder.buildSelect(output.getAnnotation(Table.class).name(), clauses));
        return this.select(output, QueryBuilder.buildSelect(output.getAnnotation(Table.class).name(), clauses));
    }

    public <T> List<T> select(Class<T> output, String sql) throws Exception {
        Field[] fields = output.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }

        var resultSet = this.connection.createStatement().executeQuery(sql);

        List<T> list = new ArrayList<>();
        while (resultSet.next()) {

            T dto = output.getConstructor().newInstance();

            for (Field field : fields) {
                if (!field.isAnnotationPresent(Column.class))
                    continue;

                String name = this.getColumnName(field);

                try {
                    String value = resultSet.getString(name);
                    field.set(dto, field.getType().getConstructor(String.class).newInstance(value));
                } catch (Exception e) {
                    if (!field.isAnnotationPresent(Nullable.class))
                        throw new Exception("An error occurred! Field content was null! (Add @nullable if a field can be null)", e);
                }

            }

            list.add(dto);

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
                clauses.add(new Clause(name, CompareMethod.Equal, field.get(item)));
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


}
