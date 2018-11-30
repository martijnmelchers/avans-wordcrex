package model;

import java.util.ArrayList;

public class GameModel {

    private ArrayList<String> _allowedWords = new ArrayList<>();

    private int _currentTurn;



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

    public GameModel() { }

    private void submitWord(){

    }
}
