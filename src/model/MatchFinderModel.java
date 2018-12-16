package model;

import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.Game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatchFinderModel {

    private Database _db;


    public MatchFinderModel() {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    public List<Game> searchPlayers(String name) {
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.LIKE, "%" + name + "%", LinkMethod.OR));
        clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.LIKE, "%" + name + "%", LinkMethod.OR));

        try {
            return this._db.select(Game.class, clauses);
        } catch (Exception e) {
            Log.error(e);
        }

        return null;
    }


}
