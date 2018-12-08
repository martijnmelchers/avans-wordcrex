package model.database.services;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ObjectHelper {
    public static String objectToSQL(Object o) throws Exception {
        var c = o.getClass();
        if (c == String.class) {
            return "'" + o + "'";
        } else if (c == Integer.class || c == int.class) {
            return o.toString();
        } else if (c == Timestamp.class) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o) + "'";
        } else {
            throw new Exception("No serializer found for class " + c + ". Implement it in ObjectHelper.java in function `objectToSQL()`");
        }
    }

    public static Object SQLToObject(Field field, ResultSet set, String columnName) throws Exception {
        var c = field.getType();
        if (c == String.class) {
            return set.getString(columnName);
        } else if (c == Integer.class) {
            return set.getInt(columnName);
        } else if (c == Timestamp.class) {
            return set.getTimestamp(columnName);
        } else {
            return null;
        }
    }
}
