package model;

public class Points {

    private int _score;
    private int _bonus;

    public Points(int score, int bonus) {
        _score = score;
        _bonus = bonus;

    }

    public int score() {
        return _score;
    }

    public int bonus() {
        return _bonus;
    }

    public int total() {
        return _score + _bonus;
    }

    public  void add(Points point){
        _score += point.score();
        _bonus +=point.bonus();
    }
}
