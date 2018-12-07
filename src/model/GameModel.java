package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.tables.Turn;
import model.tables.TurnPlayer1;
import model.tables.TurnPlayer2;

import java.util.ArrayList;

public class GameModel {

    private ArrayList<String> _allowedWords = new ArrayList<>();

    private int _currentTurn;
    private Board _board;

    private int _gameId;
    private int _turnId;

    private int _playerScore1;
    private  int _playerScore2;

    private String _playerName1;
    private String _playerName2;

    public int getPlayerScore1() { return _playerScore1; }
    public int getPlayerScore2() { return _playerScore2; }
    public int turn() { return _turnId; }

    public GameModel()
    {
        _board = new Board();
    }

    public Letter[] getDock()
    {
        //TODO: get letters from database
        return new Letter[]
        {
            new Letter("A"),
            new Letter("G"),
            new Letter("D"),
            new Letter("L"),
            new Letter("E"),
            new Letter("N"),
            new Letter("E"),
        };
    }

    public Tile[][] getTiles(){ return _board.getTiles(); }

    public boolean tileIsEmpty(Vector2 vector2) { return _board.isEmpty(vector2); }

    public void placeTile(Vector2 vector2, String letter, int letterId){ _board.place(vector2, letter, letterId); }

    public Tile removeTile(Vector2 vector2) { return _board.remove(vector2); }

    public CheckInfo checkBoard(Vector2 vector2) { return _board.check(vector2); }

    //Submit a piece to the database
    public void submitTurnP1(CheckInfo checkInfo){
        //TODO zorg dat hij weet of hij speler 1 of 2 is en die database tabel aanpast
        Database db = DocumentSession.getDatabase();
        var clauses = new ArrayList<Clause>();

        try{
            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn_id", CompareMethod.EQUAL, _turnId + 1));

            var results = db.select(TurnPlayer2.class, clauses);

            boolean uploadedLast = results.size() > 0;

            db.insert(new Turn(_gameId, _turnId));
            db.insert(new TurnPlayer1(1,5, _playerName1, 10, 1, "play"));

            if(uploadedLast) {

                boolean equalScore = results.get(0).getScore().equals(checkInfo.getPoints().score()); //Compare player 2 score with own score

                var result = results.get(0);

                if(equalScore) { db.update(new TurnPlayer2(_gameId, _turnId, _playerName2, result.getScore(), result.getBonus() + 5, "play")); }
            }

            //for (var tile : checkInfo.)

        }catch (Exception e){
            e.printStackTrace();
        }
        //db.insert(new InsertedKeys())
        _turnId++;
        _board.clearPlacedCoords();
    }

    public void submitTurnP2(CheckInfo checkInfo){
        //TODO zorg dat hij weet of hij speler 1 of 2 is en die database tabel aanpast
        Database db = DocumentSession.getDatabase();
        var clauses = new ArrayList<Clause>();

        try{
            clauses.add(new Clause(new TableAlias("turnplayer1", -1), "turn", CompareMethod.EQUAL, _turnId + 1));

            var results = db.select(TurnPlayer1.class, clauses);

            boolean uploadedLast = results.size() > 0;

            db.insert(new Turn(_gameId, _turnId));
            db.insert(new TurnPlayer2(_gameId,_turnId, _playerName1, checkInfo.getPoints().score(), checkInfo.getPoints().bonus(), "play"));

            if(uploadedLast) {

                boolean equalScore = results.get(0).getScore().equals(checkInfo.getPoints().score()); //Compare player 2 score with own score

                var result = results.get(0);

                if(equalScore) { db.update(new TurnPlayer1(_gameId, _turnId, _playerName1, result.getScore(), result.getBonus() + 5, "play")); }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //db.insert(new InsertedKeys())
        _turnId++;
        _board.clearPlacedCoords();
    }
}

