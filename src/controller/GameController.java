package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.*;
import model.tables.Account;
import model.tables.Game;
import model.tables.HandLetter;
import view.BoardView.BoardView;

import java.util.ArrayList;

public class GameController extends Controller{

    private BoardView _boardView;
    private GameModel _gameModel;

    public GameController() {
    }

    public void startGame(Game game)
    {
        _gameModel = new GameModel(game); //TODO: The game will be created by the match overview so gameId parameter is for testing. Remove after branch merged
    }

    public void startGame(){
        startGame(GameSession.getGame());
    }


    public Tile[][] getTiles() { return _gameModel.getTiles(); }

    public int getCurrentTurn() { return _gameModel.turn(); }

    public HandLetter[] getDock(){ return _gameModel.getDock(); }

    public Letter getLetterType(HandLetter letter){ return _gameModel.getLetterType(letter); }

    public int[] getScore() { return new int[] {_gameModel.getPlayerScore1(), _gameModel.getPlayerScore2() }; }

    public String[] getPlayerNames() {return new String[] {_gameModel.getPlayerName1(), _gameModel.getPlayername2() }; }

    public void placeTile(int x,int y, String letter, int letterId) {
        _gameModel.placeTile(new Vector2(x, y), letter, letterId);
        _boardView = getViewCasted();
        _boardView.update(false);
    }

    public void resetTile(int x, int y)
    {
        _gameModel.removeTile(new Vector2(x, y));
        checkScore();
    }

    public ArrayList<Tile> resetTiles()
    {
        return _gameModel.removeTiles();
    }

    public boolean tileEmpty(int x,int y)
    {
        return  _gameModel.tileIsEmpty(new Vector2(x, y));
    }

    private Task nextTurn()
    {
        return new Task() {
            @Override
            protected Object call() throws Exception
            {
                updateView(true);
                Platform.runLater(new Runnable(){
                    @Override
                    public void run()
                    {
                        _boardView.stopLoadingScreen();
                        if (_gameModel.checkGameDone())
                        {
                            _boardView.gameDone();
                        }
                    }
                });

                return null;
            }
        };
    }

    public void submitTurn()
    {
        submitTurn(false);
    }

    public void submitTurn(boolean pass){
        CheckInfo info = _gameModel.checkBoard();

        if(info == null &&!pass) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Foute zet");
            alert.showAndWait();
            return;
        }

        if (info == null && pass) {
            info = new CheckInfo(new Points(0, 0),  null);
        }

        _boardView.startLoadingScreen("Wachten op andere speler.");
        _gameModel.submitTurn(info, nextTurn(),onSurrender());

    }

    public void passTurn()
    {
        _boardView = getViewCasted();
        submitTurn(true);
    }

    public void showTurn(int turn){
        _gameModel.setTurn(turn);
        _boardView = getViewCasted();
        updateView(true);
        _gameModel.updateScore(turn);
    }

    public void surrender()
    {
        _boardView = getViewCasted();
        _gameModel.surrender();
        _boardView.gameDone();
    }

    public void checkScore(){
       CheckInfo info = _gameModel.checkBoard();
       String total = (info == null) ? "0p" : info.getPoints().total() + "p";

        _boardView.updateLocalScore(total);
    }

    public void checkIfTurnPlayed()
    {
       if( _gameModel.checkIfTurnPlayed())
       {
           _boardView = getViewCasted();
           _boardView.startLoadingScreen("Wachten op andere speler.");
           _gameModel.alreadyPlayed(nextTurn(), onSurrender() );
       }
    }

    private Task onSurrender()
    {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                _boardView.gameDone();
                return null;
            }
        };
    }

    private void updateView(boolean updateDock)
    {
        _boardView.update(updateDock);
        _gameModel.updateScore();
        _boardView.updateScore();
        _boardView.updateTilesLeft();
        checkScore();
    }

    public String getNotUsedTiles()
    {
        return _gameModel.getNotUsedTiles(getCurrentTurn());
    }

    // get older turns
    public String getNotUsedTiles(int turnId)
    {
        return _gameModel.getNotUsedTiles(turnId);
    }

    public void getOldDock(int turn)
    {
        _gameModel.getOldDock(turn);
    }

    public String getGameWinner()
    {
        return _gameModel.getGameWinner();
    }

    public String getGameWinnerScore()
    {
        return _gameModel.getGameWinnerScore();
    }

}
