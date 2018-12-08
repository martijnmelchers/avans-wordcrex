package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.database.services.Database;
import model.tables.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Add comments
 * TODO: Clean up code
 */

public class MatchOverviewModel {
    private Database _db;

    // TODO: Fill the username automatically in using the authentication feature.
    private String _username = "Huseyin-Testing";

    private ArrayList<Game> _games;

    // TODO make the controller use this function
    public ArrayList<Game> getGames() {
        return _games;
    }


    public MatchOverviewModel() {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        _games = FindGames();
    }

    private ArrayList<Game> FindGames() {
        var clauses = new ArrayList<Clause>();
        var foundGames = new ArrayList<Game>();

        System.out.println();

        clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.EQUAL, _username));
        try {
            for (Game game : _db.select(Game.class, clauses)) {
                foundGames.add(game);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundGames;
    }

    public boolean currentTurnHasAction(Game game) {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("turnplayer1", -1), "username_player1", CompareMethod.EQUAL, game.player1.getUsername()));

        try {
            var turnList = _db.select(TurnPlayer1.class, clauses);
            for (TurnPlayer1 playerTurn : turnList) {
                if (playerTurn.turn.game.getGameID().equals(game.getGameID())) {
                    return playerTurn.getTurnActionType() != null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean currentTurnPlayer2HasAction(Game game) {
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("turnplayer2", -1), "username_player2", CompareMethod.EQUAL, game.player2.getUsername()));

        try {
            var turnList = _db.select(TurnPlayer2.class, clauses);
            for (TurnPlayer2 playerTurn : turnList) {
                if (playerTurn.turn.game.getGameID().equals(game.getGameID())) {
                    return playerTurn.getTurnActionType() != null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Game> getAllGames() {
        try {
            ArrayList<Game> games = new ArrayList<Game>();

            for (Game game : _db.select(Game.class, new ArrayList<Clause>()))
            {
                if(game.gameState.isRequest())
                    continue;

                games.add(game);
            }

            return games;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Game> searchForGamesAsObserver(String gamesToSearch) {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.LIKE, "%" + gamesToSearch + "%", LinkMethod.OR));
        clauses.add(new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.LIKE, "%" + gamesToSearch + "%"));

        try {
            Map<Integer, Game> map = new HashMap<>();

            for (Game game : _db.select(Game.class, clauses))
            {
                if(game.gameState.isRequest())
                    continue;

                if(map.containsKey(game.getGameID()))
                    continue;

                map.put(game.getGameID(), game);
            }

            return new ArrayList<Game>(map.values());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Game> searchForGamesAsPlayer(String currentGamesToSearch) {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.EQUAL, _username));
        clauses.add(new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.LIKE, "%" + currentGamesToSearch + "%"));

        try {
            var foundGames = new ArrayList<Game>();
            for (Game game : _db.select(Game.class, clauses))
            {
                foundGames.add(game);
            }

            return foundGames;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<String> getPlayerRoles() {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("accountrole", -1), "username", CompareMethod.EQUAL, _username));

        try {
            ArrayList<String> accountRoles = new ArrayList<>();

            for (AccountInfo acc : _db.select(AccountInfo.class, clauses))
            {
                accountRoles.add(acc.role.getRole());
            }

            return accountRoles;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
