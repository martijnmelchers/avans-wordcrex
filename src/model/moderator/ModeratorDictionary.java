package model.moderator;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.tables.Game;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ModeratorDictionary {
    private List<GameWord> words;
    private Database dB;
    private List<Clause> pending;
    private List<Clause> accepted;
    private List<Clause> declined;


    public ModeratorDictionary(Database dB) {

        pending = new ArrayList<Clause>();
        accepted = new ArrayList<Clause>();
        declined = new ArrayList<Clause>();
        pending.add(new Clause(new TableAlias("dictionary",-1), "state", CompareMethod.EQUAL, "pending"));
        accepted.add(new Clause(new TableAlias("dictionary",-1), "state", CompareMethod.EQUAL, "accepted"));
        declined.add(new Clause(new TableAlias("dictionary",-1), "state", CompareMethod.EQUAL, "denied"));

        this.dB = dB;


    }

    public void refreshPending() {

        try {
            words  =  dB.select(GameWord.class, pending);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshAccepted() {

        try {
            words = dB.select(GameWord.class, accepted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void refreshDeclined() {

        try {
            words = dB.select(GameWord.class, declined);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptWords(String[] words, String userName, String letterset) {
        setWords(words,userName,letterset,"accepted");


    }

    public void setWords(String[] words, String userName, String letterSet,String state) {
        List<GameWord> acceptedWords = new ArrayList<GameWord>();

        for (String temp : words) {
            acceptedWords.add(
                    new GameWord(temp, letterSet, state, userName)
            );
        }

        try {
            dB.update(acceptedWords);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void declineWords(String[] words, String userName, String letterSet){
        setWords(words,userName,letterSet,"denied");
    }

    public List<GameWord> getWords() {
        return words;
    }
    public void close(){
        try {
            dB.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getSize() {
        return words.size();
    }
}
