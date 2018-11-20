package model.database;

import model.annotations.Column;
import model.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

public class Database {
    private Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
    }


    public <T> void insert(T item, String table) {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<Object> values = new ArrayList<>();

        for (Field field : item.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) {
                String name = field.getAnnotation(Column.class).name();

                if (name.equals(""))
                    name = field.getName();

                columns.add(name);
                try {
                    values.add(field.get(item));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        StringBuilder valuesInsert = new StringBuilder();

        for(Object object : values) {
            var c = object.getClass();
            boolean addComma = true;


            if(values.indexOf(object) == values.size() - 1)
                addComma = false;

            if(c == String.class) {
                valuesInsert.append("'").append(object).append("'").append(addComma ? ", " : "");
            } else if(c == Integer.class || c == int.class) {
                valuesInsert.append(object).append(addComma ? ", " : "");
            } else {
                System.out.println("Unknown item to serialize. Please implement it in Database.java");
            }
        }


        System.out.println("INSERT INTO " + table + " (" + String.join(", ", columns) + ") VALUES (" + valuesInsert + ")");
        //this.connection.createStatement().executeQuery("")

    }

    public <T> List<T> select(Class<T> output, String sql) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] fields = output.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }

        var resultSet = this.connection.createStatement().executeQuery(sql);

        List<T> list = new ArrayList<>();
        while (resultSet.next()) {

            T dto = output.getConstructor().newInstance();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    String name = field.getAnnotation(Column.class).name();

                    if (name.equals(""))
                        name = field.getName();

                    try {
                        String value = resultSet.getString(name);
                        field.set(dto, field.getType().getConstructor(String.class).newInstance(value));
                    } catch (Exception e) {
                        if(!field.isAnnotationPresent(Nullable.class))
                            System.out.println("An error occurred! Field content was null! (Add @nullable if a field can be null)");
                    }
                }

            }

            list.add(dto);

        }

        return list;
    }
}
