package view.MatchOverview;

import model.tables.Game;

import java.util.ArrayList;

// TODO To model
public class SearchSystem implements Runnable {

    private ArrayList<Game> _gameList;



    public SearchSystem() {

    }

    public synchronized void doStop() {
        _gameList = null;
    }

    public synchronized void setSearchFilters(ArrayList<Game> gamesList, String filter) {
        _gameList = gamesList;
    }

    @Override
    public void run() {
        while (_gameList != null) {

        }
    }
}
