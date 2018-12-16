package controller;

import model.GameSession;
import model.MatchFinderModel;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.helper.Log;
import model.tables.Game;

import java.util.Arrays;
import java.util.List;

public class ObserverController extends Controller{

    MatchFinderModel model;
    public ObserverController(){
        model = new MatchFinderModel();
    }

    public void WatchGame(int gameID){

        try {
            GameSession.setGame(DocumentSession.getDatabase().select(Game.class,
                    Arrays.asList( new Clause(new TableAlias("game", -1), "game_id", CompareMethod.EQUAL, gameID))).get(0));
            getController(GameController.class).startGame();
        } catch (Exception e) {
            Log.error(e);
        }
    }
    public String[] getGames(String name){
        return model.searchPlayers(name).stream().map(x -> x.getGameID() + " " +
                x.getPlayer1().getUsername() + " VS " +
                x.getPlayer2().getUsername() ).toArray(String[]::new);
    }

}
