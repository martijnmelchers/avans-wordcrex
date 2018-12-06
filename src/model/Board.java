package model;

import javafx.scene.paint.Color;
import model.database.classes.InsertedKeys;
import model.database.services.Database;
import model.tables.TurnPlayer1;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

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

    public boolean isEmpty(Vector2 vector2) { return (_tiles[vector2.getX()][vector2.getY()].isEmpty()); }

    public void place(Vector2 vector2, String letter) {
        _tiles[vector2.getX()][vector2.getY()].replace(letter, _letterValues.get(letter.toUpperCase()));
        _tiles[vector2.getX()][vector2.getY()].setColor(Color.WHITE);
        _placedCoords.add(vector2);
    }

    //Remove a piece
    public Tile remove(Vector2 vector2){
        Tile prevTile = _tiles[vector2.getX()][vector2.getY()];
        _tiles[vector2.getX()][vector2.getY()] = decideTileType(vector2);
        return prevTile;
    }


    //Returned de punten die het woord geeft
    public CheckInfo check(Vector2 vector2){

        ArrayList<Tile> tiles = new ArrayList<>();
        String[] words = new String[2]; //0 = x - 1 = y;

        for (int x = 0; x < 15; x++){
            if(!_tiles[x][vector2.getY()].isEmpty()){
                tiles.add(_tiles[x][vector2.getY()]);
                words[0] = words[0].concat(_tiles[x][vector2.getY()].getLetterType().getLetter());
            }
        }

        for (int y = 0; y < 15; y++){
            if(!_tiles[vector2.getX()][y].isEmpty()){
                tiles.add(_tiles[vector2.getX()][y]);
                words[1] = words[1].concat(_tiles[vector2.getX()][y].getLetterType().getLetter());
            }
        }

        WordChecker checker = new WordChecker();
        if(!checker.check(words[0]) && !checker.check(words[1])) return null;

        Tile[] tileArr = tiles.toArray(new Tile[tiles.size()]);
        Score score = calculatePoints(tileArr);

        return new CheckInfo(words, score, tileArr);
    }

    private Score calculatePoints(Tile[] tiles){

        int score = 0;
        int bonus = 0;

        int w3 = 0;
        int w4 = 0;

        for (int i = 0; i < tiles.length; i++){
            TileType tileType = tiles[i].getType();

            int letterValue = _letterValues.get(tiles[i].getLetterType().getLetter());
            score += letterValue;

            switch (tileType){
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

        return new Score(score, bonus);
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
        }
        return new Tile(TileType.STANDARD, Color.rgb(51, 43, 124));
    }
}
