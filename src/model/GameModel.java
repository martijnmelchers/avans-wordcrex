package model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameModel {

    private ArrayList<String> _allowedWords = new ArrayList<>();

    Database db;

    private Timer timer;

    private Dock dock;

    private Board _board;

    private int _gameId;
    private int _turnId;

    private int _playerScore1;
    private  int _playerScore2;

    private String _playerName1;
    private String _playerName2;

    public int getPlayerScore1() { return _playerScore1; }
    public int getPlayerScore2() { return _playerScore2; }

    public String getPlayerName1() { return _playerName1; }
    public String getPlayername2() { return _playerName2; }

    public int turn() { return _turnId; }

    public GameModel(Game game)
    {
        try{
            this.db = DocumentSession.getDatabase();
        }catch (Exception e){
            Log.error(e);
        }

        _gameId = game.getGameId();
        _board = new Board();

        try{
            var clauses = new ArrayList<Clause>();
            clauses.add(new Clause(new TableAlias("turn", -1), "game_id", CompareMethod.EQUAL, _gameId));

            for (Turn turn : db.select(Turn.class, clauses)) {
                if(_turnId < turn.getTurnId()) _turnId = turn.getTurnId();
            }

            _playerName1 = game.getUsernamePlayer1();
            _playerName2 = game.getUsernamePlayer2();

            if(isPlayerOne())
            {
                if(_turnId==0)
                {
                    createNewTurn();
                    dock = new Dock(true,_gameId,_turnId);
                }
                else
                {
                    dock = new Dock(false,_gameId ,_turnId );
                }

            }
            else
            {
                dock = new Dock(false,_gameId ,_turnId );
            }

            _board.getBoardFromDatabase(_gameId,_turnId );

            clauses.clear();

            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "turn_id", CompareMethod.EQUAL, _turnId));
            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "game_id", CompareMethod.EQUAL, _gameId));

           for (TurnPlayer1 turnPlayer1 : db.select(TurnPlayer1.class, clauses)){
               _playerScore1 = turnPlayer1.getScore()  + turnPlayer1.getBonus();
           }

            clauses.clear();

            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.EQUAL, _turnId));
            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "game_id", CompareMethod.EQUAL, _gameId));

            for (TurnPlayer2 turnPlayer2 : db.select(TurnPlayer2.class, clauses)){
                _playerScore2 = turnPlayer2.getScore()  + turnPlayer2.getBonus();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public HandLetter[] getLetters()
    {
        return dock.getLetters();
    }

    private boolean isPlayerOne()
    {
       return _playerName1.equals(GameSession.getUsername());
    }

    public void waitForPlayer(Task finished)
    {
        TimerTask task = new TimerTask() {
            @Override
            public void run()
            {
                if(isNewTurn())
                {
                    finished.run();
                    if(timer != null)
                    {
                        timer.cancel();
                        timer.purge();
                    }

                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 3000,3000);
    }

    public HandLetter[] getDock()
    {
        return dock.getLetters();
    }

    public Tile[][] getTiles(){ return _board.getTiles(); }

    public boolean tileIsEmpty(Vector2 vector2) { return _board.isEmpty(vector2); }

    public void placeTile(Vector2 vector2, String letter, int letterId){ _board.place(vector2, letter, letterId); }

    public Tile removeTile(Vector2 vector2) { return _board.remove(vector2); }

    public CheckInfo checkBoard() { return _board.check(); }


    public void submitTurn(CheckInfo checkInfo,Task onEndTurn)
    {
        // submit turn to database tables: boardplayer1, turnplayer1 OR boardplayer2,

        if(isPlayerOne())
        {
            submitTurnP1(checkInfo);
        }
        else
        {
            submitTurnP2(checkInfo);
        }


        if(bothPlayersFinished())//players both finished
        {
            //submit tiles of the winner to the database table:'turnboardletters'
            submitWinnerBoard();

            // Create new turn
            createNewTurn();

            removeUsedLettersInDock(checkInfo);

            //refill winners hand + insert hand to database
            dock.refill(_gameId,_turnId);

            _board.getBoardFromDatabase(_gameId,_turnId);

            onEndTurn.run();
        }
        else // other player not finished
        {
            // wait for other player 3 seconds timer interval
            waitForPlayer(new Task() {
                @Override
                protected Object call() // This gets called when other player is ready
                {
                    // when other player ready: get updated board + hand + score (other player created the new hand + updated the board in the database)
                    dock.update(_gameId,_turnId);// update hand
                    _board.getBoardFromDatabase(_gameId,_turnId);
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run()
                        {
                            onEndTurn.run();
                        }
                    });
                    return null;
                }
            });
        }
    }

    public void removeUsedLettersInDock(CheckInfo info)
    {
        Vector2[] coords = info.getCoordinates();
        ArrayList<Integer> usedLetterIds = new ArrayList<Integer>();
        for(Vector2 coord : coords)
        {
            usedLetterIds.add(_board.getTiles()[coord.getX()][coord.getY()].getLetterType().getid());
        }
        dock.removeUsedLetters(usedLetterIds);
    }

    private void createNewTurn()
    {
        _turnId ++;
        try
        {
            db.insert(new Turn(_gameId, _turnId));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Submit a piece to the database
    private void submitTurnP1(CheckInfo checkInfo){
        //TODO zorg dat hij weet of hij speler 1 of 2 is en die database tabel aanpast
        var clauses = new ArrayList<Clause>();

        try{
            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "game_id", CompareMethod.EQUAL, _turnId));
            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.EQUAL, _turnId));

            var results = db.select(TurnPlayer2.class, clauses);

            boolean uploadedLast = results.size() > 0;

            db.insert(new TurnPlayer1(_gameId,_turnId, _playerName1, checkInfo.getPoints().score(), checkInfo.getPoints().bonus(), "play"));

            if(uploadedLast) {

                var result = results.get(0);

                boolean equalScore = results.get(0).getScore().equals(checkInfo.getPoints().score()); //Compare player 2 score with own score
                if(equalScore) { db.update(new TurnPlayer2(_gameId, _turnId, _playerName2, result.getScore(), result.getBonus() + 5, "play")); }
            }

            Vector2[] c = checkInfo.getCoordinates();

            //insert alle tiles in tile en boardplayer1

            //TODO in database tileType moet je kijke wat -- en * zijn (default?)
            for (int i = 0; i < c.length; i++){

                int letterId = _board.getTiles()[c[i].getX()][c[i].getY()].getLetterType().getid();

                db.insert(new BoardPlayer1(_gameId, _playerName1, _turnId, letterId,c[i].getX(), c[i].getY())); // Insert in Boardplayer 1
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //db.insert(new InsertedKeys())
        _board.clearPlacedCoords();
    }

    private void submitTurnP2(CheckInfo checkInfo){
        //TODO zorg dat hij weet of hij speler 1 of 2 is en die database tabel aanpast
        var clauses = new ArrayList<Clause>();

        try{
            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "game_id", CompareMethod.EQUAL, _turnId));
            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "turn_id", CompareMethod.EQUAL, _turnId));

            var results = db.select(TurnPlayer1.class, clauses);

            boolean uploadedLast = results.size() > 0;

            db.insert(new TurnPlayer2(_gameId,_turnId, _playerName2, checkInfo.getPoints().score(), checkInfo.getPoints().bonus(), "play"));

            if(uploadedLast) {

                boolean equalScore = results.get(0).getScore().equals(checkInfo.getPoints().score()); //Compare player 2 score with own score

                var result = results.get(0);

                if(equalScore) { db.update(new TurnPlayer1(_gameId, _turnId, _playerName1, result.getScore(), result.getBonus() + 5, "play")); }
            }

            Vector2[] c = checkInfo.getCoordinates();
            Tile[] tiles = checkInfo.getTiles();
            //insert alle tiles in tile en boardplayer1

            //TODO in database tileType moet je kijke nwat -- en * zijn (default?)
            for (int i = 0; i < c.length; i++){

                int letterId = _board.getTiles()[c[i].getX()][c[i].getY()].getLetterType().getid();
                db.insert(new model.tables.BoardPlayer2(_gameId, _playerName2, _turnId, letterId,c[i].getX(), c[i].getY())); // Insert in Boardplayer 2
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //db.insert(new InsertedKeys())
        _board.clearPlacedCoords();

    }

    private String getWinner()
    {
        List<Clause> player1Clauses = new ArrayList<>();
        List<Clause> player2Clauses = new ArrayList<>();
        player1Clauses.add(new Clause( new TableAlias("TurnPlayer1",-1) ,"turn_id",CompareMethod.EQUAL ,_turnId ));
        player1Clauses.add(new Clause( new TableAlias("TurnPlayer1",-1) ,"game_id",CompareMethod.EQUAL ,_gameId ));
        player2Clauses.add(new Clause( new TableAlias("TurnPlayer2",-1) ,"turn_id",CompareMethod.EQUAL ,_turnId ));
        player2Clauses.add(new Clause( new TableAlias("TurnPlayer2",-1) ,"game_id",CompareMethod.EQUAL ,_gameId ));
        try
        {
            List<TurnPlayer1> turnPlayer1 = db.select(TurnPlayer1.class, player1Clauses);
            List<TurnPlayer2> turnPlayer2 = db.select(TurnPlayer2.class, player2Clauses);

            if(!(turnPlayer1.size()<1)&&!(turnPlayer2.size()<1))
            {
                int scoreP1 = turnPlayer1.get(0).getScore() + turnPlayer1.get(0).getBonus();
                int scoreP2 = turnPlayer2.get(0).getScore() + turnPlayer2.get(0).getBonus();
                if(scoreP1 > scoreP2 )
                {
                    return "player1";
                }
                else
                {
                    return "player2";
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void submitWinnerBoard()
    {
        String winner = getWinner();

        List<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause( new TableAlias("Board" + winner,-1) ,"turn_id",CompareMethod.EQUAL ,_turnId ));
        clauses.add(new Clause( new TableAlias("Board" + winner,-1) ,"game_id",CompareMethod.EQUAL ,_gameId ));

        Vector2[] coords;

        try
        {
            if(winner.equals("player1"))
            {
                List<BoardPlayer1> boardPlayer1 = db.select(BoardPlayer1.class, clauses);
                coords = boardPlayer1.stream().map(a-> new Vector2(a.tile.getX(),a.tile.getY())).toArray(Vector2[]::new);
            }
            else
            {
                List<BoardPlayer2> boardPlayer2 = db.select(BoardPlayer2.class, clauses);
                coords = boardPlayer2.stream().map(a-> new Vector2(a.tile.getX(),a.tile.getY())).toArray(Vector2[]::new);
            }
        }
        catch (Exception e)
        {
            Log.error(e,false );
            return;
        }

        //filter because database returns double results (autojoins)

        ArrayList<TurnBoardLetter> turnBoardLetters = new ArrayList<>();
        for(Vector2 vector2 : coords)
        {
            if(turnBoardLetters.stream().anyMatch(a-> a.getX() == vector2.getX()&& a.getY() == vector2.getY() ))
            {
                continue;
            }
            Tile tile = _board.getTiles()[vector2.getX()][vector2.getY()];
            TurnBoardLetter turnBoardLetter = new TurnBoardLetter(tile.getLetterType().getid(),_gameId,_turnId,vector2.getX(),vector2.getY());
            turnBoardLetters.add(turnBoardLetter);
        }
        try
        {
            db.insert(turnBoardLetters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean isNewTurn()
    {
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Turn",-1), "turn_id", CompareMethod.EQUAL,_turnId+1 ));
        clauses.add(new Clause(new TableAlias("Turn",-1), "game_id", CompareMethod.EQUAL,_gameId ));
        try
        {
             return db.select(Turn.class,  clauses).size()>0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private boolean bothPlayersFinished()
    {
        List<Clause> player1Clauses = new ArrayList<>();
        List<Clause> player2Clauses = new ArrayList<>();
        player1Clauses.add(new Clause( new TableAlias("TurnPlayer1",-1) ,"turn_id",CompareMethod.EQUAL ,_turnId ));
        player1Clauses.add(new Clause( new TableAlias("TurnPlayer1",-1) ,"game_id",CompareMethod.EQUAL ,_gameId ));
        player2Clauses.add(new Clause( new TableAlias("TurnPlayer2",-1) ,"turn_id",CompareMethod.EQUAL ,_turnId ));
        player2Clauses.add(new Clause( new TableAlias("TurnPlayer2",-1) ,"game_id",CompareMethod.EQUAL ,_gameId ));
        try
        {
            List<TurnPlayer1> turnPlayer1 = db.select(TurnPlayer1.class, player1Clauses);
            List<TurnPlayer2> turnPlayer2 = db.select(TurnPlayer2.class, player2Clauses);

            if(!(turnPlayer1.size()<1)&&!(turnPlayer2.size()<1))
            {
                int scoreP1 = turnPlayer1.get(0).getScore() + turnPlayer1.get(0).getBonus();
                int scoreP2 = turnPlayer2.get(0).getScore() + turnPlayer2.get(0).getBonus();
                return true;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;

    }



}

