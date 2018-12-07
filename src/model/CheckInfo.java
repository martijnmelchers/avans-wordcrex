package model;

public class CheckInfo {

    private String[] _words;

    private Points _score;

    private Tile[] _tiles;

    private Vector2[] _coordinates;

    public String[] getWords(){ return _words; }

    public Points getPoints(){ return _score; }

    public Tile[] getTiles(){ return getTiles(); }

    public Vector2[] getCoordinates() {return _coordinates; }

    public CheckInfo(String[] words, Points score, Tile[] tiles, Vector2[] coordinates){
        _words = words;
        _score = score;
        _tiles = tiles;
        _coordinates = coordinates;
    }
}
