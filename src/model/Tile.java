package model;

import javafx.scene.paint.Color;

public class Tile {

    public enum TileType { letterTimesFour, wordTimesThree, letterTimesTwo, letterTimesSix, wordTimesFour, standard  }

    private TileType _tileType;

    private int _tileState;

    private Color _tileColor;

    public TileType getType(){ return _tileType; }
    public Color getColor() {return _tileColor; }


    public Tile(){}

    public Tile(TileType tileType, Color color)
    {
        _tileType = tileType;
        _tileColor = color;
    }

    private void getLetter(){}

}
