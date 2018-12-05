import controller.App;
import javafx.scene.control.Alert;
import model.database.classes.Clause;
import model.database.services.Connector;
import model.database.services.Database;
import model.tables.Account;
import model.tables.AccountInfo;
import model.tables.BoardPlayer1;
import model.tables.Game;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        /*
        Thus us a database example :)
         */

        try {
            var conn = new Connector().connect("databases.aii.avans.nl", "fjmelche", "Ab12345", "smendel_db2");
            var _db = new Database(conn, true);

            var clauses = new ArrayList<Clause>();


            var accountInfoTest = new AccountInfo();
            accountInfoTest.account = new Account("Mega Neger #" + new Random().nextInt(5000), "Gangnam stijl");
            accountInfoTest.setRoleId("player");

        controller.setModerator(new Moderator(new Database(new Connector().connect("databases.aii.avans.nl","ddfschol","Ab12345","smendel_db2"))));
        primaryStage.setTitle("controller/moderator");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));


            /* fuck that account lets delete that nibba again */
            _db.delete(accountInfoTest);

            for (AccountInfo ac : _db.select(AccountInfo.class, clauses)) {
                System.out.println("Account found! " + ac.doStuff());
            }

            for(Game game : _db.select(Game.class, clauses)) {
                System.out.println("Game found!");

            }

            App application = new App();
            application.load("MainView.fxml");

            // close the connection nibba
            _db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
