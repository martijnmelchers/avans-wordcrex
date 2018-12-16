package model;

import javafx.scene.paint.Color;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.tables.TurnBoardLetter;

import java.util.ArrayList;
import java.util.Arrays;
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
        createDefaultBoard();
        ArrayList<Clause> clauses = new ArrayList<>();
        clauses.add(new Clause(new TableAlias("turnboardletter", -1),"game_id" , CompareMethod.EQUAL, gameId ));
        clauses.add(new Clause(new TableAlias("turnboardletter", -1),"turn_id" , CompareMethod.LESS_EQUAL, turn_id ));
        try
        {
            List<TurnBoardLetter> grid = DocumentSession.getDatabase().select(TurnBoardLetter.class, clauses);
            for (TurnBoardLetter letter : grid)
            {
                _tiles[(letter.getY() - 1)][(letter.getX() - 1)].replace(letter.letter.get_symbol()+"", letter.letter.symbol.get_value(), letter.letter.get_letterId(), Color.rgb(247, 235, 160));
            }
        }
        catch (Exception e)
        {
            Handle.error(e);
        }
    }

    public ArrayList<Vector2> getPlacedCoords(){ return _placedCoords; }
    public void clearPlacedCoords() { _placedCoords.clear(); }

    public Board() {
        createDefaultBoard();
    }

    private void createDefaultBoard()
    {
        for (int x = 0; x < _tiles.length; x++) {
            for (int y = 0; y < _tiles[x].length; y++) {
                _tiles[y][x] = decideTileType(new Vector2(x, y));
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

    public ArrayList<Tile> removeTiles()
    {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Vector2 vector2 : _placedCoords)
        {
            Tile prevTile = _tiles[vector2.getY()][vector2.getX()];
            _tiles[vector2.getY()][vector2.getX()] = decideTileType(vector2);
            _tiles[vector2.getY()][vector2.getX()].setState(TileState.LOCKED);
            tiles.add(prevTile);
        }
        _placedCoords.clear();
        return tiles;
    }

    //Returned de punten die het woord geeft
    public CheckInfo check(){

        ArrayList<Tile> words = new ArrayList<>();

        ArrayList<String> tileIds = new ArrayList<>();


        for (Vector2 placedCoords : _placedCoords) {

            ArrayList<Tile> tilesX = new ArrayList<>();
            ArrayList<Tile> tilesY = new ArrayList<>();

            int x = placedCoords.getX();
            int y = placedCoords.getY();

            String woordX = "";
            String woordY = "";

            String tileId = "";

            for (int j = 0; j < 14; j++){ //Eerst links checken dan rechts
                if(x - j == -1) break;
                if(_tiles[y][x - j].isEmpty()) break;

                tilesX.add(_tiles[y][x - j]);
                woordX += _tiles[y][x - j].getLetterType().getLetter();
            }

            woordX = ReverseString(woordX);

            for (int j = 1; x + j <= 14; j++){ //Begint bij 1 want dan overlappen ze niet met elkaar
                if(_tiles[y][x + j].isEmpty()) break;

                tilesX.add(_tiles[y][x + j]);

                woordX += _tiles[y][x + j].getLetterType().getLetter();
                tileId += y +(x + j);
            }


            for (int j = 0; j < 14; j++){ //Eerst links checken dan rechts
                if(y - j == -1) break;
                if(_tiles[y - j][x].isEmpty()) break;

                tilesY.add(_tiles[y - j][x]);
                woordY += _tiles[y - j][x].getLetterType().getLetter();
            }

            woordY = ReverseString(woordY);

            for (int j = 1; y + j <= 14; j++){ //Begint bij 1 want dan overlappen ze niet met elkaar
                if(_tiles[y + j][x].isEmpty()) break;

                tilesY.add(_tiles[y + j][x]);
                woordY += _tiles[y + j][x].getLetterType().getLetter();

            }

            System.out.println("Woord X: " + woordX +" Woord Y:"+ woordY);
            WordChecker checker = new WordChecker();
            boolean bothSidesCorrect = checker.check(woordX) && checker.check(woordY);

            if(bothSidesCorrect && !tileIds.contains(tileId)){
                tileIds.add(tileId);
                if(tilesX.size() > 1)words.addAll(tilesX);
                if(tilesY.size() > 1)words.addAll(tilesY);
            }
        }

        if(tileIds.size()<1 || !moveIsLegit()) { return null; }

        Tile[] tileArr = words.toArray(new Tile[0]);
        Vector2[] coordinatesArr = _placedCoords.toArray(new Vector2[0]);

        Points points = calculatePoints(words.toArray(new Tile[0]));

        return new CheckInfo(points, tileArr, coordinatesArr);
    }

    private boolean moveIsLegit(){
        ArrayList<Boolean> tilesConnected = new ArrayList<>();
        ArrayList<Boolean> connectedToOldTiles = new ArrayList<>();

        boolean usedStartingTile = !_tiles[7][7].isEmpty();
        boolean isStraight = _placedCoords.stream().allMatch(x -> x.getX() == _placedCoords.get(0).getX()) || _placedCoords.stream().allMatch(x -> x.getY() == _placedCoords.get(0).getY());

        if(_placedCoords.size() < 1) { return false; }


        boolean boardHasOldTiles = Arrays.stream(_tiles).anyMatch(c -> Arrays.stream(c).anyMatch(o -> o.getState() == TileState.LOCKED && !o.getLetterType().getLetter().equals("")));
        boolean isConnectedToOld = true;

        for (var c : _placedCoords) {

            ArrayList<Boolean> isEmpty = new ArrayList<>();
            ArrayList<Boolean> connectedToOld = new ArrayList<>();

            //TODO refactor dit nog wel een keer want mn ogen doen pijn

            if (c.getX()-1 > -1) {
                isEmpty.add(_tiles[c.getY()][c.getX()-1].isEmpty());// Check of hij leeg is
                connectedToOld.add(_tiles[c.getY()][c.getX()-1].getState() == TileState.LOCKED && !_tiles[c.getY()][c.getX() - 1].getLetterType().getLetter().equals("")); // Check of hij aan een oud block gevoegd is
            }
            if(c.getX()+1 < 15){
                isEmpty.add(_tiles[c.getY()][c.getX()+1].isEmpty());
                connectedToOld.add(_tiles[c.getY()][c.getX()+1].getState() == TileState.LOCKED && !_tiles[c.getY()][c.getX() + 1].getLetterType().getLetter().equals(""));
            }

            if (c.getY()-1 > -1) {
                isEmpty.add(_tiles[c.getY()-1][c.getX()].isEmpty());
                connectedToOld.add(_tiles[c.getY() - 1][c.getX()].getState() == TileState.LOCKED && !_tiles[c.getY() - 1][c.getX()].getLetterType().getLetter().equals(""));
            }
            if(c.getY()+1 < 15){
                isEmpty.add(_tiles[c.getY()+1][c.getX()].isEmpty());
                connectedToOld.add(_tiles[c.getY()+1][c.getX()].getState() == TileState.LOCKED && !_tiles[c.getY()+1][c.getX()].getLetterType().getLetter().equals(""));
            }

            if(isEmpty.contains(false)) { tilesConnected.add(true); }
            if(boardHasOldTiles) { connectedToOldTiles.add(connectedToOld.contains(true)); }
        }

        boolean isConnected = _placedCoords.size() == tilesConnected.size();
        if(boardHasOldTiles) { isConnectedToOld = connectedToOldTiles.contains(true); }

        if(!usedStartingTile || !isStraight || !isConnected || !isConnectedToOld) return false;

        return true;
    }

    private Points calculatePoints(Tile[] tiles){

        int score = 0;
        int bonus = 0;

        int w3 = 0;
        int w4 = 0;

    for (Tile tile : tiles) {
        TileType tileType = tile.getType();

            int letterValue = _letterValues.get(tile.getLetterType().getLetter());

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
                default:
                    score += letterValue;
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
                String s = _tiles[k][i].getLetterType().getLetter();
                line += s==""?".":s ;
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

    private String ReverseString(String string){
        String temp = "";
        for (int i = string.length() - 1; i > -1; i--){
            temp += string.toCharArray()[i];
        }

        return temp;
    }

}
