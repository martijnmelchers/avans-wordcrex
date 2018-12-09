package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.tables.HandLetter;

import java.util.ArrayList;
import java.util.List;

public class Dock
{
    private HandLetter[] letters;
    private Database db;


    public Dock(boolean createNewHand)
    {
        db = DocumentSession.getDatabase();
        letters = new HandLetter[7];
        if(createNewHand)
        {
            refill();
        }
    }

    public void update(int _gameId,Integer turn_id) // update to turn specific hand (For history mode)
    {
        List<HandLetter> handLetters;

        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause( new TableAlias("HandLetter",-1) ,"game_id", CompareMethod.EQUAL ,_gameId ));
        clauses.add(new Clause( new TableAlias("HandLetter",-1) ,"turn_id", CompareMethod.EQUAL ,turn_id ));
        try
        {
            handLetters = db.select(HandLetter.class, clauses);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        clearAll();
        for (int i =0;i<handLetters.size();i++)
        {
            letters[i] = handLetters.get(i);
        }

    }

    private void clearAll()
    {
        for (int i=0;i<letters.length; i++)
        {
            letters[i] = null;
        }
    }

    public void refill() // on next turn refill dock positions that are empty
    {
        for(HandLetter letter : letters )
        {
            if(letter == null)
            {
                try
                {

                }
                catch (Exception e)
                {

                }
            }
        }
    }

    private Letter getLetterFromRemainingLetters()
    {
        return null;
    }

    private void addLetterToHand()
    {

    }
}
