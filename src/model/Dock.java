package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.tables.HandLetter;
import model.tables.TurnBoardLetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    public void refill(int gameId,int turnId) // on next turn refill dock positions that are empty
    {

        for(int i =0; i<letters.length;i++)
        {
            List<Letter> notUsed = notUsedLetters(gameId);
            try
            {

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            if(letters[i] == null)
            {
                int randomIndex = new Random().nextInt(notUsed.size());
                Letter l = notUsed.get(randomIndex);
                letters[i] = new HandLetter(l.getid(),turnId, gameId);
            }
        }


    }

    private List<Letter> notUsedLetters(int gameId)
    {
        List<Letter> availableLetters = new ArrayList<>();
        try
        {
            availableLetters = db.select(Letter.class,null );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        List<TurnBoardLetter> usedLetters = new ArrayList<>();

        try
        {
            ArrayList<Clause> clauses = new ArrayList<>();
            clauses.add(new Clause(new TableAlias("TurnBoardLetter", -1),"game_id" ,CompareMethod.EQUAL , gameId));
            usedLetters = db.select(TurnBoardLetter.class,clauses);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String[] ids = usedLetters.stream().map(a->a.letter.get_letterId()).toArray(String[]::new);
        List<Letter> usableLetters = availableLetters.stream()
                .filter(a-> Arrays.stream(ids).anyMatch(
                        b-> b.equals(a.getid()+"")))
                .collect(Collectors.toList());

        return usableLetters;
    }

    private void addLetterToHand()
    {

    }
}
