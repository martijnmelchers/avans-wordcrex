package controller;

import model.GameSession;
import model.moderator.GameWord;
import model.moderator.ModeratorDictionary;

import java.util.List;

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

    public List<GameWord> getByPlayerRequestedWords(String username) {
        return _moderatorDictionary.getByPlayerRequestedWords(username);
    }
}
