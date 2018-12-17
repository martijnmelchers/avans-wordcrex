package model;

public class Letter {

    //Is een string omdat het soms meer dan 1 char heeft
    private String _letter;

    private int _points;

    private int _letterId;

    public Letter(String letter) {
        this._letter = letter;
    }

    public String getLetter() {
        return this._letter;
    }

    public void setLetter(String letter) {
        this._letter = letter;
    }

    public void setPoints(int points) {
        this._points = points;
    }

    public void setId(int letterId) {
        this._letterId = letterId;
    }

    public int getid() {
        return this._letterId;
    }

}
