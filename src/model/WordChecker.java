package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.tables.WordDictionary;

import java.util.ArrayList;

public class WordChecker {

    private Database _database;

    public WordChecker(){ _database = DocumentSession.getDatabase(); }

    public boolean check(String word){

        if(word == null) return false;

        var clauses = new ArrayList<Clause>();

        try{
            clauses.add(new Clause(new TableAlias("dictionary", -1), "word", CompareMethod.EQUAL, word));
            clauses.add(new Clause(new TableAlias("dictionary", -1), "state", CompareMethod.EQUAL, "accepted"));
            return _database.select(WordDictionary.class, clauses).size() > 0;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
