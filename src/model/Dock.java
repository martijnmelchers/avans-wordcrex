package model;

import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.HandLetter;
import model.tables.Letter;
import model.tables.Turn;
import model.tables.TurnBoardLetter;

import java.util.*;
import java.util.stream.Collectors;

public class Dock {
    private HandLetter[] letters;
    private Database _db;


    public Dock(boolean createNewHand, int gameId, int turnId) {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (Exception e) {
            Log.error(e);
        }
        this.letters = new HandLetter[7];
        if (createNewHand) {
            this.refill(gameId, turnId);
        } else {
            this.update(gameId, turnId);
        }

    }

    public String getNotUsedTiles(int gameId, int turnId) {
        return String.valueOf(this.notUsedLetters(gameId, turnId).size());
    }

    public List<TurnBoardLetter> getAllPlacedLetters(int gameId, int turn_id) {
        ArrayList<Clause> clauses = new ArrayList<>();

        clauses.add(new Clause(new TableAlias("TurnBoardLetter", -1), "game_id", CompareMethod.EQUAL, gameId));
        clauses.add(new Clause(new TableAlias("TurnBoardLetter", -1), "turn_id", CompareMethod.LESS_EQUAL, turn_id));

        try {
            var turnBoardLetters = new ArrayList<>(this._db.select(TurnBoardLetter.class, clauses));
            ArrayList<TurnBoardLetter> filtered = new ArrayList<>();

            // Database returns double results of one table so this function filters doubles
            for (TurnBoardLetter turnBoardLetter : turnBoardLetters) {
                if (filtered.stream().noneMatch(a -> a.getX() == turnBoardLetter.getX() && a.getY() == turnBoardLetter.getY())) {
                    filtered.add(turnBoardLetter);
                }
            }
            return filtered;
        } catch (Exception e) {
            Log.error(e, false);
            return null;
        }
    }

    public void update(int gameId, Integer turn_id) // update to turn specific hand (For history mode)
    {
        List<TurnBoardLetter> placed = this.getAllPlacedLetters(gameId, turn_id);

        List<HandLetter> handLetters;

        ArrayList<Clause> clauses = new ArrayList<>();

        clauses.add(new Clause(new TableAlias("HandLetter", -1), "game_id", CompareMethod.EQUAL, gameId));
        clauses.add(new Clause(new TableAlias("HandLetter", -1), "turn_id", CompareMethod.EQUAL, turn_id));

        for (TurnBoardLetter turnBoardLetter : placed) {
            clauses.add(new Clause(new TableAlias("HandLetter", -1), "letter_id", CompareMethod.NOT_EQUAL, turnBoardLetter.letter.getLetterId()));
        }

        try {
            handLetters = this._db.select(HandLetter.class, clauses);
        } catch (Exception e) {
            Log.error(e);
            return;
        }


        ArrayList<HandLetter> filtered = new ArrayList<>();

        // Database returns double results of one table so this function filters doubles
        for (HandLetter h : handLetters) {
            if (filtered.stream().noneMatch(a -> a.letter.getLetterId().equals(h.letter.getLetterId()) && a.letter.game.getGameId() == gameId)) {
                filtered.add(h);
            }
        }

        this.clearAll();
        Log.info("Filtered size: " + filtered.size());
        for (int i = 0; i < filtered.size(); i++) {
            this.letters[i] = filtered.get(i);
        }

    }

    public HandLetter[] getLetters() {
        return this.letters;
    }

    public model.Letter getLetterType(HandLetter letter, int points) {
        var letterType = new model.Letter(letter.letter.getSymbol());
        letterType.setId(letter.getLetterId());
        letterType.setPoints(points);
        return letterType;

    }

    public void replaceLettersDock(int gameId, int turnId) {

        this.clearAll();
        this.refill(gameId, turnId);
    }

    private void clearAll() {
        for (int i = 0; i < this.letters.length; i++) {
            this.letters[i] = null;
        }
    }

