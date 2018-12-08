package model;

import model.database.services.Database;
import model.tables.HandLetter;

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

    public void refill() // on next turn refill dock positions that are empty
    {
        // refill empty space if player won
        for(HandLetter letter : letters )
        {
            if(letter == null)
            {
                try
                {
                    List<HandLetter> l = db.select(HandLetter.class, null);
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
