package controller;

import model.GameSession;
import model.MatchOverviewModel;
import model.helper.Log;
import model.tables.Game;

import java.util.ArrayList;
import java.util.List;

public class MatchOverviewController extends Controller {

    private MatchOverviewModel model;

    public MatchOverviewController() {
        model = new MatchOverviewModel();
    }

    public List<Game> getGames() {
        System.out.println(GameSession.getUsername());
        return model.getCurrentPlayerGames(GameSession.getUsername());
    }

    public void start() {
        try {
            this.getController(GameController.class).startGame(GameSession.getGame());
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public ArrayList<Game> getAllGames() {
        return model.getAllGames();
    }

    public boolean currentTurnHasAction(Game game) {
        return model.currentTurnHasAction(game);
    }

    public boolean currentTurnPlayer2HasAction(Game game) {
        return model.currentTurnPlayer2HasAction(game);
    }

    public ArrayList<Game> searchForAllGamesAsObserver(String currentGamesToSearch) {
        return model.searchForGamesAsObserver(currentGamesToSearch);
    }

    public ArrayList<String> getPlayerRoles() {
        return model.getPlayerRoles();
    }

    public ArrayList<Game> searchForAllGamesAsPlayer(String currentGamesToSearch) {
        return model.searchForGamesAsPlayer(currentGamesToSearch);
    }


    public boolean isMyTurn(Game game) {
        return this.model.isMyTurn(game);
    }

    public MatchOverviewModel.GameScore getPlayerScores(Game game) {
        return model.getPlayerScores(game);
    }
}
