package view.ObserverOverview;

import controller.MatchOverviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.helper.Log;
import model.tables.Game;
import view.View;

import java.util.List;

public class ObserverOverview extends View {

    @FXML
    private ListView _gameListView;


    @FXML
    private ListView _gameListView2;

    @FXML
    private GridPane _gridParent;

    private ObservableList<Game> gameObservableList;

    private ObservableList<Game> gameObservableList2;

    private MatchOverviewController _controller;

    @FXML
    private TextField _searchBar;


    public ObserverOverview() {

    }

    public void loadFinished() {
        try {
            this._controller = this.getController(MatchOverviewController.class);
        } catch (Exception e) {
            Log.error(e);
        }
        this.renderGames();
        this.ScaleScreen(this._gridParent);
    }

    private void renderGames() {
        this.gameObservableList = FXCollections.observableArrayList();
        this.gameObservableList2 = FXCollections.observableArrayList();
        this._gameListView.setItems(this.gameObservableList);
        this._gameListView2.setItems(this.gameObservableList2);
        this.gameObservableList.clear();
        this.gameObservableList2.clear();

        List<Game> games = this._controller.getAllGames();


        for (var game : games) {
            switch (game.getGameState().getState()) {
                case "request": {
                    break;
                }

                case "playing": {
                    this.gameObservableList.add(game);
                    break;
                }

                case "finished": {
                    //TODO: show finished games
                    this.gameObservableList2.add(game);
                    break;
                }

                case "resigned": {
                    //TODO: show resigned games??
                    break;
                }
            }

            this._gameListView.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                return listViewCell;
            });


            this._gameListView2.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                return listViewCell;
            });

        }
    }

    @FXML
    public void filter() {
        String filter = this._searchBar.getText();
        FilteredList<Game> filteredGames = new FilteredList<>(this.gameObservableList, s -> true);
        FilteredList<Game> filteredGames2 = new FilteredList<>(this.gameObservableList2, s -> true);
        if (filter == null || filter.length() == 0) {
            filteredGames.setPredicate(s -> true);
        } else {
            filteredGames.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }


        if (filter == null || filter.length() == 0) {
            filteredGames2.setPredicate(s -> true);
        } else {
            filteredGames2.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        this._gameListView.setItems(filteredGames);
        this._gameListView2.setItems(filteredGames2);
    }

    // Shows all buttons whe have access to.
    private void showAccessibleButtons() {

    }


    @FXML
    private void logOut() {
        this._controller.endSession();
        try {
            this._controller.navigate("LoginView", 350, 550);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    @FXML
    private void playMode() {
        try {
            this._controller.navigate("MatchOverview", 861, 920);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @FXML
    public void refresh() {
        this.renderGames();
    }

}
