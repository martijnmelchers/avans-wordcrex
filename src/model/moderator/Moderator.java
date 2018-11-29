package model.moderator;


import model.database.services.Database;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Moderator {

    private GameDictionary gameDictionary;
    private String username = "bea";
    private String letterset = "NL";
    public Moderator(Database dB){
        this.gameDictionary = new GameDictionary(dB);
    }

    public String[] getSuggestedWords(){
        gameDictionary.refreshPending();
        return gameDictionary.getWords().stream().map(s -> s.getWord()).toArray(String[]::new);
    }
    public String[] getDeclinedWords(){
        gameDictionary.refreshDeclined();
        return gameDictionary.getWords().stream().map(s -> s.getWord()).toArray(String[]::new);
    }
    public String[] getAcceptedWords(){
        gameDictionary.refreshAccepted();
        return gameDictionary.getWords().stream().map(s -> s.getWord()).toArray(String[]::new);
    }
    public void rejectSuggestedWords(String[] words){

    }
    public void acceptSuggestedWords(String[] words){
        gameDictionary.acceptWords(words,username,letterset);
    }
}
