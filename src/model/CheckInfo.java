package model;

public class CheckInfo {

    private Points _score;

    private Tile[] _tiles;

    private Vector2[] _coordinates;

    public Points getPoints(){ return _score; }

    public Tile[] getTiles(){ return _tiles; }

    public Vector2[] getCoordinates() {return _coordinates; }

    public CheckInfo(Points score, Tile[] tiles, Vector2[] coordinates){
        _score = score;
        _tiles = tiles;
        _coordinates = coordinates;
    }
}
