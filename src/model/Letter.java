package model;

public class Letter {

    //Is een string omdat het soms meer dan 1 char heeft
    private String _letter;

    private int _points;

    private int _letterId;

    public Letter(String letter) {
        _letter = letter;
    }

    public String getLetter() {
        return _letter;
    }

    public void setPoints(int points){
        _points = points;
    }

    public void setLetter(String letter) {
        _letter = letter;
    }

    public void setId(int letterId) { _letterId = letterId; }

    public int getid() { return _letterId; }

}
