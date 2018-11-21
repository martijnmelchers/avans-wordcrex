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
                if (field.isAnnotationPresent(AutoIncrement.class))
                    continue;

                String name = this.getColumnName(field);

                columns.add(name);
                try {
                    var v = field.get(item);
                    if (!field.isAnnotationPresent(Nullable.class) && v == null)
                        throw new Exception("Field " + field.getName() + " is not allowed to be null!");

                    values.add(field.get(item));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        StringBuilder valuesInsert = new StringBuilder();

        for (Object object : values) {
            var c = object.getClass();
            boolean addComma = true;


            if (values.indexOf(object) == values.size() - 1)
                addComma = false;

            if (c == String.class) {
                valuesInsert.append("'").append(object).append("'").append(addComma ? ", " : "");
            } else if (c == Integer.class || c == int.class) {
                valuesInsert.append(object).append(addComma ? ", " : "");
            } else {
                throw new Exception("No serializer found for class " + c + " add it to database.java!");
            }
        }


        System.out.println("INSERT INTO " + table + " (`" + String.join("`, `", columns) + "`) VALUES (" + valuesInsert + ")");
    }


    public <T> void insert(T item) throws Exception {
        if (item.getClass().isAnnotationPresent(Table.class)) {
            var table = item.getClass().getAnnotation(Table.class).name();

            if (table.equals(""))
                throw new Exception("Table annotation is missing, please add it to the class or add it manually");
            else
                this.insert(item, table);
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


    public <T> void update(T item) throws Exception {
        HashMap<String, Object> clause = new HashMap<>();

        for (Field field : item.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) continue;
            field.setAccessible(true);

            String name = this.getColumnName(field);

            if (field.isAnnotationPresent(PrimaryKey.class))
                clause.put(name, field.get(item));
        }

        System.out.println(this.buildClause(clause));
    }

    private String getColumnName(Field field) {
        String name = field.getAnnotation(Column.class).name();

        if (name.equals(""))
            name = field.getName();

        return name;
    }

    private String buildClause(HashMap<String, Object> clause) throws Exception {
        var builder = new StringBuilder();
        var iteration = 0;

        for (Map.Entry<String, Object> entry : clause.entrySet()) {
            builder.append(entry.getKey()).append(" = ").append(this.objectToSQL(entry.getValue()));

            if(iteration !=  clause.size() - 1) {
                iteration++;
                builder.append(" AND ");
            }
        }

        return builder.toString();
    }

    private String objectToSQL(Object o) throws Exception {
        var c = o.getClass();
        if(c == String.class) {
            return "'" + o +"'";
        } else if(c == Integer.class || c == int.class) {
            return o.toString();
        } else {
            throw new Exception("No serializer found for class " + o.getClass() + ". Implement it in Database.java in function `objectToSQL()`");
        }
    }
}
