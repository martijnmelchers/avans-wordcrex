package controller;


import model.GameSession;
import model.moderator.GameWord;
import model.moderator.ModeratorDictionary;

public class ModeratorController extends Controller {

    private ModeratorDictionary moderatorDictionary;

    private String letterSet = "NL";

    public ModeratorController() {
        this.moderatorDictionary = new ModeratorDictionary();

    }

    public String[] getSuggestedWords() {
        this.moderatorDictionary.refreshPending();
        return this.moderatorDictionary.getWords().stream().map(GameWord::getWord).toArray(String[]::new);
    }

    public String[] getDeclinedWords() {
        this.moderatorDictionary.refreshDeclined();
        return this.moderatorDictionary.getWords().stream().map(GameWord::getWord).toArray(String[]::new);
    }

    public String[] getAcceptedWords() {
        this.moderatorDictionary.refreshAccepted();
        return this.moderatorDictionary.getWords().stream().map(GameWord::getWord).toArray(String[]::new);
    }

    public void rejectSuggestedWords(String[] words) {
        this.moderatorDictionary.declineWords(words, GameSession.getUsername(), this.letterSet);
    }

    public void acceptSuggestedWords(String[] words) {
        this.moderatorDictionary.acceptWords(words, GameSession.getUsername(), this.letterSet);
    }
}
