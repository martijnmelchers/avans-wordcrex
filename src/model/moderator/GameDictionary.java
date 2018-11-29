package model.moderator;

import model.database.classes.Clause;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;

import java.util.ArrayList;
import java.util.List;


public class GameDictionary {
    private List<GameWord> words;
    private Database dB;
    private List<Clause> pending;
    private List<Clause> accepted;
    private List<Clause> declined;



    public GameDictionary(Database dB){
        pending = new ArrayList<Clause>();
        accepted = new ArrayList<Clause>();
        declined = new ArrayList<Clause>();
        pending.add(new Clause("dictionary", "state",CompareMethod.EQUAL,"pending"));
        accepted.add(new Clause("dictionary", "state",CompareMethod.EQUAL,"accepted"));
        declined.add(new Clause("dictionary", "state",CompareMethod.EQUAL,"denied"));

        this.dB = dB;


    }
    public void refreshPending(){

        try {
            words = dB.select(GameWord.class,pending);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshAccepted(){

        try {
            words = dB.select(GameWord.class,accepted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshDeclined(){

        try {
            words = dB.select(GameWord.class,declined);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void acceptWords(String[] words,String userName,String letterset) {
        List<GameWord> acceptedWords = new ArrayList<GameWord>();

        for (String temp : words) {
            acceptedWords.add(
                    new GameWord() {
                        {
                            this.word = temp;
                            this.letterset_code = letterset;
                            this.state = "accepted";
                            this.username = userName;
                        }
                    }
            );
        }

        try {
            dB.insert(acceptedWords);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public List<GameWord> getWords() {
        return words;
    }
    public int getSize(){
        return  words.size();
    }
}