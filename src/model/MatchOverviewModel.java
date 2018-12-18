package model;

import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Add comments
 * TODO: Clean up code
 */

public class MatchOverviewModel {
    private static HashMap<Game, Boolean> currentTurns = new HashMap<>();
    private static HashMap<Game, String> observerTurns = new HashMap<>();
    private Database _db;

    public MatchOverviewModel() {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public static boolean isMyTurn(Game game) throws NullPointerException {
        if (MatchOverviewModel.currentTurns.containsKey(game)) {
            return MatchOverviewModel.currentTurns.get(game);
        } else {
            throw new NullPointerException();
        }
    }


    public static String whoTurn(Game game) {
        return MatchOverviewModel.observerTurns.get(game);
    }

    public List<Game> getCurrentPlayerGames(String username) {
        return this.findCurrentPlayerGame(username);
    }


    /**
     * Returns the games of the current user
     * @param username
     * @return
     */
    private List<Game> findCurrentPlayerGame(String username) {
        try {

            var clauses = new ArrayList<Clause>();

            clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.EQUAL, username, LinkMethod.OR));
            clauses.add(new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.EQUAL, username, LinkMethod.OR));

            var games = this._db.select(Game.class, clauses);


            // Caches the turns so we don't need to get the turns every time.
            for (Game game : games) {
                MatchOverviewModel.currentTurns.put(game, !this.checkIfTurnPlayed(game));
            }

