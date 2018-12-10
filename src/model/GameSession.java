package model;

import model.tables.Account;
import model.tables.Game;
import model.tables.Role;

public final class GameSession {
    private static Account account;
    private static Role role;
    private static Game game;

    public static void setSession(Account user) { GameSession.account = user; }

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

    public static  Game getGame() {
        return GameSession.game;
    }

    public static void setGame(Game game) {
        GameSession.game = game;
    }

    public static void exitGame() {
        GameSession.game = null;
    }

    public static void setRole(Role role) { GameSession.role = role; }

    public static Role getRole() { return GameSession.role; }
}
