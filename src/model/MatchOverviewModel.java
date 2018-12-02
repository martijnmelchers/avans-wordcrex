package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Connector;
import model.database.services.Database;
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
    private String _username = "Mega Neger #1741";

    private ArrayList<Game> _games;
    public ArrayList<Game> getGames() {return _games;}


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

        _games = FindGames();
    }

    private ArrayList<Game> FindGames()
    {
        var clauses = new ArrayList<Clause>();
        var foundGames = new ArrayList<Game>();

        System.out.println();

        clauses.add(new Clause(new TableAlias("Game", -1), "username_player1", CompareMethod.EQUAL, _username));
        try
        {
            for (Game game : _db.select(Game.class, clauses)) {
                foundGames.add(game);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return foundGames;
    }
}
