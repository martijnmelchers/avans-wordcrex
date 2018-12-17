package model;

import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.Account;
import model.tables.AccountInfo;

import java.sql.SQLException;
import java.util.ArrayList;

public class AccountModel {
    private Database _db;

    public AccountModel() {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (SQLException e) {
            Log.error(e, true);
        }
    }

    public void register(String username, String password) throws Exception {
        if (username.length() < 5 || username.length() > 25)
            throw new Exception("Gebruikersnaam lengte moet tussen (5 - 25)");


        if (password.length() < 5 || password.length() > 25)
            throw new Exception("Wachtwoord lengte moet tussen (5 - 25)");

        String lowerUsername = username.toLowerCase();

        this._db.insert(new Account(lowerUsername, password));
        this._db.insert(new AccountInfo("player", lowerUsername));
    }

    public AccountInfo login(String username, String password) throws Exception {
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("account", 1), "username", CompareMethod.EQUAL, username));
        clauses.add(new Clause(new TableAlias("account", 1), "password", CompareMethod.EQUAL, password));

        return this._db.select(AccountInfo.class, clauses).get(0);
    }

    public void changePassword(String username, String password) throws Exception {
        if (password.length() < 5 || password.length() > 25)
            throw new Exception("Wachtwoord lengte moet tussen (5 - 25)");

        this._db.update(new Account(username, password));
    }

}
