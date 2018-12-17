package controller;

import model.match.MatchFixer;
import model.tables.AccountInfo;

public class MatchFixerController extends Controller {
    private MatchFixer _matchFixer;

    public MatchFixerController() {
        this._matchFixer = new MatchFixer();
    }

    public String[] searchPlayers(String name) {
        return this._matchFixer.searchPlayers(name).stream().map(AccountInfo::getUsername).toArray(String[]::new);
    }

    public void RequestGame(String player) {
        this._matchFixer.invitePlayer(player);
    }

    public void fetchCache() {
        this._matchFixer.refreshCache();
    }
}
