package model;

import javafx.scene.paint.Color;

public class Board {

    private Tile[][] _tiles = new Tile[15][15];

    public Tile[][] getTiles() { return _tiles; }

    public Board()
    {

        for (int x = 0; x < _tiles.length; x++)
        {
            for (int y = 0; y < _tiles[x].length; y++)
            {
               _tiles[x][y] = decideTileType(x, y);
            }
        }

    }

    private void drawTiles() {}
    private void setTile() {}

    private Tile decideTileType(int x, int y){

        int postion = (x * 15) + y;
        System.out.println("Postion: " + postion);

        switch (postion)
        {
            case 0: case 14: case 79: case 85:
            case 91: case 103: case 121: case 133:
            case 139: case 145: case 210: case 224:
                return new Tile(Tile.TileType.letterTimesSix, Color.rgb(142, 234, 68));
            case 4: case 10: case 30: case 44:
            case 63: case 71: case 153: case 161:
            case 180: case 194: case 214: case 220:
                return new Tile(Tile.TileType.wordTimesThree, Color.rgb(234, 67, 209));
            case 7: case 33: case 41: case 50:
            case 54: case 61: case 73: case 82:
            case 110: case 114: case 142: case 151:
            case 163: case 170: case 174: case 183:
            case 191: case 217:
                return new Tile(Tile.TileType.letterTimesFour, Color.rgb(78, 94, 237));
            case 17: case 21: case 23: case 27:
            case 52: case 96: case 98: case 107:
            case 117: case 126: case 128: case 172:
            case 197: case 201: case 203: case 207:
                return new Tile(Tile.TileType.letterTimesTwo, Color.rgb(14, 212, 234));
            case 105: case 119:
                return new Tile(Tile.TileType.wordTimesFour, Color.rgb(232, 183, 39));
        }
        return new Tile(Tile.TileType.standard, Color.rgb(51, 43, 124));
    }

}
