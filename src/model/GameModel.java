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
    private int _turn;

    private int _playerScore1;
    private  int _playerScore2;

    private String _playerName1;
    private String _playerName2;

    public int getPlayerScore1() { return _playerScore1; }
    public int getPlayerScore2() { return _playerScore2; }
    public int turn() { return _turn; }

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

    public void placeTile(Vector2 vector2, String letter){ _board.place(vector2, letter); }

    //Geeft de tile terug die er lag voordat het verwijderd word
    public Tile removeTile(Vector2 vector2) { return _board.remove(vector2); }

    public CheckInfo checkBoard(Vector2 vector2) { return _board.check(vector2); }

    //Submit a piece to the database
    public void submitTurn(CheckInfo checkInfo){
        //TODO zorg dat hij weet of hij speler 1 of 2 is en die database tabel aanpast
        Database db = DocumentSession.getDatabase();
        var clauses = new ArrayList<Clause>();

        try{
            clauses.add(new Clause(new TableAlias("turnplayer2", -1), "turn", CompareMethod.EQUAL, _turn + 1));

            boolean uploadedLast = db.select(TurnPlayer2.class, clauses).size() > 0;
            boolean equalScore = db.select(TurnPlayer2.class, clauses).get(0).getScore().equals(checkInfo.getScore());

            db.insert(new Turn(1, 5));
            db.insert(new TurnPlayer1(1,5, _playerName1, 10, 1, "play"));

            if(uploadedLast && equalScore) {

                var result = db.select(TurnPlayer2.class, clauses).get(0);
                int score = result.getScore();
                int bonus = result.getBonus();

                db.update(new TurnPlayer2(_gameId, _turn, _playerName2, score, bonus + 5, "play"));
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        //db.insert(new InsertedKeys())
        _turn++;
        _board.clearPlacedCoords();
    }


}

