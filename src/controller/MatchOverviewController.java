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
        this.model = new MatchOverviewModel();
    }

    public List<Game> getGames() {

        return this.model.getCurrentPlayerGames(GameSession.getUsername());
    }

    public void start() {
        try {
            this.getController(GameController.class).startGame(GameSession.getGame());
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public List<Game> getAllGames() {
        return this.model.getAllGames();
    }

    public boolean currentTurnHasAction(Game game) {
        return this.model.currentTurnHasAction(game);
    }

    public boolean currentTurnPlayer2HasAction(Game game) {
        return this.model.currentTurnPlayer2HasAction(game);
    }

    public ArrayList<Game> searchForAllGamesAsObserver(String currentGamesToSearch) {
        return this.model.searchForGamesAsObserver(currentGamesToSearch);
    }

    public ArrayList<String> getPlayerRoles() {
        return this.model.getPlayerRoles();
    }

    public ArrayList<Game> searchForAllGamesAsPlayer(String currentGamesToSearch) {
        return this.model.searchForGamesAsPlayer(currentGamesToSearch);
    }

    public boolean isMyTurn(Game game) throws NullPointerException {
        return MatchOverviewModel.isMyTurn(game);
    }

    public MatchOverviewModel.GameScore getPlayerScores(Game game) {
        return this.model.getPlayerScores(game);
    }

    public void surrender(Game game){
        this.model.surrenderGame(game);
    }

    public void acceptInvite(Game game){
        this.model.acceptInvite(game);
    }

    public void declineInvite(Game game){
        this.model.declineInvite(game);
    }

    public void endSession(){
        GameSession.endSession();
    }
}
