package model;

public class Letter {

    //Is een string omdat het soms meer dan 1 char heeft
    private String _letter;
    private int _points;

    public Letter(String letter) {
        _letter = letter;
    }

    public String getLetter() {
        return _letter;
    }

    public int getPoints(){
        return _points;
    }

    public void setPoints(int points){
        _points = points;
    }

    public void setLetter(String letter) {
        _letter = letter;
    }

}
