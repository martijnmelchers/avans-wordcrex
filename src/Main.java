import controller.App;
import model.WordChecker;
import model.database.classes.Clause;
import model.database.services.Connector;
import model.database.services.Database;
import model.tables.BoardPlayer1;
import model.tables.Game;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /*
        Thus us a database example :)
         */

        try {
            var conn = new Connector().connect("databases.aii.avans.nl", "fjmelche", "Ab12345", "smendel_db2");
            var _db = new Database(conn, true);

            var clauses = new ArrayList<Clause>();


            System.out.println(new WordChecker(_db).check("man"));

//            var accountInfoTest = new AccountInfo();
//            accountInfoTest.account = new Account("Mega Neger #" + new Random().nextInt(5000), "Gangnam stijl");
//            accountInfoTest.setRoleId("player");
//
//            _db.insert(accountInfoTest);


            /*for (AccountInfo ac : _db.select(AccountInfo.class, clauses)) {
                System.out.println(ac);
                System.out.println(ac.account);
                System.out.println(ac.role);
            }*/

            /*for(Game game : _db.select(Game.class, clauses)) {
                System.out.println("Game found!");

            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        App application = new App();
        application.load("BoardView.fxml");
    }
}
