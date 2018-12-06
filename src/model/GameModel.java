package model;

import model.database.services.Database;
import model.tables.Turn;
import model.tables.TurnPlayer1;

import java.util.ArrayList;

public class GameModel {

    private ArrayList<String> _allowedWords = new ArrayList<>();

    private int _currentTurn;
    private Board _board;


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
        //TODO check of hij alseerste plaatst
        //TODO als hij als laatste plaats kijk of de text gelijk is en geef 5 punten aan de tegen partij
        Database db = DocumentSession.getDatabase();
        try{
            db.insert(new Turn(1, 5));
            db.insert(new TurnPlayer1(1,5, "Mega Neger #1741", 10, 1, "play"));
        }catch (Exception e){
            e.printStackTrace();
        }
        //db.insert(new InsertedKeys())
        _board.clearPlacedCoords();
    }


}

