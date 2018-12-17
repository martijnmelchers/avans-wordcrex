package model;

import model.tables.Account;
import model.tables.Game;
import model.tables.Role;

import java.util.ArrayList;

public final class GameSession {
    private static Account account;
    private static ArrayList<Role> roles;
    private static Game game;

    public static void setSession(Account user) {
        GameSession.account = user;
    }

    public static void endSession() {
        GameSession.account = null;
    }

    public static boolean loggedIn() {
        return GameSession.account != null;
    }

    public static String getUsername() {
        return GameSession.account.getUsername();
    }

    public static boolean inGame() {
        return GameSession.game != null;
    }

    public static Game getGame() {
        return GameSession.game;
    }

    public static void setGame(Game game) {
        GameSession.game = game;
    }

    public static void exitGame() {
        GameSession.game = null;
    }

    public static ArrayList<Role> getRoles() {
        return GameSession.roles;
    }

    public static boolean hasRole(String role){
        for (var roleObj: GameSession.roles){
            if(roleObj.getRole().equals(role)){
                return true;
            }
        }
        return false;
    }


    public static Account getAccount(){
        return GameSession.account;
    }
    public static void setRoles(ArrayList<Role> roles) {
        GameSession.roles = roles;
    }
}
