package model;

import java.util.ArrayList;

public class GameModel {

    private ArrayList<String> _allowedWords = new ArrayList<>();

    private int _currentTurn;
    private Board _board;


    public GameModel()
    {
        _board = new Board();
    }

    public Tile[][] getTiles(){ return _board.getTiles(); }

    public Letter[] getDock()
    {
        //TODO: get letters from database
        return new Letter[]
        {
            new Letter("A"),
            new Letter("G"),
            new Letter("D"),
            new Letter("L"),
            new Letter("E"),
            new Letter("N"),
            new Letter("E"),
        };
    }

    private void getBoardDisplayPieces(){

    }

    private void getPlayerPieces(){

    }

    private void getPlayers(){

    }

    private void getGameState(){

    }

    private void getScore(){

    }



    private boolean validateWord(String word){
        if(_allowedWords.contains(word)) return true;
        return false;
    }



    private void submitWord(){

    }
}