    private void insertLetterCollection(int gameId) {
        try {
            List<String> defaultLetters = Letter.defaultLetters();
            ArrayList<Letter> letterset = new ArrayList<>();
            for (int i = 1; i <= defaultLetters.size(); i++) {
                letterset.add(new Letter(i, gameId, "NL", defaultLetters.get(i - 1)));
            }
            this._db.insert(letterset);
        } catch (Exception e) {
            Log.error(e);
        }

    }

    public void refill(int gameId, int turnId) // on next turn refill dock positions that are empty
    {

        if (!this.isLetterSetPresent(gameId)) //letterset is not present
        {
            this.insertLetterCollection(gameId);
        }

        List<Letter> notUsed = this.notUsedLetters(gameId, turnId);

        for (int i = 0; i < this.letters.length; i++) {
            if (this.letters[i] != null) {
                int finalI = i;
                notUsed.remove(notUsed.stream().filter(a -> a.getLetterId().equals(this.letters[finalI].letter.getLetterId())).collect(Collectors.toList()).get(0));
            }
        }

        if (notUsed.size() == 0) {
            return;
        }

        for (int i = 0; i < this.letters.length; i++) {
            if (this.letters[i] == null) {
                int randomIndex;
                if (notUsed.size() == 1) {
                    randomIndex = 0;
                } else {
                    randomIndex = new Random().nextInt(notUsed.size() - 1) + 1;
                }
                Letter l = notUsed.get(randomIndex);
                Turn t = new Turn(gameId, turnId);
                this.letters[i] = new HandLetter(l.getLetterId(), turnId, gameId, l, t);
                notUsed.remove(randomIndex);
                try {
                    this._db.insert(this.letters[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeUsedLetters(List<Integer> ids) {
        for (Integer id : ids) {
            HandLetter handLetter;
            try {
                handLetter = Arrays.stream(this.letters)
                        .filter(Objects::nonNull)
                        .filter(a -> a.letter.getLetterId().equals(id))
                        .collect(Collectors.toList()).get(0);

            } catch (Exception e) {
                new Exception("Letter id not found").printStackTrace();
                continue;
            }
            int index = Arrays.stream(this.letters).collect(Collectors.toList()).indexOf(handLetter);
            this.letters[index] = null;
        }
    }

    private boolean isLetterSetPresent(int gameId) {
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Letter", -1), "game_id", CompareMethod.EQUAL, gameId));
        try {
            return this._db.select(Letter.class, clauses).size() > 1;
        } catch (Exception e) {
            Log.error(e, false);
        }
        return false;
    }

    private List<Letter> notUsedLetters(int gameId, int turnId) {
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("Letter", -1), "game_id", CompareMethod.EQUAL, gameId));
        List<Letter> availableLetters = new ArrayList<>();
        try {
            availableLetters = this._db.select(Letter.class, clauses);
        } catch (Exception e) {
            Log.error(e, true);
        }

        List<TurnBoardLetter> usedLetters = new ArrayList<>();

        try {
            clauses = new ArrayList<>();

            clauses.add(new Clause(new TableAlias("TurnBoardLetter", -1), "game_id", CompareMethod.EQUAL, gameId));
            clauses.add(new Clause(new TableAlias("TurnBoardLetter", -1), "turn_id", CompareMethod.LESS_EQUAL, turnId));

            usedLetters = this._db.select(TurnBoardLetter.class, clauses);
        } catch (Exception e) {
            Log.error(e);
        }

        List<Integer> ids = usedLetters.stream().map(a -> a.letter.getLetterId()).collect(Collectors.toList());

        return availableLetters.stream()
                .filter(a -> !ids.contains(a.getLetterId()))
                .collect(Collectors.toList());
    }

    public void print() {
        StringBuilder s = new StringBuilder();
        StringBuilder s1 = new StringBuilder();
        for (HandLetter l : this.letters) {
            s.append(l.getLetterId()).append(" ");
            s1.append(l.letter.getSymbol());
        }
        System.out.println(s);
        System.out.println(s1);
    }
}
