package controller;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.*;
import model.database.DocumentSession;
import model.tables.Account;
import model.tables.Game;
import model.tables.HandLetter;
import view.BoardView.*;

public class GameController extends Controller{

    private BoardView boardView;
    private GameModel _gameModel;

    public GameController() {
        _gameModel = new GameModel(new Game(504, "playing", "NL", "jagermeester", "Lidewij", "accepted", null)); //TODO: The game will be created by the match overview so gameId parameter is for testing. Remove after branch merged
    }

    public Tile[][] getTiles() { return _gameModel.getTiles(); }

    public HandLetter[] getDock(){ return _gameModel.getDock(); }

    public int[] getScore() { return new int[] {_gameModel.getPlayerScore1(), _gameModel.getPlayerScore2() }; }

    public String[] getPlayerNames() {return new String[] {_gameModel.getPlayerName1(), _gameModel.getPlayername2() }; }

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

        if(info == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Foute zet");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    GameSession.setSession(new Account("jagermeester","rrr"));
                }
            });
            return;
        }

        boardView.startLoadingScreen();
        Task nextTurn = new Task() {
            @Override
            protected Object call() throws Exception
            {
                updateView();
                boardView.stopLoadingScreen();
                return null;
            }
        };
        _gameModel.submitTurn(info,nextTurn);
    }

    private void updateView()
    {
        boardView.update();
    }
}