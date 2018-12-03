package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.tables.WordDictionary;

import java.util.ArrayList;

public class WordChecker {

    private Database _database;

    public WordChecker(Database db){
        _database = db;
    }

    public boolean check(String word){
        var clauses = new ArrayList<Clause>();

        try{
            clauses.add(new Clause(new TableAlias("dictionary", -1), "word", CompareMethod.LIKE, word));
            return _database.select(WordDictionary.class, clauses).get(0).getWord().equals(word);

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
