package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Connector;
import model.database.services.Database;
import model.tables.Account;

import java.sql.Connection;
import java.util.ArrayList;

public class AccountModel
{
    private Database db;
    private String username;

    public AccountModel()
    {
        //TODO: Export database init to more suitable place
        try
        {
            Connection conn = new Connector().connect("databases.aii.avans.nl", "fjmelche", "Ab12345", "smendel_db2");
            db = new Database(conn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String registerAccount(String username,String password)
    {
        if (username.length() < 5 || username.length() > 25)
        {
            return "Gebruikersnaam lengte moet tussen (5 - 25)";
        }

        if (password.length() < 5 || password.length() > 25)
        {
            return "Wachtwoord lengte moet tussen (5 - 25)";
        }

        String lowerUsername = username.toLowerCase();
        String lowerPassword = password.toLowerCase();

        try
        {
            db.insert(new Account(lowerUsername, lowerPassword));
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public Account getAccount(String username,String password)
    {
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("account",-1 ),"username" , CompareMethod.EQUAL,username ));
        clauses.add(new Clause(new TableAlias("account",-1 ),"password" , CompareMethod.EQUAL,password ));

        try
        {
            this.username = username;
            return db.select(Account.class,clauses ).get(0);
        }
        catch (Exception e)
        {
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
            db.update(new Account(username, lowerPassword));
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String getUsername()
    {
        return this.username;
    }
}
