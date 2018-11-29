package model.moderator;

import model.database.annotations.Table;
import model.database.services.Database;

import java.util.ArrayList;
import java.util.List;


public class GameDictionary {
    private List<GameWord> words;
    private Database dB;

    public GameDictionary(Database dB){
        this.dB = dB;
        refresh();
    }
    public void refresh(){

        try {
            words = dB.select(GameWord.class, new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
