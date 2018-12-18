package model.moderator;

import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.helper.Log;

import java.util.ArrayList;
import java.util.List;


public class ModeratorDictionary {
    private List<GameWord> words;
    private Database _db;
    private List<Clause> pending;
    private List<Clause> accepted;
    private List<Clause> declined;


    public ModeratorDictionary() {

        this.pending = new ArrayList<>();
        this.accepted = new ArrayList<>();
        this.declined = new ArrayList<>();
        this.pending.add(new Clause(new TableAlias("dictionary", -1), "state", CompareMethod.EQUAL, "pending"));
        this.accepted.add(new Clause(new TableAlias("dictionary", -1), "state", CompareMethod.EQUAL, "accepted"));
        this.declined.add(new Clause(new TableAlias("dictionary", -1), "state", CompareMethod.EQUAL, "denied"));

        try {
            this._db = DocumentSession.getDatabase();
        } catch (Exception e) {
            Log.error(e);
        }


    }

    public void refreshPending() {

        try {
            this.words = this._db.select(GameWord.class, this.pending);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void refreshAccepted() {

        try {
            this.words = this._db.select(GameWord.class, this.accepted);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void refreshDeclined() {

        try {
            this.words = this._db.select(GameWord.class, this.declined);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public List<GameWord> getByPlayerRequestedWords(String username) {
        ArrayList<Clause> clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("dictionary", -1), "username", CompareMethod.EQUAL, username));

        List<GameWord> requestedWords = new ArrayList<>();

        try {
            requestedWords = this._db.select(GameWord.class, clauses);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  requestedWords;
    }


    public void acceptWords(String[] words, String userName, String letterSet) {
        this.setWords(words, userName, letterSet, "accepted");


    }

    public void setWords(String[] words, String userName, String letterSet, String state) {
        List<GameWord> acceptedWords = new ArrayList<>();

        for (String temp : words) {
            if(temp.length() > 15)
                continue;

            acceptedWords.add(new GameWord(temp, letterSet, state, userName));
        }

        try {
            this._db.insert(acceptedWords);
        } catch (Exception e) {
            try {
                this._db.update(acceptedWords);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Log.error(e);
        }
    }



    public void declineWords(String[] words, String userName, String letterSet) {
        this.setWords(words, userName, letterSet, "denied");
    }

    public List<GameWord> getWords() {
        return this.words;
    }

    public int getSize() {
        return this.words.size();
    }
}
