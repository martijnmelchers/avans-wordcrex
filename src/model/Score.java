package model;

public class Score {

    private int _score;
    private int _bonus;

    public int getScore() { return _score; }
    public int getBonus() { return _bonus; }
    public int getTotal() { return _score + _bonus; }

    public Score(int score, int bonus){
        _score = score;
        _bonus = bonus;

    }
}
