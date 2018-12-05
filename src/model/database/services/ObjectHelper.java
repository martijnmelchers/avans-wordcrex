package model.database.services;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ObjectHelper {
    public static String objectToSQL(Object o) throws Exception {
        var c = o.getClass();
        if(c == String.class) {
            return "'" + o +"'";
        } else if(c == Integer.class || c == int.class) {
            return o.toString();
        } else if (c == Timestamp.class){
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o);
            System.out.println(formattedDate);
            return "'" + formattedDate + "'";
        } else {
            throw new Exception("No serializer found for class " + o.getClass() + ". Implement it in ObjectHelper.java in function `objectToSQL()`");
        }
    }
}
