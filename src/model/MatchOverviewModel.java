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

    public List<Game> getCurrentPlayerGames(String username) {
        return findCurrentPlayerGame(username);
    }

    private List<Game> findCurrentPlayerGame(String username) {
        try {

            var clauses = new ArrayList<Clause>();

            clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.EQUAL, username, LinkMethod.OR));
            clauses.add(new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.EQUAL, username, LinkMethod.OR));

            var games = this._db.select(Game.class, clauses);

            for (Game game : games) {
                String player = GameSession.getUsername();
                String player1 = game.getPlayer1().getUsername();

                boolean isPlayer1 = player.equals(player1);

                if (isPlayer1) {
                    if (!currentTurnHasAction(game) && !currentTurnPlayer2HasAction(game)) {
                        MatchOverviewModel.currentTurns.put(game, true);
                    } else {
                        MatchOverviewModel.currentTurns.put(game, currentTurnHasAction(game));
                    }
                } else {
                    if (!currentTurnHasAction(game) && !currentTurnPlayer2HasAction(game)) {
                        MatchOverviewModel.currentTurns.put(game, true);
                    } else {
                        MatchOverviewModel.currentTurns.put(game, currentTurnPlayer2HasAction(game));
                    }
                }
            }

            return games;
        } catch (Exception e) {
            Log.error(e);
        }

        return null;
    }

    public boolean currentTurnHasAction(Game game) {
        var latestTurn = this.getLatestTurnOfGame(game);

        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("turnplayer1", -1), "username_player1", CompareMethod.EQUAL, game.getPlayer1().getUsername(), LinkMethod.AND));
        clauses.add(new Clause(new TableAlias("turnplayer1", -1), "turn_id", CompareMethod.EQUAL, latestTurn));

        try {
            var turnList = this._db.select(TurnPlayer1.class, clauses);
            if (turnList.size() > 0)
                return true;

        } catch (Exception e) {
            Log.error(e);
        }

        return false;
    }

    public boolean currentTurnPlayer2HasAction(Game game) {
        Integer latestTurn = getLatestTurnOfGame(game);

        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("turnplayer2", -1), "username_player2", CompareMethod.EQUAL, game.getPlayer2().getUsername(), LinkMethod.AND));
        clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.EQUAL, latestTurn));

        try {
            var turnList = _db.select(TurnPlayer2.class, clauses);
            if (turnList.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    private int getLatestTurnOfGame(Game game) {
        int latestTurn = 0;

        var turnClauses = new ArrayList<Clause>();
        turnClauses.add(new Clause(new TableAlias("turn", -1), "game_id", CompareMethod.EQUAL, game.getGameId()));

        try {
            for (Turn turn : _db.select(Turn.class, turnClauses)) {
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

    public ArrayList<Game> getAllGames() {
        try {
            ArrayList<Game> games = new ArrayList<>();

            var clauses = new ArrayList<Clause>();

            for (Game game : _db.select(Game.class, clauses)) {
                if (!game.getGameState().isPlaying())
//                    continue;

                    games.add(game);
            }

            return games;
        } catch (Exception e) {
            Log.error(e);
        }

        return null;
    }


    public ArrayList<Game> searchForGamesAsObserver(String gamesToSearch) {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("game", -1), "username_player1", CompareMethod.LIKE, "%" + gamesToSearch + "%", LinkMethod.OR));
        clauses.add(new Clause(new TableAlias("game", -1), "username_player2", CompareMethod.LIKE, "%" + gamesToSearch + "%"));

        try {
            Map<Integer, Game> map = new HashMap<>();

            for (Game game : _db.select(Game.class, clauses)) {
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
            return new ArrayList<>(_db.select(Game.class, clauses));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<String> getPlayerRoles() {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("accountrole", -1), "username", CompareMethod.EQUAL, GameSession.getUsername()));

        try {
            ArrayList<String> accountRoles = new ArrayList<>();

            for (AccountInfo acc : _db.select(AccountInfo.class, clauses)) {
                accountRoles.add(acc.getRole().getRole());
            }

            return accountRoles;
        } catch (Exception e) {
            Log.error(e);
        }

        return null;
    }

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

    public void acceptInvite(Game game) {
        game.setState("playing");
        game.setAnswer("accepted");

        try {
            this._db.update(game);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void declineInvite(Game game) {
        game.setAnswer("rejected");

        try {
            this._db.update(game);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public GameScore getPlayerScores(Game game) {
        GameScore score = new GameScore();

        var clauses1 = new ArrayList<Clause>();
        clauses1.add(new Clause(new TableAlias("turnplayer1", -1), "game_id", CompareMethod.EQUAL, game.getGameId()));

        var clauses2 = new ArrayList<Clause>();
        clauses2.add(new Clause(new TableAlias("turnplayer2", -1), "game_id", CompareMethod.EQUAL, game.getGameId()));

        try {
            for (TurnPlayer1 turn1 : _db.select(TurnPlayer1.class, clauses1))
                score.player1 += turn1.getScore() + turn1.getBonus();


            for (TurnPlayer2 turn2 : _db.select(TurnPlayer2.class, clauses2))
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
