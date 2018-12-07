package controller;

import model.*;
import view.BoardView;

public class GameController extends Controller{

    private BoardView boardView;
    private GameModel _gameModel;

    public GameController() {
        _gameModel = new GameModel();
        submitTurn();
    }

    public Tile[][] getTiles() { return _gameModel.getTiles(); }

    public Letter[] getDock(){return _gameModel.getDock();}

    public void placeTile(int x,int y,String letter) {
        _gameModel.placeTile(new Vector2(x,y ),letter);
        boardView = getViewCasted();
        boardView.update();
    }

    public void resetTile(int x, int y)
    {
        _gameModel.removeTile(new Vector2(x, y));
    }

    public boolean tileEmpty(int x,int y)
    {
        return  _gameModel.tileIsEmpty(new Vector2(x, y));
    }

    //TODO check of je speler 1 of speler 2 bent
    public void submitTurn(){
        CheckInfo info = _gameModel.checkBoard(new Vector2(0,0));
        //_gameModel.submitTurn(info);
    }

}
