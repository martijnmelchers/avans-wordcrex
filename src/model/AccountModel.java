package model;

import controller.AccountController;
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

    public String registerAccount(String username, String password) {
        if (username.length() < 5 || username.length() > 25) {
            return "Gebruikersnaam lengte moet tussen (5 - 25)";
        }

        if (password.length() < 5 || password.length() > 25) {
            return "Wachtwoord lengte moet tussen (5 - 25)";
        }

        String lowerUsername = username.toLowerCase();
        String lowerPassword = password.toLowerCase();

        try {
            _db.insert(new Account(lowerUsername, lowerPassword));
            return null;
        } catch (Exception e) {
            Log.error(e, true);
            return e.getMessage();
        }
    }

    public Account getAccount(String username, String password) {
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("account", -1), "username", CompareMethod.EQUAL, username));
        clauses.add(new Clause(new TableAlias("account", -1), "password", CompareMethod.EQUAL, password));

        try {
            return _db.select(Account.class, clauses).get(0);
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }

    public String changePassword(String username, String password)
    {
        if (password.length() < 5 || password.length() > 25)
        {
            return "Wachtwoord lengte moet tussen (5 - 25)";
        }

        String lowerPassword = password.toLowerCase();

        try
        {
            _db.update(new Account(username, lowerPassword));
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String getRole()
    {
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("accountrole", -1), "username", CompareMethod.EQUAL, GameSession.getUsername()));

        try {
            GameSession.setRole(_db.select(AccountInfo.class, clauses).get(0).role);
            return GameSession.getRole().getRole();
        } catch (Exception e) {
            return null;
        }
    }
}
