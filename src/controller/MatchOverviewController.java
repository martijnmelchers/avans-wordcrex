package controller;

import model.GameSession;
import model.MatchOverviewModel;
import model.tables.Game;

import java.util.ArrayList;

public class MatchOverviewController extends Controller
{

    private MatchOverviewModel model;

    public MatchOverviewController()
    {
        model = new MatchOverviewModel();
    }

    public ArrayList<Game> getGames()
    {
        System.out.println(GameSession.getUsername());
        return model.getCurrentPlayerGames(GameSession.getUsername());
    }

    public ArrayList<Game> getAllGames()
    {
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

    public MatchOverviewModel.GameScore getPlayerScores(Game game)
    {
        return model.getPlayerScores(game);
    }
}
