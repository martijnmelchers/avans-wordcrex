package model;

import model.database.services.Connector;
import model.database.services.Database;
import model.tables.AccountInfo;

import java.sql.Connection;

/**
 * TODO: Add comments
 * TODO: Clean up code
 */

public class MatchOverviewModel
{
    private Database _db;

    // TODO: Fill the username automatically in using the authentication feature.
    private String _username = "";

    public MatchOverviewModel()
    {
        try
        {
            Connection conn = new Connector().connect("databases.aii.avans.nl", "fjmelche", "Ab12345", "smendel_db2");
            this._db = new Database(conn);
        }
        catch(Exception e){
            e.printStackTrace();
        }



        _db.select()
    }
}
