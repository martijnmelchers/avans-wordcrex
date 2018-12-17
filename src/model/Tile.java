package model;

import javafx.scene.paint.Color;

public class Tile {

    private TileType _tileType;
    private TileState _tileState;

    private Color _tileColor;

    private Letter _letter;

    public Tile() {
    }

    //Laden van de tiles
    public Tile(TileType tileType, Color color) {
        this._tileState = TileState.LOCKED;
        this._tileType = tileType;
        this._tileColor = color;
        this._letter = new Letter("");
    }

    //Laden van de letters
    public Tile(String letter) {
        this._tileState = TileState.UNLOCKED;
        this._tileType = TileType.STANDARD;
        this._tileColor = Color.WHITE;
        this._letter = new Letter(letter);
    }

    public TileType getType() {
        return this._tileType;
    }

    public TileState getState() {
        return this._tileState;
    }

    public void setState(TileState tileState) {
        this._tileState = tileState;
    }

    public Color getColor() {
        return this._tileColor;
    }

    public void setColor(Color color) {
        this._tileColor = color;
    }

    public Letter getLetterType() {
        return this._letter;
    }

    public boolean isEmpty() {
        System.out.println(this._letter.getLetter());
        return this._letter.getLetter().equals("");
    }

    public void replace(String letter, int points, int letterId) {
        this._letter.setLetter(letter);
        this._letter.setPoints(points);
        this._letter.setId(letterId);
    }

    public void replace(String letter, int points, int letterId, Color color) {
        this._letter.setLetter(letter);
        this._letter.setPoints(points);
        this._letter.setId(letterId);
        this._tileColor = color;
    }
}
