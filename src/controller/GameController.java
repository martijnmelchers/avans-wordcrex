package controller;

import model.GameModel;
import model.Tile;

public class GameController extends Controller{

    private GameModel _gameModel;

    public GameController()
    {
        _gameModel = new GameModel();
    }

    public Tile[][] getTiles() { return _gameModel.getTiles(); }

}
