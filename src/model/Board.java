package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
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

    public Board() {

        for (int x = 0; x < _tiles.length; x++) {
            for (int y = 0; y < _tiles[x].length; y++) {
                _tiles[x][y] = decideTileType(x, y);
            }
        }
    }

    public Tile[][] getTiles() { return _tiles; }

    public boolean isTaken(int x, int y) { return (_tiles[x][y].isEmpty()); }

    public void place(int x, int y, String letter) {
        _tiles[x][y].replace(letter, _letterValues.get(letter.toUpperCase()));
        _tiles[x][y].setColor(Color.WHITE);
        _placedCoords.add(new Vector2(x, y));
    }

    //Remove a piece
    public void remove(int x, int y){ _tiles[x][y] = decideTileType(x, y); }

    //Submit a piece to the database
    public void submit(){
        _placedCoords.clear();
    }

    public int check(Vector2 vector2){
        ArrayList<Tile> tiles = new ArrayList<>();

        for (int x = 0; x < 15; x++){
            if(!_tiles[x][vector2.getY()].isEmpty()){
                tiles.add(_tiles[x][vector2.getY()]);
            }
        }

        for (int y = 0; y < 15; y++){
            if(!_tiles[vector2.getX()][y].isEmpty()){
                tiles.add(_tiles[vector2.getX()][y]);
            }
        }

        //if(new WordChecker())

        return calculatePoints(tiles.toArray(new Tile[tiles.size()]));
    }

    private int calculatePoints(Tile[] tiles){

        int points = 0;

        int w3 = 0;
        int w4 = 0;

        for (int i = 0; i < tiles.length; i++){
            TileType tileType = tiles[i].getType();

            switch (tileType){
                case LETTER_TIMES_TWO:
                    points += _letterValues.get(tiles[i].getLetterType().getLetter()) * 2;
                    break;
                case LETTER_TIMES_FOUR:
                    points += _letterValues.get(tiles[i].getLetterType().getLetter()) * 4;
                    break;
                case LETTER_TIMES_SIX:
                    points += _letterValues.get(tiles[i].getLetterType().getLetter()) * 6;
                    break;
                case WORD_TIMES_THREE:
                    w3++;
                    break;
                case WORD_TIMES_FOUR:
                    w4++;
                    break;
            }
        }

        for (int j = 0; j < w3; j++){ points *= 3; }
        for (int j =0; j< w4; j++){ points  *= 4;}

        return points;
    }

    private Tile decideTileType(int x, int y) {

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
