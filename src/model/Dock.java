package model;

import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.HandLetter;
import model.tables.Turn;
import model.tables.TurnBoardLetter;
import model.tables.Letter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Dock
{
    private HandLetter[] letters;
    private Database db;


    public Dock(boolean createNewHand,int gameId,int turnId)
    {
        try {
            db = DocumentSession.getDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }
        letters = new HandLetter[7];
        refill(gameId, turnId);

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

    public HandLetter[] getLetters()
    {
        return letters;
    }

    private void clearAll()
    {
        for (int i=0;i<letters.length; i++)
        {
            letters[i] = null;
        }
    }

    private void insertPot()
    {

    }

    public void refill(int gameId,int turnId) // on next turn refill dock positions that are empty
    {

        for(int i =0; i<letters.length;i++)
        {
            List<Letter> notUsed = notUsedLetters(gameId);
            if(letters[i] == null)
            {
                int randomIndex = new Random().nextInt(notUsed.size());
                Letter l = notUsed.get(randomIndex);
                Turn t = new Turn(gameId,turnId);
                letters[i] = new HandLetter(l.get_letterId(),turnId, gameId,l,t);
            }
        }

        try
        {
            db.insert(Arrays.stream(letters).filter(a-> a!=null).collect(Collectors.toList()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private List<Letter> notUsedLetters(int gameId)
    {
        List<Letter> availableLetters = new ArrayList<>();
        try
        {
            availableLetters = db.select(Letter.class);
        }
        catch (Exception e)
        {
            Log.error(e, true);
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

        List<Integer> ids = usedLetters.stream().map(a->a.letter.get_letterId()).collect(Collectors.toList());
        List<Letter> usableLetters = availableLetters.stream()
                .filter(a-> !ids.contains(a))
                .collect(Collectors.toList());

        return usableLetters;
    }
}
