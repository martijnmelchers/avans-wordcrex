package controller;

import model.GameModel;
import model.Tile;

public class BoardController {

    private GameModel _gameModel;

    public BoardController()
    {
        _gameModel = new GameModel();
    }

    public Tile[][] getTiles() { return _gameModel.getTiles(); }

}
