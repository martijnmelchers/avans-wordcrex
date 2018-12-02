package model;

import model.database.classes.Clause;
import model.database.enumerators.CompareMethod;
import model.database.services.Connector;
import model.database.services.Database;
import model.tables.Account;

import java.sql.Connection;
import java.util.ArrayList;

public class AccountModel
{
    private Database db;

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

    public boolean registerAccount(String username,String password)
    {
        try
        {
            db.insert(new Account(username,password));
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Account getAccount(String username,String password)
    {
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause("account","username" , CompareMethod.EQUAL,username ));
        clauses.add(new Clause("account","password" , CompareMethod.EQUAL,password ));

        try
        {
            return db.select(Account.class,clauses ).get(0);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
