package model;

public class CheckInfo {

    private String[] _words;

    private Score _score;

    private Tile[] _tiles;

    public String[] getWords(){ return _words; }

    public Score getScore(){ return _score; }

    public Tile[] getTiles(){ return getTiles(); }

    public CheckInfo(String[] words, Score score, Tile[] tiles){
        _words = words;
        _score = score;
        _score = score;
        _tiles = tiles;
    }

}
