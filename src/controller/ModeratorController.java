package controller;


import controller.Controller;
import model.DocumentSession;
import model.database.services.Database;
import model.moderator.ModeratorDictionary;

import java.sql.SQLException;

public class ModeratorController extends Controller {

    private ModeratorDictionary moderatorDictionary;
    private String username;
    private String letterset = "NL";
    public ModeratorController(){
        try {
            this.moderatorDictionary = new ModeratorDictionary(DocumentSession.getDatabase());
        } catch (SQLException e) {
            Log.error(e);

        }
        username = DocumentSession.getPlayerUsername();

    }

    public String[] getSuggestedWords(){
        moderatorDictionary.refreshPending();
        return moderatorDictionary.getWords().stream().map(s -> s.getWord()).toArray(String[]::new);
    }
    public String[] getDeclinedWords(){
        moderatorDictionary.refreshDeclined();
        return moderatorDictionary.getWords().stream().map(s -> s.getWord()).toArray(String[]::new);
    }
    public String[] getAcceptedWords(){
        moderatorDictionary.refreshAccepted();
        return moderatorDictionary.getWords().stream().map(s -> s.getWord()).toArray(String[]::new);
    }
    public void rejectSuggestedWords(String[] words){
        moderatorDictionary.declineWords(words,username,letterset);
    }
    public void acceptSuggestedWords(String[] words){
        moderatorDictionary.acceptWords(words,username,letterset);
    }
    public void close(){
        moderatorDictionary.close();
    }
}
