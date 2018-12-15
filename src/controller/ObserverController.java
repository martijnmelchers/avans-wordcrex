package controller;

import model.MatchFinderModel;

public class ObserverController extends Controller{

    MatchFinderModel model;
    public ObserverController(){
        model = new MatchFinderModel();
    }

    public void WatchGame(int gameID){

    }
    public String[] getGames(String name){
        return null;// model.searchPlayers(name);
    }

}
