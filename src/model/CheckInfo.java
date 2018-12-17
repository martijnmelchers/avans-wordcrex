package model;

public class CheckInfo {

    private Points _score;



    private Vector2[] _coordinates;

    public CheckInfo(Points score, Vector2[] coordinates) {
        _score = score;

        _coordinates = coordinates;
    }

    public Points getPoints() {
        return _score;
    }



    public Vector2[] getCoordinates() {
        return _coordinates;
    }
}
