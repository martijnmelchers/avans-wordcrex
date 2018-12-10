package model;

import model.tables.Account;
import model.tables.Game;

public final class GameSession {
    private Account account;
    private Game game;

    public void setSession(Account user) {
        this.account = user;
    }

    public void endSession() {
        this.account = null;
    }

    public boolean loggedIn() {
        return this.account != null;
    }

    public String getUsername() {
        return this.account.getUsername();
    }

    public boolean inGame() {
        return this.game != null;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void exitGame() {
        this.game = null;
    }
}
