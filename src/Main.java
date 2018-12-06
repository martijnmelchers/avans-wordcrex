import controller.App;

import model.WordChecker;
import model.DocumentSession;
import model.database.classes.Clause;
import model.tables.Account;
import model.tables.AccountInfo;
import model.tables.Game;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        /*
        This is a database example :)
         */

        try {

            var _db = DocumentSession.getDatabase(false);
            var clauses = new ArrayList<Clause>();

            System.out.println(new WordChecker().check("seks"));
            /*var accountInfoTest = new AccountInfo();
            accountInfoTest.account = new Account("Mega Neger #" + new Random().nextInt(5000), "Gangnam stijl");
            accountInfoTest.setRoleId("player");

            _db.insert(accountInfoTest);


             //fuck that account lets delete that nibba again
            _db.delete(accountInfoTest);

            for (AccountInfo ac : _db.select(AccountInfo.class, clauses)) {
                System.out.println("Account found! " + ac.doStuff());
            }*/

            /*for(Game game : _db.select(Game.class, clauses)) {
                System.out.println("Game found!");

            }*/

            App application = new App();
            application.load("BoardView.fxml");

            // close the connection nibba
            _db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
