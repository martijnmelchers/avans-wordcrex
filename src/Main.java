import controller.database.Connector;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            var conn = new Connector().connect("databases.aii.avans.nl", "fjmelche", "Ab12345", "fjmelche_db2");
            System.out.println("Name: " + conn.getCatalog());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
