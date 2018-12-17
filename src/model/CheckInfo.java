package model;

public class CheckInfo {

    private Points _score;

    private Tile[] _tiles;

    private Vector2[] _coordinates;

    public CheckInfo(Points score, Tile[] tiles, Vector2[] coordinates) {
        this._score = score;
        this._tiles = tiles;
        this._coordinates = coordinates;
    }

    public Points getPoints() {
        return this._score;
    }

    public Tile[] getTiles() {
        return this._tiles;
    }

    public Vector2[] getCoordinates() {
        return this._coordinates;
    }
}
