package view.MatchOverview;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.concurrent.TimeUnit;

public class FilterGameView {
    private enum FilterState {
        None,
        CountDown,
        Refreshing,
        Refreshed
    }

    private MatchOverview _matchOverview;

    private TextField _textBar;
    private final long _startTimer = TimeUnit.SECONDS.toNanos(3);
    private long _currentTime = _startTimer;

    private FilterState _filterState = FilterState.Refreshed;

    private String _currentGamesToSearch;
    private String _currentTextValue;

    private SearchSystem _searchSystem;
    private Thread _searchSystemThread;

    public FilterGameView(MatchOverview matchView, TextField searchBar) {
        _textBar = searchBar;
        _textBar.textProperty().addListener((observable, oldValue, newValue) -> searchTextChanged(observable, oldValue, newValue));

        _matchOverview = matchView;
        _searchSystem = new SearchSystem();
    }

    public void updateTimer(long elapsedTime) {
        if (_filterState != FilterState.CountDown) // Stop countdown while refreshing.
        {
            _currentTime = _startTimer;
            return;
        }

        _currentTime -= elapsedTime;

        if (_currentTime < 0) {
            _currentTime = _startTimer;
            _currentGamesToSearch = _currentTextValue;
            _filterState = FilterState.Refreshing;

            // start thread here.
            if(_searchSystemThread == null) // create new thread if necessary
            {
                _searchSystemThread = new Thread(_searchSystem);
                _searchSystemThread.start();
            }

            _searchSystem.setSearchFilters(_matchOverview.getGamesList(), _currentGamesToSearch);
        }
    }



    private void searchTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(newValue.length() == 0)
            _filterState = FilterState.None;

        if (newValue.length() > 20) // Ignore too long texts.
            return;

        // Don't need to refresh anymore.
        if (newValue.equals(_currentGamesToSearch)) {
            _filterState = FilterState.Refreshed;
            return;
        }

        // if new text has been chosen
        if (_filterState == FilterState.Refreshing) {
            // Stop thread here.

            _searchSystem.doStop();
        }

        // start countDown
        _filterState = FilterState.CountDown;
        _currentTextValue = newValue;
    }
}
