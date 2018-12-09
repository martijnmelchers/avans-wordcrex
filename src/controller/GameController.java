package controller;

import model.*;
import view.BoardView;

public class GameController extends Controller{

    private BoardView boardView;
    private GameModel _gameModel;

    public GameController() {

        DocumentSession.setPlayerUsername("stan");//TODO: Authentication branch will set the player this is for testing purposes. Remove after branch merged
        _gameModel = new GameModel(0); //TODO: The game will be created by the match overview so gameId parameter is for testing. Remove after branch merged
    }

    public Tile[][] getTiles() { return _gameModel.getTiles(); }

    public Letter[] getDock(){return _gameModel.getDock();}

    public void placeTile(int x,int y, String letter, int letterId) {
        _gameModel.placeTile(new Vector2(x, y), letter, letterId);
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

    public void submitTurn(){
        CheckInfo info = _gameModel.checkBoard();

        if(info == null) return;

        _gameModel.submitTurn(info);
    }

}
