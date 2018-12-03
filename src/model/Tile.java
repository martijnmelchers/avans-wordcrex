package model;

import javafx.scene.paint.Color;

public class Tile {

    private TileType _tileType;
    private Color _tileColor;

    private Letter _letter;

    public TileType getType() {
        return _tileType;
    }

    public Color getColor() { return _tileColor; }

    public void setColor(Color color){ _tileColor = color; }

    public Letter getLetterType() { return _letter; }

    public void setLetter( String letter){ _letter.setLetter(letter); }

    public Tile() {}

    public Tile(TileType tileType, Color color, String letter) {
        _tileType = tileType;
        _tileColor = color;
        _letter = new Letter(letter);
    }
}
