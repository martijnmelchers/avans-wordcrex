package model.moderator;


import model.database.services.Database;

public class Moderator {

    private GameDictionary gameDictionary;
    public Moderator(Database dB){
        this.gameDictionary = new GameDictionary(dB);
    }

    public String[] getSuggestedWords(){
        return new String[]{"kek","kek2","kek3","keki",};
    }
    public void rejectSuggestedWords(String[] words){

    }
    public void acceptSuggestedWords(String[] words){

    }
}
