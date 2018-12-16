package view.MatchOverview_old;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.concurrent.TimeUnit;

public class FilterGameView {
    private enum FilterState {
        None,
        CountDown,
        Refreshed
    }

    private MatchOverview _matchOverview;

    private TextField _textBar;
    private final long _startTimer = TimeUnit.SECONDS.toNanos(2);
    private long _currentTime = _startTimer;

    private FilterState _filterState = FilterState.Refreshed;

    private String _currentGamesToSearch;
    private String _currentTextValue;


    public FilterGameView(MatchOverview matchView, TextField searchBar) {
        _textBar = searchBar;
        _textBar.textProperty().addListener((observable, oldValue, newValue) -> searchTextChanged(observable, oldValue, newValue));

        _matchOverview = matchView;
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
            _filterState = FilterState.Refreshed;

            // start searching from here.
            _matchOverview.filterObserverGames(_currentGamesToSearch);
        }
    }

    private void searchTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(newValue.length() == 0) {
            _matchOverview.filterObserverGames(newValue);
            _filterState = FilterState.None;
        }
        if (newValue.length() > 20) // Ignore too long texts.
            return;

        // Don't need to refresh anymore.
        if (newValue.equals(_currentGamesToSearch)) {
            _filterState = FilterState.Refreshed;
            return;
        }

        // start countDown
        _filterState = FilterState.CountDown;
        _currentTextValue = newValue;
    }
}
