package controller;

import model.*;

public class GameController extends Controller{

    private GameModel _gameModel;

    public GameController()
    {
        _gameModel = new GameModel();
        submitTurn();
    }

    public Tile[][] getTiles() { return _gameModel.getTiles(); }

    public Letter[] getDock(){return _gameModel.getDock();}

    public void submitTurn(){
       CheckInfo info = _gameModel.checkBoard(new Vector2(0,0));
       _gameModel.submitTurn(info);
    }

}