            return games;
        } catch (Exception e) {
            Log.error(e);
        }

        return null;
    }


    /**
     * Check if the current user has played a turn.
     * @param game
     * @return
     */
    public boolean checkIfTurnPlayed(Game game) {

        var lastTurn = this.getLatestTurnOfGame(game);

        if (GameSession.getUsername().equals(game.getPlayer1Username())) {
            var clauses = new ArrayList<Clause>();

            clauses.add(new Clause(new TableAlias("TurnPlayer1", -1), "turn_id", CompareMethod.EQUAL, lastTurn));
            clauses.add(new Clause(new TableAlias("TurnPlayer1", -1), "game_id", CompareMethod.EQUAL, game.getGameId()));

            try {
                var turnPlayer1 = this._db.select(TurnPlayer1.class, clauses);

                if (turnPlayer1.size() != 0)
                    return true;

            } catch (Exception e) {
                Log.error(e);
            }

            return false;
        } else {
            var clauses = new ArrayList<Clause>();

            clauses.add(new Clause(new TableAlias("TurnPlayer2", -1), "turn_id", CompareMethod.EQUAL, lastTurn));
            clauses.add(new Clause(new TableAlias("TurnPlayer2", -1), "game_id", CompareMethod.EQUAL, game.getGameId()));

            try {
                var turnPlayer2 = this._db.select(TurnPlayer2.class, clauses);

                if (turnPlayer2.size() != 0)
                    return true;

            } catch (Exception e) {
                Log.error(e);
            }

            return false;
        }
    }


    /**
     * Checks if player1 has played his turn.
     * @param game
     * @return
     */
    public boolean currentTurnHasAction(Game game) {
        var latestTurn = this.getLatestTurnOfGame(game);

        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("turnplayer1", -1), "username_player1", CompareMethod.EQUAL, game.getPlayer1().getUsername(), LinkMethod.AND));
        clauses.add(new Clause(new TableAlias("turnplayer1", -1), "turn_id", CompareMethod.EQUAL, latestTurn));

        try {
            var turnList = this._db.select(TurnPlayer1.class, clauses, false);
            if (turnList.size() > 0)
                return true;

        } catch (Exception e) {
            Log.error(e);
        }

        return false;
    }

    /**
     * Checks if player2 has played his turn.
     * @param game
     * @return
     */
    public boolean currentTurnPlayer2HasAction(Game game) {
        Integer latestTurn = this.getLatestTurnOfGame(game);

        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("turnplayer2", -1), "username_player2", CompareMethod.EQUAL, game.getPlayer2().getUsername(), LinkMethod.AND));
        clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.EQUAL, latestTurn));

        try {
            var turnList = this._db.select(TurnPlayer2.class, clauses);
            if (turnList.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Returns the latest turn id of the game.
     * @param game
     * @return
     */
    private int getLatestTurnOfGame(Game game) {
        int latestTurn = 0;

        var turnClauses = new ArrayList<Clause>();
        turnClauses.add(new Clause(new TableAlias("turn", -1), "game_id", CompareMethod.EQUAL, game.getGameId()));

        try {
            for (Turn turn : this._db.select(Turn.class, turnClauses)) {
                Integer id = turn.getTurnID();
                if (id > latestTurn) {
                    latestTurn = id;
                }
            }
        } catch (Exception e) {
            Log.error(e);
        }

        return latestTurn;
    }

    /**
     * Returns all games in the database
     * @return
     */
    public List<Game> getAllGames() {
        try {
            var games = this._db.select(Game.class);


            // Caches who's turn it is so the observer is much faster.
            for (var game : games) {
                if (!this.currentTurnHasAction(game)) {
                    MatchOverviewModel.observerTurns.put(game, game.getPlayer1Username());
                } else if (!this.currentTurnPlayer2HasAction(game)) {
                    MatchOverviewModel.observerTurns.put(game, game.getPlayer2Username());
                }
            }


            return games;

        } catch (Exception e) {
            Log.error(e);
        }
        return null;
    }

    /**
     * Old function
     * @param gamesToSearch
     * @return
     */
    public ArrayList<Game> searchForGamesAsObserver(String gamesToSearch) {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.LIKE, "%" + gamesToSearch + "%", LinkMethod.OR));
        clauses.add(new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.LIKE, "%" + gamesToSearch + "%"));

        try {
            Map<Integer, Game> map = new HashMap<>();

            for (Game game : this._db.select(Game.class, clauses)) {
                if (game.getGameState().isRequest())
                    continue;

                if (map.containsKey(game.getGameId()))
                    continue;

                map.put(game.getGameId(), game);
            }

            return new ArrayList<>(map.values());
        } catch (Exception e) {
            Log.error(e);
        }

        return null;
    }

    public ArrayList<Game> searchForGamesAsPlayer(String currentGamesToSearch) {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.EQUAL, GameSession.getUsername()));
        clauses.add(new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.LIKE, "%" + currentGamesToSearch + "%"));

        try {
            return new ArrayList<>(this._db.select(Game.class, clauses));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns all roles of the current user.
     * @return
     */
    public ArrayList<String> getPlayerRoles() {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("accountrole", -1), "username", CompareMethod.EQUAL, GameSession.getUsername()));

        try {
            ArrayList<String> accountRoles = new ArrayList<>();

            for (AccountInfo acc : this._db.select(AccountInfo.class, clauses)) {
                accountRoles.add(acc.getRole().getRole());
            }

            return accountRoles;
        } catch (Exception e) {
            Log.error(e);
        }

        return null;
    }


    /**
     * Surrenders the current game.
     * @param game
     */
    public void surrenderGame(Game game) {
        String player = GameSession.getUsername();

        game.setState("resigned");
        game.setWinner(game.getPlayer1().getUsername().equals(player) ? game.getPlayer2().getUsername() : game.getPlayer1().getUsername());

        try {
            this._db.update(game);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    /**
     * Accept an invite.
     * @param game
     */
    public void acceptInvite(Game game) {
        game.setState("playing");
        game.setAnswer("accepted");

        try {
            this._db.update(game);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    /**
     * Decline an invite
     * @param game
     */
    public void declineInvite(Game game) {
        game.setAnswer("rejected");
        try {
            this._db.update(game);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    /**
     * Returns the latest scores of the game.
     * @param game
     * @return
     */
    public GameScore getPlayerScores(Game game) {
        GameScore score = new GameScore();

        var clauses1 = new ArrayList<Clause>();
        clauses1.add(new Clause(new TableAlias("turnplayer1", -1), "game_id", CompareMethod.EQUAL, game.getGameId()));

        var clauses2 = new ArrayList<Clause>();
        clauses2.add(new Clause(new TableAlias("turnplayer2", -1), "game_id", CompareMethod.EQUAL, game.getGameId()));

        try {
            for (TurnPlayer1 turn1 : this._db.select(TurnPlayer1.class, clauses1))
                score.player1 += turn1.getScore() + turn1.getBonus();


            for (TurnPlayer2 turn2 : this._db.select(TurnPlayer2.class, clauses2))
                score.player2 += turn2.getScore() + turn2.getBonus();

        } catch (Exception e) {
            Log.error(e);
        }
        return score;
    }

    public class GameScore {
        public int player1;
        public int player2;
    }

}
