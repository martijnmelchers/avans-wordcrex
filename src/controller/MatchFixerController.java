package controller;

import model.matchfixer.Matchfixer;
import model.matchfixer.Player;

public class MatchFixerController extends Controller {
    private Matchfixer matchfixer;

    public MatchFixerController() {
        this.matchfixer = new Matchfixer();
    }
    public String[] SearchPlayers(String name){
        return matchfixer.searchPlayers(name).stream().map(Player::getUsername).toArray(String[]::new);
    }
    public void RequestGame(String player){
        matchfixer.invitePlayer(player);
    }
}
