package model;

import javafx.scene.paint.Color;

public class Tile {

    private TileType _tileType;
    private TileState _tileState;

    private Color _tileColor;

    private Letter _letter;

    public Tile() {}
    //LAden van de tiles
    public Tile(TileType tileType, Color color) {
        _tileState = TileState.LOCKED;
        _tileType = tileType;
        _tileColor = color;
        _letter = new Letter("");
    }
    //Laden van de letters
    public Tile(String letter){
        _tileState = TileState.UNLOCKED;
        _tileType = TileType.STANDARD;
        _tileColor = Color.WHITE;
        _letter = new Letter(letter);
    }

    public TileType getType() {
        return _tileType;
    }

    public TileState getState() { return _tileState; }

    public Color getColor() { return _tileColor; }

    public void setColor(Color color){ _tileColor = color; }

    public Letter getLetterType() { return _letter; }

    public boolean isEmpty(){ return _letter.getLetter().equals(""); }

    public void replace( String letter, int points){
        _letter.setLetter(letter);
        _letter.setPoints(points);
    }
}
