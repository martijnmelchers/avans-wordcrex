package model;

public class CheckInfo {

    private String[] _words;

    private Points _score;

    private Tile[] _tiles;

    public String[] getWords(){ return _words; }

    public Points getPoints(){ return _score; }

    public Tile[] getTiles(){ return getTiles(); }

    public CheckInfo(String[] words, Points score, Tile[] tiles){
        _words = words;
        _score = score;
        _tiles = tiles;
    }
}
