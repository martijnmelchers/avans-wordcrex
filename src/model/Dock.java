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

    public List<TurnBoardLetter> getAllPlacedLetters(int gameId, int turn_id)
    {
        ArrayList<Clause> clauses = new ArrayList<>();

        clauses.add(new Clause( new TableAlias("TurnBoardLetter",-1) ,"game_id", CompareMethod.EQUAL ,gameId ));
        clauses.add(new Clause( new TableAlias("TurnBoardLetter",-1) ,"turn_id", CompareMethod.LESS_EQUAL ,turn_id ));

        try
        {
            var turnBoardLetters = db.select(TurnBoardLetter.class, clauses).stream().collect(Collectors.toList());
            ArrayList<TurnBoardLetter> filtered = new ArrayList<>();

            // Database returns double results of one table so this function filters doubles
            for(TurnBoardLetter turnBoardLetter : turnBoardLetters)
            {
                if(!filtered.stream().anyMatch(a->a.getX() == turnBoardLetter.getX()&& a.getY() == turnBoardLetter.getY()))
                {
                    filtered.add(turnBoardLetter);
                }
            }
            return turnBoardLetters;
        }
        catch (Exception e)
        {
            Log.error(e,false );
            return null;
        }
    }

    public void update(int gameId,Integer turn_id) // update to turn specific hand (For history mode)
    {
        List<TurnBoardLetter> placed = getAllPlacedLetters(gameId, turn_id);

        List<HandLetter> handLetters;

        ArrayList<Clause> clauses = new ArrayList<>();

        clauses.add(new Clause( new TableAlias("HandLetter",-1) ,"game_id", CompareMethod.EQUAL ,gameId ));
        clauses.add(new Clause( new TableAlias("HandLetter",-1) ,"turn_id", CompareMethod.LESS_EQUAL ,turn_id ));

        for(TurnBoardLetter turnBoardLetter : placed)
        {
            clauses.add(new Clause( new TableAlias("HandLetter",-1) ,"letter_id", CompareMethod.NOT_EQUAL ,turnBoardLetter.letter.get_letterId()));
        }

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
            if(!filtered.stream().anyMatch(a->a.letter.get_letterId() == h.letter.get_letterId()&& a.letter.game.getGameId() == gameId))
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

        if(!isLetterSetPresent(gameId)) //letterset is not present
        {
            insertLetterCollection(gameId);
        }

        List<Letter> notUsed = notUsedLetters(gameId);

        for(int i =0; i<letters.length;i++)
        {
            if(letters[i]!= null)
            {
                int finalI = i;
                notUsed.remove(notUsed.stream().filter(a->a.get_letterId() == letters[finalI].letter.get_letterId()).collect(Collectors.toList()).get(0));
            }
        }

        for(int i =0; i<letters.length;i++)
        {
            if(letters[i] == null)
            {
                int randomIndex = new Random().nextInt(notUsed.size()-1)+1;
                Letter l = notUsed.get(randomIndex);
                Turn t = new Turn(gameId,turnId);
                letters[i] = new HandLetter(l.get_letterId(),turnId, gameId,l,t);
                notUsed.remove(randomIndex);
                try
                {
                    db.insert(letters[i]);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeUsedLetters(List<Integer> ids)
    {
        for(Integer id : ids)
        {
            HandLetter handLetter;
            try
            {
                 handLetter = Arrays.stream(this.letters)
                         .filter(a->a != null)
                         .filter(a->a.letter.get_letterId() == id)
                         .collect(Collectors.toList()).get(0);

            }
            catch (Exception e)
            {
                new Exception("Letter id not found").printStackTrace();
                continue;
            }
            int index = Arrays.stream(letters).collect(Collectors.toList()).indexOf(handLetter);
            letters[index] = null;
        }
    }

    private boolean isLetterSetPresent(int gameId)
    {
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Letter",-1 ),"game_id" , CompareMethod.EQUAL,gameId));
        try
        {
            return db.select(Letter.class, clauses).size()>1;
        }
        catch (Exception e)
        {
            Log.error(e,false );
        }
        return false;
    }

    private List<Letter> notUsedLetters(int gameId)
    {
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Letter", -1),"game_id" ,CompareMethod.EQUAL , gameId));
        List<Letter> availableLetters = new ArrayList<>();
        try
        {
            availableLetters = db.select(Letter.class,clauses);
        }
        catch (Exception e)
        {
            Log.error(e, true);
        }

        List<TurnBoardLetter> usedLetters = new ArrayList<>();

        try
        {
            clauses = new ArrayList<>();
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

    public void print()
    {
        String s = "";
        String s1 = "";
        for (HandLetter l : letters)
        {
            s+= l.get_letterId()+" ";
            s1+= l.letter.get_symbol();
        }
        System.out.println(s);
        System.out.println(s1);
    }
}
