package model;

import model.GameSession;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.JoinMethod;
import model.database.enumerators.LinkMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.Game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchFinderModel {

    Database dB;

    private List<Clause> gameSearch;


    public MatchFinderModel() {
        try {
            dB = DocumentSession.getDatabase();
        } catch (SQLException e) {
            Log.error(e);
        }
        gameSearch = new ArrayList<Clause>();
        gameSearch.add(null);
        gameSearch.add(null);
    }

    public List<Game> searchPlayers(String name) {
        gameSearch.set(1, new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.LIKE, "%" + name + "%", LinkMethod.OR));
        gameSearch.set(0, new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.LIKE, "%" + name + "%", LinkMethod.OR));

        try {
            return dB.select(Game.class, gameSearch);
        } catch (Exception e) {
            Log.error(e);
        }
        return null;
    }



}
