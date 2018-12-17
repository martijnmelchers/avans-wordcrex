package model;

public class Points {

    private int _score;
    private int _bonus;

    public Points(int score, int bonus) {
        this._score = score;
        this._bonus = bonus;

    }

    public int score() {
        return this._score;
    }

    public int bonus() {
        return this._bonus;
    }

    public int total() {
        return this._score + this._bonus;
    }
}
