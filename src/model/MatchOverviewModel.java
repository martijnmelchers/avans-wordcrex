package model;

import model.database.classes.Clause;
import model.database.services.Connector;
import model.database.services.Database;
import model.tables.AccountInfo;
import model.tables.Game;

import java.sql.Connection;
import java.util.ArrayList;

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

        var clauses = new ArrayList<Clause>();

        try
        {
            for (Game game : _db.select(Game.class, clauses)) {
                System.out.println(game.account);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
