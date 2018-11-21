package model.database;

import model.annotations.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
    }

    public <T> void insert(T item, String table) throws Exception {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        for (Field field : item.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) {
                if (field.isAnnotationPresent(AutoIncrement.class) && field.get(item) == null)
                    continue;

                columns.add(this.getColumnName(field));

                try {
                    var v = field.get(item);

                    /* If the field is not allowed to be null and is null throw an exception */
                    if (!field.isAnnotationPresent(Nullable.class) && v == null)
                        throw new Exception("Field " + field.getName() + " is not allowed to be null!");

                    values.add(field.get(item));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(this.buildInsertQuery(table, columns, values));
    }


    public <T> void insert(T item) throws Exception {
        if (item.getClass().isAnnotationPresent(Table.class)) {
            var table = item.getClass().getAnnotation(Table.class).name();

            if (table.equals(""))
                throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
            else
                this.insert(item, table);
        } else {
            throw new Exception("Table annotation is missing, please add it to the class or use insert(item, table)");
        }
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
        return null;
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

        System.out.println(this.buildUpdateQuery(table, updated, clauses));
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

    private String buildClause(List<Clause> clauses) throws Exception {
        var builder = new StringBuilder();

        for (Clause clause : clauses) {
            if (clauses.indexOf(clause) != clauses.size() - 1) {
                builder.append(clause.toString(true)).append(" ");
            } else {
                builder.append(clause.toString(false));
            }
        }

        return builder.toString();
    }

    private String buildInsertQuery(String table, List<String> columns, List<Object> values) throws Exception {
        var insertString = new StringBuilder();

        for (Object object : values) {
            boolean addComma = true;

            if (values.indexOf(object) == values.size() - 1)
                addComma = false;

            insertString.append(ObjectHelper.objectToSQL(object)).append(addComma ? ", " : "");
        }

        return String.format("INSERT INTO %s (`%s`) VALUES (%s)", table, String.join("`, `", columns), insertString);
    }

    private String buildUpdateQuery(String table, HashMap<String, Object> updatedValues, List<Clause> clauses) throws Exception {
        var updateString = new StringBuilder();
        var iteration = 0;

        for (Map.Entry<String, Object> entry : updatedValues.entrySet()) {
            boolean addComma = true;

            if (iteration == updatedValues.size() - 1)
                addComma = false;

            updateString.append(entry.getKey()).append(" = ").append(ObjectHelper.objectToSQL(entry.getValue())).append(addComma ? ", " : "");
        }

        String clauseString = clauses.size() > 0 ? "WHERE " + this.buildClause(clauses) : "";

        return String.format("UPDATE %s SET %s %s", table, updateString, clauseString);
    }


}
