package model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.util.Pair;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel {

    private ArrayList<String> _allowedWords = new ArrayList<>();

    private Database _db;

    private Timer _timer;

    private Dock _dock;

    private Board _board;

    private int _gameId;
    private int _turnId;

    private int _playerScore1;
    private int _playerScore2;

    private String _playerName1;
    private String _playerName2;

    public GameModel(Game game) {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (Exception e) {
            Log.error(e);
        }

        _gameId = game.getGameID();
        _board = new Board();

        try {
            var clauses = new ArrayList<Clause>();
            clauses.add(new Clause(new TableAlias("turn", -1), "game_id", CompareMethod.EQUAL, _gameId));

            for (Turn turn : _db.select(Turn.class, clauses)) {
                if (_turnId < turn.getTurnID()) _turnId = turn.getTurnID();
            }

            _playerName1 = game.getPlayer1Username();
            _playerName2 = game.getPlayer2Username();

            if (isPlayerOne()) {
                if (_turnId == 0) {
                    createNewTurn();
                    _dock = new Dock(true, _gameId, _turnId);
                } else {
                    _dock = new Dock(false, _gameId, _turnId);
                }

            } else {
                _dock = new Dock(false, _gameId, _turnId);
            }

            _board.getBoardFromDatabase(_gameId, _turnId);

            clauses.clear();

            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "game_id", CompareMethod.EQUAL, _gameId));
            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "turn_id", CompareMethod.LESS_EQUAL, _turnId));
            try {
                int score = 0;
                List<TurnPlayer1> turnPlayer1s = _db.select(TurnPlayer1.class, clauses);
                for (TurnPlayer1 turnPlayer1 : turnPlayer1s) {
                    score += (turnPlayer1.getScore() + turnPlayer1.getBonus());
                }
                this._playerScore1 = score;
            } catch (Exception e) {
                Log.error(e, false);
            }

            clauses.clear();

            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "game_id", CompareMethod.EQUAL, _gameId));
            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.LESS_EQUAL, _turnId));
            try {
                int score = 0;
                List<TurnPlayer2> turnPlayer2s = _db.select(TurnPlayer2.class, clauses);
                for (TurnPlayer2 turnPlayer2 : turnPlayer2s) {
                    score += (turnPlayer2.getScore() + turnPlayer2.getBonus());
                }
                this._playerScore2 = score;
            } catch (Exception e) {
                Log.error(e, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getPlayerScore1() {
        return _playerScore1;
    }

    public int getPlayerScore2() {
        return _playerScore2;
    }

    public String getPlayerName1() {
        return _playerName1;
    }

    public String getPlayername2() {
        return _playerName2;
    }

    public int turn() {
        return _turnId;
    }

    public HandLetter[] getLetters() {
        return _dock.getLetters();
    }

    private boolean isPlayerOne() {
        return _playerName1.equals(GameSession.getUsername());
    }

    public void waitForPlayer(Task finished) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isNewTurn()) {
                    _turnId++;
                    finished.run();
                    if (_timer != null) {
                        _timer.cancel();
                        _timer.purge();
                    }
                }
            }
        };
        _timer = new Timer();
        _timer.schedule(task, 3000, 3000);
    }

    public HandLetter[] getDock() {
        return _dock.getLetters();
    }

    public Tile[][] getTiles() {
        return _board.getTiles();
    }

    public boolean tileIsEmpty(Vector2 vector2) {
        return _board.isEmpty(vector2);
    }

    public void placeTile(Vector2 vector2, String letter, int letterId) {
        _board.place(vector2, letter, letterId);
    }

    public Tile removeTile(Vector2 vector2) {
        return _board.remove(vector2);
    }

    public ArrayList<Tile> removeTiles() {
        return _board.removeTiles();
    }

    public CheckInfo checkBoard() {
        return _board.check();
    }

    public boolean checkIfTurnPlayed() {
        if (isPlayerOne()) {
            List<Clause> clauses = new ArrayList<>();
            clauses.add(new Clause(new TableAlias("TurnPlayer1", -1), "turn_id", CompareMethod.EQUAL, _turnId));
            clauses.add(new Clause(new TableAlias("TurnPlayer1", -1), "game_id", CompareMethod.EQUAL, _gameId));
            try {
                List<TurnPlayer1> turnPlayer1 = _db.select(TurnPlayer1.class, clauses);

                if (turnPlayer1.size() != 0) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {
            List<Clause> clauses = new ArrayList<>();
            clauses.add(new Clause(new TableAlias("TurnPlayer2", -1), "turn_id", CompareMethod.EQUAL, _turnId));
            clauses.add(new Clause(new TableAlias("TurnPlayer2", -1), "game_id", CompareMethod.EQUAL, _gameId));
            try {
                List<TurnPlayer2> turnPlayer2 = _db.select(TurnPlayer2.class, clauses);

                if (turnPlayer2.size() != 0) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void submitTurn(CheckInfo checkInfo, Task onEndTurn) {
        // submit turn to database tables: boardplayer1, turnplayer1 OR boardplayer2,

        if (isPlayerOne()) {
            submitTurnP1(checkInfo);
        } else {
            submitTurnP2(checkInfo);
        }


        if (bothPlayersFinished())//players both finished
        {
            //submit tiles of the winner to the database table:'turnboardletters'
            submitWinnerBoard();

            // Create new turn
            createNewTurn();

            //removeUsedLettersInDock(checkInfo);
            _dock.update(_gameId, _turnId);
            //refill winners hand + insert hand to database
            _dock.refill(_gameId, _turnId);

            _board.getBoardFromDatabase(_gameId, _turnId);

            onEndTurn.run();

            checkGameFinished();
        } else // other player not finished
        {
            alreadyPlayed(onEndTurn);
        }
    }

    public void alreadyPlayed(Task onEndTurn) {
        waitForPlayer(new Task() {
            @Override
            protected Object call() // This gets called when other player is ready
            {
                // wait one second for the other player to insert data in the database
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    Log.error(e, false);
                }

                // when other player ready: get updated board + hand + score (other player created the new hand + updated the board in the database)
                _dock.update(_gameId, _turnId);// update hand
                _board.getBoardFromDatabase(_gameId, _turnId);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        onEndTurn.run();
                    }
                });

                checkGameFinished();
                return null;
            }
        });
    }

    public void removeUsedLettersInDock(CheckInfo info) {
        Vector2[] coords = info.getCoordinates();
        ArrayList<Integer> usedLetterIds = new ArrayList<Integer>();
        for (Vector2 coord : coords) {
            usedLetterIds.add(_board.getTiles()[coord.getX()][coord.getY()].getLetterType().getid());
        }
        _dock.removeUsedLetters(usedLetterIds);
    }

    private void createNewTurn() {
        _turnId++;
        try {
            _db.insert(new Turn(_gameId, _turnId));
        } catch (Exception e) {
            Log.error(e);
        }
    }

    //Submit a piece to the database
    private void submitTurnP1(CheckInfo checkInfo) {
        //TODO zorg dat hij weet of hij speler 1 of 2 is en die database tabel aanpast
        var clauses = new ArrayList<Clause>();

        try {
            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "game_id", CompareMethod.EQUAL, _gameId));
            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.EQUAL, _turnId));

            var results = _db.select(TurnPlayer2.class, clauses);

            boolean uploadedLast = results.size() > 0;

            if (checkInfo.getPoints().total() == 0 && checkInfo.getTiles() == null && checkInfo.getCoordinates() == null) {
                _db.insert(new TurnPlayer1(_gameId, _turnId, _playerName1, checkInfo.getPoints().score(), checkInfo.getPoints().bonus(), "pass"));
            } else {
                _db.insert(new TurnPlayer1(_gameId, _turnId, _playerName1, checkInfo.getPoints().score(), checkInfo.getPoints().bonus(), "play"));
            }

            if (uploadedLast) {

                var result = results.get(0);

                boolean equalScore = results.get(0).getScore().equals(checkInfo.getPoints().score()); //Compare player 2 score with own score

                // Checken of niet beide players hebben gepassed
                if (!(checkInfo.getPoints().total() == 0 && (result.getScore() + result.getBonus()) == 0)) {
                    if (equalScore) {
                        _db.update(new TurnPlayer2(_gameId, _turnId, _playerName2, result.getScore(), result.getBonus() + 5, "play"));
                    }
                }
            }

            Vector2[] c = checkInfo.getCoordinates();

            //insert alle tiles in tile en boardplayer1

            //TODO in database tileType moet je kijke wat -- en * zijn (default?)
            for (int i = 0; i < c.length; i++) {

                int letterId = _board.getTiles()[c[i].getY()][c[i].getX()].getLetterType().getid();

                _db.insert(new BoardPlayer1(_gameId, _playerName1, _turnId, letterId, (c[i].getX() + 1), (c[i].getY() + 1))); // Insert in Boardplayer 1
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //db.insert(new InsertedKeys())
        _board.clearPlacedCoords();
    }

    public void updateScore() {
        updateScore(_turnId);
    }

    public void updateScore(int turnId) {
        List<Clause> clausesP1 = new ArrayList<>();
        clausesP1.add(new Clause(new TableAlias("turnplayer1", -1), "game_id", CompareMethod.EQUAL, _gameId));
        clausesP1.add(new Clause(new TableAlias("turnplayer1", -1), "turn_id", CompareMethod.LESS_EQUAL, turnId));
        try {
            int score = 0;
            List<TurnPlayer1> turnPlayer1s = _db.select(TurnPlayer1.class, clausesP1);
            for (TurnPlayer1 turnPlayer1 : turnPlayer1s) {
                score += (turnPlayer1.getScore() + turnPlayer1.getBonus());
            }
            this._playerScore1 = score;
        } catch (Exception e) {
            Log.error(e, false);
        }

        List<Clause> clausesP2 = new ArrayList<>();
        clausesP2.add(new Clause(new TableAlias("turnplayer2", -1), "game_id", CompareMethod.EQUAL, _gameId));
        clausesP2.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.LESS_EQUAL, turnId));
        try {
            int score = 0;
            List<TurnPlayer2> turnPlayer2s = _db.select(TurnPlayer2.class, clausesP2);
            for (TurnPlayer2 turnPlayer2 : turnPlayer2s) {
                score += (turnPlayer2.getScore() + turnPlayer2.getBonus());
            }
            this._playerScore2 = score;
        } catch (Exception e) {
            Log.error(e, false);
        }

    }

    private void submitTurnP2(CheckInfo checkInfo) {
        //TODO zorg dat hij weet of hij speler 1 of 2 is en die database tabel aanpast
        var clauses = new ArrayList<Clause>();

        try {
            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "game_id", CompareMethod.EQUAL, _gameId));
            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "turn_id", CompareMethod.EQUAL, _turnId));

            var results = _db.select(TurnPlayer1.class, clauses);

            boolean uploadedLast = results.size() > 0;

            if (checkInfo.getPoints().total() == 0 && checkInfo.getCoordinates() == null && checkInfo.getTiles() == null) {
                _db.insert(new TurnPlayer2(_gameId, _turnId, _playerName2, checkInfo.getPoints().score(), checkInfo.getPoints().bonus(), "pass"));
            } else {
                _db.insert(new TurnPlayer2(_gameId, _turnId, _playerName2, checkInfo.getPoints().score(), checkInfo.getPoints().bonus(), "play"));
            }

            if (uploadedLast) {

                boolean equalScore = results.get(0).getScore().equals(checkInfo.getPoints().score()); //Compare player 2 score with own score

                var result = results.get(0);

                // Checken of niet beide players hebben gepassed
                if (!(checkInfo.getPoints().total() == 0 && (result.getScore() + result.getBonus()) == 0)) {
                    if (equalScore) {
                        _db.update(new TurnPlayer1(_gameId, _turnId, _playerName1, result.getScore(), result.getBonus() + 5, "play"));
                    }
                }
            }

            Vector2[] c = checkInfo.getCoordinates();
            Tile[] tiles = checkInfo.getTiles();
            //insert alle tiles in tile en boardplayer1

            //TODO in database tileType moet je kijke nwat -- en * zijn (default?)
            for (int i = 0; i < c.length; i++) {

                int letterId = _board.getTiles()[c[i].getY()][c[i].getX()].getLetterType().getid();
                _db.insert(new model.tables.BoardPlayer2(_gameId, _playerName2, _turnId, letterId, (c[i].getX() + 1), (c[i].getY() + 1))); // Insert in Boardplayer 2
            }

        } catch (Exception e) {
            Log.error(e);
        }
        //db.insert(new InsertedKeys())
        _board.clearPlacedCoords();

    }

    private String getWinner() {
        List<Clause> player1Clauses = new ArrayList<>();
        List<Clause> player2Clauses = new ArrayList<>();
        player1Clauses.add(new Clause(new TableAlias("TurnPlayer1", -1), "turn_id", CompareMethod.EQUAL, _turnId));
        player1Clauses.add(new Clause(new TableAlias("TurnPlayer1", -1), "game_id", CompareMethod.EQUAL, _gameId));
        player2Clauses.add(new Clause(new TableAlias("TurnPlayer2", -1), "turn_id", CompareMethod.EQUAL, _turnId));
        player2Clauses.add(new Clause(new TableAlias("TurnPlayer2", -1), "game_id", CompareMethod.EQUAL, _gameId));
        try {
            List<TurnPlayer1> turnPlayer1 = _db.select(TurnPlayer1.class, player1Clauses);
            List<TurnPlayer2> turnPlayer2 = _db.select(TurnPlayer2.class, player2Clauses);

            if (!(turnPlayer1.size() < 1) && !(turnPlayer2.size() < 1)) {
                int scoreP1 = turnPlayer1.get(0).getScore() + turnPlayer1.get(0).getBonus();
                int scoreP2 = turnPlayer2.get(0).getScore() + turnPlayer2.get(0).getBonus();
                if (scoreP1 > scoreP2) {
                    return "player1";
                } else {
                    return "player2";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void submitWinnerBoard() {
        String winner = getWinner();

        List<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Board" + winner, -1), "turn_id", CompareMethod.EQUAL, _turnId));
        clauses.add(new Clause(new TableAlias("Board" + winner, -1), "game_id", CompareMethod.EQUAL, _gameId));

        List<Pair<Vector2, Integer>> idsAndXY = new ArrayList<>();

        try {
            if (winner.equals("player1")) {
                List<BoardPlayer1> boardPlayer1 = _db.select(BoardPlayer1.class, clauses);
                for (BoardPlayer1 bp : boardPlayer1) {
                    Pair<Vector2, Integer> pair = new Pair<>(new Vector2((bp.tile.getX() - 1), (bp.tile.getY() - 1)), bp.letter.getLetterId());
                    idsAndXY.add(pair);
                }
            } else {
                List<BoardPlayer2> boardPlayer2 = _db.select(BoardPlayer2.class, clauses);
                for (BoardPlayer2 bp : boardPlayer2) {
                    Pair<Vector2, Integer> pair = new Pair<>(new Vector2((bp.tile.getX() - 1), (bp.tile.getY() - 1)), bp.letter.getLetterId());
                    idsAndXY.add(pair);
                }

            }
        } catch (Exception e) {
            Log.error(e, false);
            return;
        }

        //filter because database returns double results (autojoins)

        ArrayList<TurnBoardLetter> turnBoardLetters = new ArrayList<>();
        for (Pair<Vector2, Integer> pair : idsAndXY) {
            if (turnBoardLetters.stream().anyMatch(a -> a.getX() == (pair.getKey().getX() + 1) && a.getY() == (pair.getKey().getY() + 1))) {
                continue;
            }
            Tile tile = _board.getTiles()[pair.getKey().getY()][pair.getKey().getX()];
            TurnBoardLetter turnBoardLetter = new TurnBoardLetter(pair.getValue(), _gameId, _turnId, (pair.getKey().getX() + 1), (pair.getKey().getY() + 1));
            turnBoardLetters.add(turnBoardLetter);
        }
        try {
            _db.insert(turnBoardLetters);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    private boolean isNewTurn() {
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Turn", -1), "turn_id", CompareMethod.EQUAL, _turnId + 1));
        clauses.add(new Clause(new TableAlias("Turn", -1), "game_id", CompareMethod.EQUAL, _gameId));
        try {
            return _db.select(Turn.class, clauses).size() > 0;
        } catch (Exception e) {
            Log.error(e);
        }
        return false;
    }

    private boolean bothPlayersFinished() {
        List<Clause> player1Clauses = new ArrayList<>();
        List<Clause> player2Clauses = new ArrayList<>();
        player1Clauses.add(new Clause(new TableAlias("TurnPlayer1", -1), "turn_id", CompareMethod.EQUAL, _turnId));
        player1Clauses.add(new Clause(new TableAlias("TurnPlayer1", -1), "game_id", CompareMethod.EQUAL, _gameId));
        player2Clauses.add(new Clause(new TableAlias("TurnPlayer2", -1), "turn_id", CompareMethod.EQUAL, _turnId));
        player2Clauses.add(new Clause(new TableAlias("TurnPlayer2", -1), "game_id", CompareMethod.EQUAL, _gameId));
        try {
            List<TurnPlayer1> turnPlayer1 = _db.select(TurnPlayer1.class, player1Clauses);
            List<TurnPlayer2> turnPlayer2 = _db.select(TurnPlayer2.class, player2Clauses);

            if (!(turnPlayer1.size() < 1) && !(turnPlayer2.size() < 1)) {
                int scoreP1 = turnPlayer1.get(0).getScore() + turnPlayer1.get(0).getBonus();
                int scoreP2 = turnPlayer2.get(0).getScore() + turnPlayer2.get(0).getBonus();
                return true;
            }

        } catch (Exception e) {
            Log.error(e);
        }
        return false;

    }

    public void setTurn(int turnId) {
        _board.getBoardFromDatabase(_gameId, turnId);
    }

    public String getNotUsedTiles(int turnId) {
        return _dock.getNotUsedTiles(_gameId, turnId);
    }

    public void getOldDock(int turnId) {
        _dock.update(_gameId, turnId);
    }

    private void checkGameFinished() {

        Game game = GameSession.getGame();
        game.setGameState("finished");

        if (!getNotUsedTiles(_turnId).equals("0")) {
            return;
        }

        if (_playerScore1 > _playerScore2) {
            game.setWinner(_playerName1);
        } else if (_playerScore1 < _playerScore2) {
            game.setWinner(_playerName2);
        } else {
            game.setWinner(_playerName1);
        }

        try {
            _db.update(game);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void surrender() {
        Game game = GameSession.getGame();
        game.setGameState("resigned");

        if (isPlayerOne()) {
            game.setWinner(_playerName2);
        } else {
            game.setWinner(_playerName1);
        }

        try {
            _db.update(game);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public boolean checkGameDone() {
        List<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Game", -1), "game_id", CompareMethod.EQUAL, _gameId));
        try {
            Game game = _db.select(Game.class, clauses).get(0);
            if (game.getGameState().getState().equals("finished") || game.getGameState().getState().equals("resigned")) {
                return true;
            }

        } catch (Exception e) {
            Log.error(e);
        }
        return false;
    }

    public String getGameWinner() {
        List<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Game", -1), "game_id", CompareMethod.EQUAL, _gameId));
        try {
            return _db.select(Game.class, clauses).get(0).getWinner().getUsername();
        } catch (Exception e) {
            Log.error(e);
        }

        return null;
    }

    public String getGameWinnerScore() {
        if (_playerName1.equals(getGameWinner())) {
            return Integer.toString(_playerScore1);
        }

        return Integer.toString(_playerScore2);
    }
}
