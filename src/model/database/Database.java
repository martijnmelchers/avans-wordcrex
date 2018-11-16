package model.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
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
                System.out.println(field.getType());
                String name = field.getName();

                try {
                    String value = resultSet.getString(name);
                    field.set(dto, field.getType().getConstructor(String.class).newInstance(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            list.add(dto);

        }

        return list;
    }
}
