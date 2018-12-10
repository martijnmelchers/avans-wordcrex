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
        if(createNewHand)
        {
            refill(gameId, turnId);
        }
        else
        {
            update(gameId, turnId);
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

        ArrayList<HandLetter> filtered = new ArrayList<>();


        // Database returns double results of one table so this function filters doubles
        for(HandLetter h : handLetters)
        {
            if(!filtered.stream().anyMatch(a->a.letter.get_letterId() == h.letter.get_letterId()&& a.letter.game.getGameId() == _gameId))
            {
                filtered.add(h);
            }
        }

        clearAll();
        for (int i =0;i<filtered.size();i++)
        {
            letters[i] = filtered.get(i);
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

    private void insertLetterCollection(int gameId)
    {
        try
        {
            List<String> defaultLetters = Letter.defaultLetters();
            ArrayList<Letter> letterset = new ArrayList<>();
            for(int i =1;i<=defaultLetters.size();i++)
            {
                 letterset.add(new Letter(i,gameId,"NL",defaultLetters.get(i-1)));
            }
            db.insert(letterset);
        }
        catch (Exception e)
        {
            Log.error(e);
        }

    }

    public void refill(int gameId,int turnId) // on next turn refill dock positions that are empty
    {

        List<Letter> notUsed = notUsedLetters(gameId);

        for(int i =0; i<letters.length;i++)
        {
            notUsed.remove(letters[i]);
            /* if the above does not work
            if(letters[i]!= null)
            {
                int finalI = i;
                notUsed.remove(notUsed.stream().filter(a->a.get_letterId() == letters[finalI].letter.get_letterId()).collect(Collectors.toList()).get(0));
            }
           */
        }

        for(int i =0; i<letters.length;i++)
        {
            if(letters[i] == null)
            {
                int randomIndex = new Random().nextInt(notUsed.size());
                Letter l = notUsed.get(randomIndex);
                Turn t = new Turn(gameId,turnId);
                letters[i] = new HandLetter(l.get_letterId(),turnId, gameId,l,t);
                notUsed.remove(randomIndex);
            }
        }

        try
        {
            insertLetterCollection(gameId);
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