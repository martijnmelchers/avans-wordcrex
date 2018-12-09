package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.database.services.Database;
import model.tables.Game;
import model.tables.TurnPlayer1;
import model.tables.TurnPlayer2;

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

    public ArrayList<Game> getCurrentPlayerGames() {
        return findCurrentPlayerGame();
    }

    public MatchOverviewModel() {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Game> findCurrentPlayerGame() {
        var clauses = new ArrayList<Clause>();
        var foundGames = new ArrayList<Game>();

        clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.EQUAL, _username));
        try
        {
            foundGames.addAll(_db.select(Game.class, clauses));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return foundGames;
    }

    public boolean currentTurnHasAction(Game game) {
        Integer latestTurn = GetLatestTurnOfGame(game);

        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("turnplayer1", -1), "username_player1", CompareMethod.EQUAL, game.player1.getUsername(), LinkMethod.AND));
        clauses.add(new Clause(new TableAlias("turnplayer1", -1), "turn_id", CompareMethod.EQUAL, latestTurn));

        try {
            var turnList = _db.select(TurnPlayer1.class, clauses);
            if(turnList.size() > 0)
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean currentTurnPlayer2HasAction(Game game) {
        Integer latestTurn = GetLatestTurnOfGame(game);

        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("turnplayer2", -1), "username_player2", CompareMethod.EQUAL, game.player2.getUsername(), LinkMethod.AND));
        clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.EQUAL, latestTurn ));

        try {
            var turnList = _db.select(TurnPlayer2.class, clauses);
            if(turnList.size() > 0)
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private int GetLatestTurnOfGame(Game game)
    {
        int latestTurn = 0;

        var turnClauses = new ArrayList<Clause>();
        turnClauses.add(new Clause(new TableAlias("turn", -1), "game_id", CompareMethod.EQUAL, game.getGameID()));

        try
        {
            for (Turn turn : _db.select(Turn.class, turnClauses)) {
                Integer id = turn.getTurnID();
                if(id > latestTurn)
                {
                    latestTurn = id;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return latestTurn;
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

    public GameScore getPlayerScores(Game game) {
        GameScore score = new GameScore();

        ArrayList<Integer> player1 = new ArrayList<>();
        ArrayList<Integer> player2 = new ArrayList<>();

        var clauses1 = new ArrayList<Clause>();
        clauses1.add(new Clause(new TableAlias("turnplayer1", -1), "game_id", CompareMethod.EQUAL, game.getGameID()));

        var clauses2 = new ArrayList<Clause>();
        clauses2.add(new Clause(new TableAlias("turnplayer2", -1), "game_id", CompareMethod.EQUAL, game.getGameID()));

        try {
            for (TurnPlayer1 turn1 : _db.select(TurnPlayer1.class, clauses1))
            {
                score.player1 += turn1.getScore() + turn1.getBonus();
            }

            for (TurnPlayer2 turn2 : _db.select(TurnPlayer2.class, clauses2))
            {
                score.player2 += turn2.getScore() + turn2.getBonus();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return score;
    }

    public class GameScore
    {
        public int player1;
        public int player2;
    }
}
