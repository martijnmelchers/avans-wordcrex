package model.database;

public class ObjectHelper {
    public static String objectToSQL(Object o) throws Exception {
        var c = o.getClass();
        if(c == String.class) {
            return "'" + o +"'";
        } else if(c == Integer.class || c == int.class) {
            return o.toString();
        } else {
            throw new Exception("No serializer found for class " + o.getClass() + ". Implement it in ObjectHelper.java in function `objectToSQL()`");
        }
    }
}
