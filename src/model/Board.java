package model;

import javafx.scene.paint.Color;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.helper.Log;
import model.tables.TurnBoardLetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    WordChecker wordChecker = new WordChecker();
    private ArrayList<Vector2> _placedCoords = new ArrayList<>();
    private HashMap<String, Integer> _letterValues = new HashMap<>() {{
        this.put("A", 2);
        this.put("B", 3);
        this.put("C", 6);
        this.put("D", 2);
        this.put("E", 1);
        this.put("F", 6);
        this.put("G", 4);
        this.put("H", 5);
        this.put("I", 2);
        this.put("J", 5);
        this.put("K", 4);
        this.put("L", 4);
        this.put("M", 3);
        this.put("N", 1);
        this.put("O", 2);
        this.put("P", 5);
        this.put("Q", 20);
        this.put("R", 2);
        this.put("S", 3);
        this.put("T", 2);
        this.put("U", 3);
        this.put("V", 5);
        this.put("W", 6);
        this.put("X", 8);
        this.put("Y", 9);
        this.put("Z", 6);
    }};
    private Tile[][] _tiles = new Tile[15][15];

    public Board() {
        this.createDefaultBoard();
    }

    public int getLetterPoint(String letter) {
        return  _letterValues.get(letter.toUpperCase());
    }

    public void getBoardFromDatabase(int gameId, Integer turn_id) // with turn_id you can return state of board that moment (For history mode)
    {
        this.createDefaultBoard();
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("turnboardletter", -1), "game_id", CompareMethod.EQUAL, gameId));
        clauses.add(new Clause(new TableAlias("turnboardletter", -1), "turn_id", CompareMethod.LESS_EQUAL, turn_id));
        try {
            List<TurnBoardLetter> grid = DocumentSession.getDatabase().select(TurnBoardLetter.class, clauses);
            for (TurnBoardLetter letter : grid) {
                this._tiles[(letter.getY() - 1)][(letter.getX() - 1)].replace(letter.letter.getSymbol() + "", letter.letter.symbol.get_value(), letter.letter.getLetterId(), Color.rgb(247, 235, 160));
            }
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public ArrayList<Vector2> getPlacedCoords() {
        return this._placedCoords;
    }

    public void clearPlacedCoords() {
        this._placedCoords.clear();
    }

    private void createDefaultBoard() {
        for (int x = 0; x < this._tiles.length; x++) {
            for (int y = 0; y < this._tiles[x].length; y++) {
                this._tiles[y][x] = this.decideTileType(new Vector2(x, y));
            }
        }
    }

    public Tile[][] getTiles() {
        return this._tiles;
    }

    public boolean isEmpty(Vector2 vector2) {
        return (this._tiles[vector2.getY()][vector2.getX()].isEmpty());
    }

    public void place(Vector2 vector2, String letter, int letterId) {
        Tile tile = this._tiles[vector2.getY()][vector2.getX()];// omgedraaid

        tile.replace(letter, this._letterValues.get(letter.toUpperCase()), letterId);
        tile.setColor(Color.WHITE);
        tile.setState(TileState.UNLOCKED);

        this._placedCoords.add(vector2);
    }

    //Remove a piece
    public Tile remove(Vector2 vector2) {
        Tile prevTile = this._tiles[vector2.getY()][vector2.getX()];
        this._tiles[vector2.getY()][vector2.getX()] = this.decideTileType(vector2);
        this._tiles[vector2.getY()][vector2.getX()].setState(TileState.LOCKED);
        this._placedCoords.remove(this._placedCoords.stream().filter(a -> a.getY() == vector2.getY() && a.getX() == vector2.getX()).collect(Collectors.toList()).get(0));
        return prevTile;
    }

    public ArrayList<Tile> removeTiles() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Vector2 vector2 : this._placedCoords) {
            Tile prevTile = this._tiles[vector2.getY()][vector2.getX()];
            this._tiles[vector2.getY()][vector2.getX()] = this.decideTileType(vector2);
            this._tiles[vector2.getY()][vector2.getX()].setState(TileState.LOCKED);
            tiles.add(prevTile);
        }
        this._placedCoords.clear();
        return tiles;
    }

    private Points checkSubWordsVert(List<Vector2> locations) {
        Points temp = new Points(0,0);

        List<Points> temps = new ArrayList<Points>();
        //List<CheckInfo>


        for (Vector2 starting : locations) {
            List<Vector2> tempWordLocations = new ArrayList<Vector2>();
            String Word = "";
            for (int y = starting.getY(); y > -1 && !this._tiles[y][starting.getX()].isEmpty(); y--) {
                starting = new Vector2(starting.getX(), y);


            }
            for (int y = starting.getY(); y < 14 && !this._tiles[y][starting.getX()].isEmpty(); y++) {
                tempWordLocations.add(new Vector2(starting.getX(), y));
                Word += _tiles[y][starting.getX()].getLetterType().getLetter();
            }
            if (wordChecker.check(Word))
                temps.add(this.calculatePoints(tempWordLocations.stream().map(x -> this._tiles[x.getY()][x.getX()]).toArray(Tile[]::new)));
            else return null;
        }
        for(Points point: temps){
            temp.add(point);
        }
        return temp;//temp;
    }

    private Points checkSubWordsHorz(List<Vector2> locations) {
        Points temp = new Points(0,0);

        List<Points> temps = new ArrayList<Points>();
        //List<CheckInfo>


        for (Vector2 starting : locations) {
            List<Vector2> tempWordLocations = new ArrayList<Vector2>();
            String Word = "";
            for (int x = starting.getX(); x > -1 && !this._tiles[starting.getY()][x].isEmpty(); x--) {
                starting = new Vector2(x, starting.getY());


            }
            for (int x = starting.getX(); x < 14 && !this._tiles[starting.getY()][x].isEmpty(); x++) {
                tempWordLocations.add(new Vector2(x, starting.getY()));
                Word += _tiles[starting.getY()][x].getLetterType().getLetter();
            }
            if (wordChecker.check(Word))
                temps.add(this.calculatePoints(tempWordLocations.stream().map(x -> this._tiles[x.getY()][x.getX()]).toArray(Tile[]::new)));
            else return null;
        }
        for(Points point: temps){
            temp.add(point);
        }
        return temp;//temp;
    }


    //Returned de punten die het woord geeft
    public CheckInfo check() {


        if (_placedCoords.size() <1 || !this.newTilesConnected() || !this.isConnectedToOldTile())
            return null;



        final int tempx = this._placedCoords.stream().mapToInt(x -> x.getX()).min().orElseThrow();
        final int tempy = this._placedCoords.stream().mapToInt(x -> x.getY()).min().orElseThrow();
        Boolean isHorizontal = false;
        Boolean isVertical = false;
        if(this._placedCoords.stream().anyMatch(x -> x.getX() != tempx)) {
            isHorizontal = true;
        }
        if (this._placedCoords.stream().anyMatch(x -> x.getY() != tempy)) {
            isVertical = true;
        }
        if (isHorizontal == isVertical) {
            isHorizontal = true;
            isVertical = true;
        }




        List<Vector2> secondaryWordsHorizontal = new ArrayList<Vector2>();
        List<Vector2> secondaryWordsVertical = new ArrayList<Vector2>();
        List<Tile> mainWord = new ArrayList<Tile>();
        String word = "";
        if (isHorizontal) {
            int xMin = this._placedCoords.stream().mapToInt(x -> x.getX()).min().orElseThrow();
            int yMin = this._placedCoords.stream().mapToInt(x -> x.getY()).min().orElseThrow();
            word = "";
            mainWord = new ArrayList<Tile>();
            for (int x = xMin; x > -1 && !this._tiles[yMin][x].isEmpty(); x--) {
                xMin = x;
            }

            for (int x = xMin; x < 15 && !this._tiles[yMin][x].isEmpty(); x++) {
                word += this._tiles[yMin][x].getLetterType().getLetter();
                mainWord.add(_tiles[yMin][x]);
                if (this._tiles[yMin][x].getState() == TileState.UNLOCKED) {
                    if (yMin < 14 && !this._tiles[yMin + 1][x].isEmpty()) {
                        secondaryWordsVertical.add(new Vector2(x, yMin + 1));
                    } else if (yMin > 0 && !this._tiles[yMin - 1][x].isEmpty()) {
                        secondaryWordsVertical.add(new Vector2(x, yMin - 1));
                    }
                }
            }
            if(wordChecker.check(word))
                isVertical = false;
        }
        if (isVertical) {
            int xMin = this._placedCoords.stream().mapToInt(x -> x.getX()).min().orElseThrow();
            int yMin = this._placedCoords.stream().mapToInt(x -> x.getY()).min().orElseThrow();
            word = "";
            mainWord = new ArrayList<Tile>();
            for (int y = yMin; y > -1 && !this._tiles[y][xMin].isEmpty(); y--) {
                yMin = y;
            }


            for (int y = yMin; y < 15 && !this._tiles[y][xMin].isEmpty(); y++) {
                word += this._tiles[y][xMin].getLetterType().getLetter();
                mainWord.add(_tiles[y][xMin]);
                if (this._tiles[y][xMin].getState() == TileState.UNLOCKED) {
                    if (xMin < 14 && !this._tiles[y][xMin + 1].isEmpty()) {
                        secondaryWordsHorizontal.add(new Vector2(xMin + 1, y));
                    } else if (xMin < 14 && !this._tiles[y][xMin - 1].isEmpty()) {
                        secondaryWordsHorizontal.add(new Vector2(xMin - 1, y));
                    }
                }
            }
            if(wordChecker.check(word))
                isHorizontal= false;
        }
        if (word == "")
            return null;
        Points points = new Points(0,0);
        System.out.println(word);
        if (wordChecker.check(word) && isHorizontal != isVertical)
            points = (calculatePoints(mainWord.toArray(new Tile[0])));
        else if(!wordChecker.check(word) && isHorizontal != isVertical)
            return null;
        Points temp = this.checkSubWordsVert(secondaryWordsVertical);
        if(temp != null) points.add(temp);
        else return null;

        temp = this.checkSubWordsHorz(secondaryWordsHorizontal);
        if(temp !=null) points.add(temp);
        else return  null;

        //return new CheckInfo(0,)

        Vector2[] coordinatesArr = _placedCoords.toArray(new Vector2[0]);
        return new CheckInfo(points, coordinatesArr);
    }

    private boolean newTilesConnected() {

        if(this._tiles[7][7].isEmpty()) return false;

        int xMin = this._placedCoords.stream().mapToInt(x -> x.getX()).min().orElseThrow();
        int yMin = this._placedCoords.stream().mapToInt(x -> x.getY()).min().orElseThrow();

        boolean isVertical = !this._placedCoords.stream().anyMatch(x -> x.getX() != xMin);
        boolean isHorizontal = !this._placedCoords.stream().anyMatch(x -> x.getY() != yMin);

        if (!isHorizontal && !isVertical) {
            return false;
        }

        if (isHorizontal) {

            int index = xMin;
            int foundCount = 0;
            while(!(index>14)&&!_tiles[yMin][index].isEmpty())
            {
                final int tempIndex = index;
                if(_placedCoords.stream().anyMatch(a-> a.getX() == tempIndex && a.getY() == yMin))
                {
                    foundCount++;
                }
                index++;
            }
            return foundCount == _placedCoords.size();
            /*
            int xMax = this._placedCoords.stream().mapToInt(x -> x.getX()).max().orElseThrow();
            for (int i = xMin; i < xMax; i++) {
                Tile curTile = this._tiles[yMin][i];
                if (curTile.isEmpty()) {
                    return false;
                }
            }
            */
        } else {
            int index = yMin;
            int foundCount = 0;
            while(!(index>14)&&!_tiles[index][xMin].isEmpty())
            {
                final int tempIndex = index;
                if(_placedCoords.stream().anyMatch(a-> a.getY() == tempIndex&& a.getX() == xMin ))
                {
                    foundCount++;
                }
                index++;
            }
            return foundCount == _placedCoords.size();
        }
    }

    private boolean isConnectedToOldTile() {
        ArrayList<Boolean> tilesConnected = new ArrayList<>();
        ArrayList<Boolean> connectedToOldTiles = new ArrayList<>();

        if (this._placedCoords.size() < 1) {
            return false;
        }

        boolean boardHasOldTiles = Arrays.stream(this._tiles).anyMatch(c -> Arrays.stream(c).anyMatch(o -> o.getState() == TileState.LOCKED && !o.getLetterType().getLetter().equals("")));
        boolean isConnectedToOld = true;

        for (var c : this._placedCoords) {

            ArrayList<Boolean> isEmpty = new ArrayList<>();
            ArrayList<Boolean> connectedToOld = new ArrayList<>();

            //TODO refactor dit nog wel een keer want mn ogen doen pijn

            if (c.getX() - 1 > -1) {
                isEmpty.add(this._tiles[c.getY()][c.getX() - 1].isEmpty());// Check of hij leeg is
                connectedToOld.add(this._tiles[c.getY()][c.getX() - 1].getState() == TileState.LOCKED && !this._tiles[c.getY()][c.getX() - 1].getLetterType().getLetter().equals("")); // Check of hij aan een oud block gevoegd is
            }
            if (c.getX() + 1 < 15) {
                isEmpty.add(this._tiles[c.getY()][c.getX() + 1].isEmpty());
                connectedToOld.add(this._tiles[c.getY()][c.getX() + 1].getState() == TileState.LOCKED && !this._tiles[c.getY()][c.getX() + 1].getLetterType().getLetter().equals(""));
            }

            if (c.getY() - 1 > -1) {
                isEmpty.add(this._tiles[c.getY() - 1][c.getX()].isEmpty());
                connectedToOld.add(this._tiles[c.getY() - 1][c.getX()].getState() == TileState.LOCKED && !this._tiles[c.getY() - 1][c.getX()].getLetterType().getLetter().equals(""));
            }
            if (c.getY() + 1 < 15) {
                isEmpty.add(this._tiles[c.getY() + 1][c.getX()].isEmpty());
                connectedToOld.add(this._tiles[c.getY() + 1][c.getX()].getState() == TileState.LOCKED && !this._tiles[c.getY() + 1][c.getX()].getLetterType().getLetter().equals(""));
            }

            if (isEmpty.contains(false)) {
                tilesConnected.add(true);
            }
            if (boardHasOldTiles) {
                connectedToOldTiles.add(connectedToOld.contains(true));
            }else return true;
        }
        if(!connectedToOldTiles.contains(true)) return false;

        return isConnectedToOld;
    }

    private Points calculatePoints(Tile[] tiles) {

        int score = 0;
        int bonus = 0;

        int w3 = 0;
        int w4 = 0;

        for (Tile tile : tiles) {
            TileType tileType = tile.getType();
            System.out.println("Letter: " + tile.getLetterType().getLetter());
            int letterValue = this._letterValues.get(tile.getLetterType().getLetter());
            if (tile.getState() == TileState.UNLOCKED) {
                switch (tileType) {
                    case LETTER_TIMES_TWO:
                        score += letterValue;
                        bonus += letterValue;
                        break;
                    case LETTER_TIMES_FOUR:
                        score += letterValue;
                        bonus += letterValue * 3;
                        break;
                    case LETTER_TIMES_SIX:
                        score += letterValue;
                        bonus += letterValue * 5;
                        break;
                    case WORD_TIMES_THREE:
                        w3++;
                        score += letterValue;
                        break;
                    case WORD_TIMES_FOUR:
                        w4++;
                        score += letterValue;
                        break;
                    default:
                        score += letterValue;
                        break;
                }
            } else {
                score += letterValue;
            }
        }
        //if(w4 != 0|| w3 != 0)
           // score *= (int) Math.pow(4, w4) * (int) Math.pow(3, w3) -score;
        if(w4 != 0|| w3 != 0)
            bonus = (score + bonus) * (int) Math.pow(4, w4) * (int) Math.pow(3, w3) - score;


        return new Points(score, bonus);
    }

    public void printBoard()//Testmethod
    {
        for (int i = 0; i < 14; i++) {
            String line = "";
            for (int k = 0; k < 14; k++) {
                String s = this._tiles[k][i].getLetterType().getLetter();
                line += s == "" ? "." : s;
            }
            System.out.println(line);
        }
    }

    private Tile decideTileType(Vector2 vector2) {

        int x = vector2.getX();
        int y = vector2.getY();

        int position = (y * 15) + x;

        switch (position) {
            case 0:
            case 14:
            case 79:
            case 85:
            case 91:
            case 103:
            case 121:
            case 133:
            case 139:
            case 145:
            case 210:
            case 224:
                return new Tile(TileType.LETTER_TIMES_SIX, Color.rgb(142, 234, 68));
            case 4:
            case 10:
            case 30:
            case 44:
            case 63:
            case 71:
            case 153:
            case 161:
            case 180:
            case 194:
            case 214:
            case 220:
                return new Tile(TileType.WORD_TIMES_THREE, Color.rgb(234, 67, 209));
            case 7:
            case 33:
            case 41:
            case 50:
            case 54:
            case 61:
            case 73:
            case 82:
            case 110:
            case 114:
            case 142:
            case 151:
            case 163:
            case 170:
            case 174:
            case 183:
            case 191:
            case 217:
                return new Tile(TileType.LETTER_TIMES_FOUR, Color.rgb(78, 94, 237));
            case 17:
            case 21:
            case 23:
            case 27:
            case 52:
            case 96:
            case 98:
            case 107:
            case 117:
            case 126:
            case 128:
            case 172:
            case 197:
            case 201:
            case 203:
            case 207:
                return new Tile(TileType.LETTER_TIMES_TWO, Color.rgb(14, 212, 234));
            case 105:
            case 119:
                return new Tile(TileType.WORD_TIMES_FOUR, Color.rgb(232, 183, 39));
            case 112:
                return new Tile(TileType.STARTING_TILE, Color.rgb(252, 192, 30));
        }
        return new Tile(TileType.STANDARD, Color.rgb(51, 43, 124));
    }

    private String ReverseString(String string) {
        String temp = "";
        for (int i = string.length() - 1; i > -1; i--) {
            temp += string.toCharArray()[i];
        }

        return temp;
    }

}
