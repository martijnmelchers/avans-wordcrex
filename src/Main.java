import controller.App;

import model.tables.Account;

import model.tables.AccountInfo;
import model.database.classes.Clause;
import model.database.services.Connector;
import model.database.services.Database;

import model.tables.BoardPlayer1;


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


            for (AccountInfo ac : _db.select(AccountInfo.class, clauses)) {
                System.out.println(ac);
                System.out.println(ac.account);
                System.out.println(ac.role);
            }

            clauses.clear();

            for (BoardPlayer1 bp : _db.select(BoardPlayer1.class, clauses)){
                System.out.println(bp);
                System.out.println(bp.tile);
                System.out.println(bp.turn);
                System.out.println(bp.turnPlayer1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        App application = new App();
        application.load("LoginView.fxml");
    }
}

