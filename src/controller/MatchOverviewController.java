package controller;

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
        return model.getGames();
    }

    public boolean currentTurnHasAction(Game game) {
        return model.currentTurnHasAction(game);
    }

    public boolean currentTurnPlayer2HasAction(Game game) {
        return model.currentTurnPlayer2HasAction(game);
    }
}
