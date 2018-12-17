package controller;

import model.GameSession;
import model.moderator.ModeratorDictionary;

public class PlayerWordRequestController extends Controller {

    private ModeratorDictionary _moderatorDictionary;

    public  PlayerWordRequestController()
    {
        _moderatorDictionary = new ModeratorDictionary();
    }

    public void submitWord(String[] words)
    {
        _moderatorDictionary.setWords(words, GameSession.getUsername(), "NL", "pending");
    }
}
