package view.MatchOverview;

import controller.MatchOverviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.GameSession;
import model.MatchOverviewModel;
import model.helper.Log;
import model.tables.Game;
import view.View;

import java.util.List;

public class MatchOverview extends View {

    @FXML
    private ListView _gameListView;

    @FXML
    private ListView _gameListView1;

    @FXML
    private ListView _gameListView2;

    @FXML
    private GridPane _gridParent;

    private ObservableList<Game> _gameObservableList;

    private ObservableList<Game> _gameObservableList1;

    private ObservableList<Game> _gameObservableList2;

    private MatchOverviewController _controller;

    @FXML
    private Button _observerModeButton;

    @FXML
    private TextField _searchBar;


    public MatchOverview() {

    }

    public void loadFinished() {
        try {
            this._controller = this.getController(MatchOverviewController.class);
        } catch (Exception e) {
            Log.error(e);
        }
        this.renderGames();
        this.ScaleScreen(_gridParent);

        this.disableNotAllowed();
    }


    private void disableNotAllowed() {
        if (!GameSession.hasRole("observer")) {
            this._observerModeButton.setDisable(true);
        }

        if (!GameSession.hasRole("administrator")) {

        }
    }

    public void renderGames() {
        this._gameObservableList = FXCollections.observableArrayList();
        this._gameObservableList1 = FXCollections.observableArrayList();
        this._gameObservableList2 = FXCollections.observableArrayList();

        this._gameListView.setItems(this._gameObservableList);
        this._gameListView1.setItems(this._gameObservableList1);
        this._gameListView2.setItems(this._gameObservableList2);

        this._gameObservableList.clear();
        this._gameObservableList1.clear();
        this._gameObservableList2.clear();

        List<Game> games = this._controller.getGames();


        for (var game : games) {
            switch (game.getGameState().getState()) {
                case "request": {
                    if (game.getAnswer().get_type().equals("unknown")) {
                        this._gameObservableList.add(game);
                    }
                    break;
                }

                case "playing": {
                    boolean isMyTurn;

                    try {
                        isMyTurn = MatchOverviewModel.isMyTurn(game);
                    } catch (NullPointerException e) {
                        isMyTurn = true;
                    }

                    if (isMyTurn) {
                        this._gameObservableList1.add(game);
                    } else {
                        this._gameObservableList2.add(game);
                    }
                    break;
                }

                case "finished": {
                    //TODO: show finished games
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
                listViewCell.setMatchOverview(this);
                return listViewCell;
            });


            this._gameListView1.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                listViewCell.setMatchOverview(this);
                return listViewCell;
            });

            this._gameListView2.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                listViewCell.setMatchOverview(this);
                return listViewCell;
            });

        }
    }

    @FXML
    public void filter() {
        String filter = this._searchBar.getText();
        FilteredList<Game> filteredGames = new FilteredList<>(this._gameObservableList, s -> true);
        FilteredList<Game> filteredGames1 = new FilteredList<>(this._gameObservableList1, s -> true);
        FilteredList<Game> filteredGames2 = new FilteredList<>(this._gameObservableList2, s -> true);
        if (filter == null || filter.length() == 0) {
            filteredGames.setPredicate(s -> true);
        } else {
            filteredGames.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        if (filter == null || filter.length() == 0) {
            filteredGames1.setPredicate(s -> true);
        } else {
            filteredGames1.setPredicate(s -> {
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
        this._gameListView1.setItems(filteredGames1);
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
    private void navigateObserver() {
        try {
            this._controller.navigate("ObserverOverview", 620, 770);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @FXML
    private void accountInfo() {
        try {
            this._controller.navigate("AccountInformation");
        } catch (Exception e) {
            Log.error(e);
        }
    }


    @FXML
    public void refresh() {
        this.renderGames();
    }

    @FXML
    private void invitationView() {

        try {
            this._controller.navigate("MatchInvitationView");
        } catch (Exception e) {
            Log.error(e);
        }
    }
}
