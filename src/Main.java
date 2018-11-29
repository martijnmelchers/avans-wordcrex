import model.tables.Account;
import model.tables.AccountInfo;
import model.database.classes.Clause;
import model.database.services.Connector;
import model.database.services.Database;
import controller.App;

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

            _db.insert(accountInfoTest);









            for(AccountInfo ac : _db.select(AccountInfo.class, clauses)) {
                System.out.println(ac);
                System.out.println(ac.account);
                System.out.println(ac.role);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
      
        App application = new App();
        application.load("ChatView.fxml");
    }
}

