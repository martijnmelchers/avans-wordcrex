import model.User;
import model.database.Connector;
import model.database.Database;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            var conn = new Connector().connect("databases.aii.avans.nl", "fjmelche", "Ab12345", "fjmelche_db2");
            System.out.println("Connected to database and active scheme is: " + conn.getCatalog());
            var db = new Database(conn);



            for (User user : db.select(User.class, "select * from fjmelche_db2.user")) {
                System.out.println(user.getUserId() + " " + user.getUsername());
            }

        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
