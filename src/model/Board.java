package model;

import javafx.scene.paint.Color;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.tables.TurnBoardLetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Board {

    private ArrayList<Vector2> _placedCoords = new ArrayList<>();
    private HashMap<String, Integer> _letterValues = new HashMap<>() {{
        put("A", 2); put("B", 3); put("C", 6); put("D", 2); put("E", 1);
        put("F", 6); put("G", 4); put("H", 5); put("I", 2); put("J", 5);
        put("K", 4); put("L", 4); put("M", 3); put("N", 1); put("O", 2);
        put("P", 5); put("Q", 20); put("R", 2); put("S", 3); put("T", 2);
        put("U", 3); put("V", 5); put("W", 6); put("X", 8); put("Y", 9);
        put("Z", 6);
    }};
    private Tile[][] _tiles = new Tile[15][15];

    public void getBoardFromDatabase(int gameId,Integer turn_id) // with turn_id you can return state of board that moment (For history mode)
    {
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("turnboardletter", -1),"game_id" , CompareMethod.EQUAL, gameId ));
        clauses.add(new Clause(new TableAlias("turnboardletter", -1),"turn_id" , CompareMethod.LESS_EQUAL, turn_id ));
        try
        {
            List<TurnBoardLetter> grid = DocumentSession.getDatabase().select(TurnBoardLetter.class, clauses);
            for (TurnBoardLetter letter : grid)
            {
                _tiles[letter.getX()][letter.getY()].replace(letter.letter.get_symbol()+"", letter.letter.symbol.get_value(), letter.letter.get_letterId(), Color.rgb(247, 235, 160));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Vector2> getPlacedCoords(){ return _placedCoords; }
    public void clearPlacedCoords() { _placedCoords.clear(); }

    public Board() {

        for (int x = 0; x < _tiles.length; x++) {
            for (int y = 0; y < _tiles[x].length; y++) {
                _tiles[x][y] = decideTileType(new Vector2(x, y));
            }
        }
    }

    public Tile[][] getTiles() { return _tiles; }

    public boolean isEmpty(Vector2 vector2) { return (_tiles[vector2.getY()][vector2.getX()].isEmpty()); }

    public void place(Vector2 vector2, String letter, int letterId) {
        Tile tile = _tiles[vector2.getY()][vector2.getX()];// omgedraaid

        tile.replace(letter, _letterValues.get(letter.toUpperCase()), letterId);
        tile.setColor(Color.WHITE);
        tile.setState(TileState.UNLOCKED);

        _placedCoords.add(vector2);
    }

    //Remove a piece
    public Tile remove(Vector2 vector2){
        Tile prevTile = _tiles[vector2.getY()][vector2.getX()];

        _tiles[vector2.getY()][vector2.getX()] = decideTileType(vector2);
        _tiles[vector2.getY()][vector2.getX()].setState(TileState.LOCKED);
        _placedCoords.remove(_placedCoords.stream().filter(a->a.getY() == vector2.getY()&&a.getX() == vector2.getX()).collect(Collectors.toList()).get(0));
        return prevTile;
    }

    //Returned de punten die het woord geeft
    public CheckInfo check(){
        //TODO check elke keer als een tile geplaatst word
        ArrayList<Tile> tiles = new ArrayList<>();
        ArrayList<String> tileIds = new ArrayList<>();

        boolean usedStartingTile = !_tiles[7][7].isEmpty();
        boolean isStraight = _placedCoords.stream().allMatch(x -> x.getX() == _placedCoords.get(0).getX()) || _placedCoords.stream().allMatch(x -> x.getY() == _placedCoords.get(0).getY());

        boolean isConnected = true;

        if(_placedCoords.size() < 1) { return null; }

        isConnected = !_placedCoords.stream().allMatch( a-> _tiles[a.getX()][a.getY()-1].isEmpty()  || _tiles[a.getX()][a.getY()+1].isEmpty())
        || !_placedCoords.stream().allMatch( a-> _tiles[a.getX()-1][a.getY()].isEmpty()  || _tiles[a.getX()+1][a.getY()].isEmpty());

        if(!usedStartingTile || !isStraight || !isConnected) return null;

        for (Vector2 placedCoords : _placedCoords) {

            String wX = ""; //0 = x - 1 = y;
            String wY = "";

            int x = placedCoords.getX();
            int y = placedCoords.getY();

            String tileId = "";

            for (int j = 0; j < 15; j++) {
                if (!_tiles[x][j].isEmpty()) {
                    tiles.add(_tiles[x][j]);
                    wY = wY.concat(_tiles[x][j].getLetterType().getLetter());
                    tileId += x + j;
                }
                if (!_tiles[j][y].isEmpty()) {
                    tiles.add(_tiles[j][y]);
                    wX = wX.concat(_tiles[j][y].getLetterType().getLetter());
                }
            }

            WordChecker checker = new WordChecker();
            if (checker.check(wX) && !tileIds.contains(tileId)){ tileIds.add(tileId); }
            if (checker.check(wY) && !tileIds.contains(tileId)){ tileIds.add(tileId); }
        }

        if(tileIds.size()<1)
        {
            return null;
        }

        Tile[] tileArr = tiles.toArray(new Tile[0]);
        Vector2[] coordinatesArr = _placedCoords.toArray(new Vector2[0]);

        Points points = calculatePoints(tileArr);

        return new CheckInfo(points, tileArr, coordinatesArr);
    }

    private Points calculatePoints(Tile[] tiles){

        int score = 0;
        int bonus = 0;

        int w3 = 0;
        int w4 = 0;

        for (Tile tile : tiles) {
            TileType tileType = tile.getType();

            int letterValue = _letterValues.get(tile.getLetterType().getLetter());
            score += letterValue;

            switch (tileType) {
                case LETTER_TIMES_TWO:
                    bonus += letterValue * 2;
                    break;
                case LETTER_TIMES_FOUR:
                    bonus += letterValue * 4;
                    break;
                case LETTER_TIMES_SIX:
                    bonus += letterValue * 6;
                    break;
                case WORD_TIMES_THREE:
                    w3++;
                    break;
                case WORD_TIMES_FOUR:
                    w4++;
                    break;
            }
        }

        for (int j = 0; j < w3; j++){ bonus *= 3; }
        for (int j =0; j< w4; j++){ bonus  *= 4;}

        return new Points(score, bonus);
    }

    public void printBoard()//Testmethod
    {
        for(int i =0;i<14;i++)
        {
            String line = "";
            for(int k =0;k<14;k++)
            {
                String s = _tiles[i][k].getLetterType().getLetter();
                line += s==""?".":s ;
            }
            System.out.println(line);
        }
    }

    private Tile decideTileType(Vector2 vector2) {

        int x = vector2.getX();
        int y = vector2.getY();

        int position = (x * 15) + y;

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
}
