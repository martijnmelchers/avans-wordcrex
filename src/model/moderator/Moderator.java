package model.moderator;


import model.database.services.Database;

public class Moderator {

    private ModeratorDictionary moderatorDictionary;
    private String username = "Daan";
    private String letterset = "NL";
    public Moderator(Database dB){
        this.moderatorDictionary = new ModeratorDictionary(dB);
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

    }
    public void acceptSuggestedWords(String[] words){
        moderatorDictionary.acceptWords(words,username,letterset);
    }
    public void close(){
        moderatorDictionary.
    }
}
