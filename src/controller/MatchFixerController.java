package controller;

import model.matchfixer.Matchfixer;

public class MatchFixerController extends Controller {
    private Matchfixer matchfixer;

    public MatchFixerController() {
        this.matchfixer = new Matchfixer();
    }
    public String[] SearchPlayers(String name){
        return matchfixer.searchPlayers(name).stream().map(x -> x.getUsername()).toArray(String[]::new);
    }
}
