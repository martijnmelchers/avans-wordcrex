package model;

public class GameModel {

    private Board _board;


    public GameModel()
    {
        _board = new Board();
    }

    public Tile[][] getTiles(){ return _board.getTiles(); }

    public boolean tileIsEmpty(Vector2 vector2) { return _board.isEmpty(vector2); }

    public void placeTile(Vector2 vector2, String letter){ _board.place(vector2, letter); }

    //Geeft de tile terug die er lag voordat het verwijderd word
    public Tile removeTile(Vector2 vector2) { return _board.remove(vector2); }

    public void checkBoard(Vector2 vector2) { _board.check(vector2); }

    public void submitTurn(CheckInfo info) { _board.submit(info); }

}

