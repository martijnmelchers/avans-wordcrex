package view.MatchOverview;

import controller.MatchOverviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.tables.Game;
import view.View;

import java.util.List;

public class MatchOverview extends View {

    @FXML
    private ListView gameListview;

    @FXML
    private ListView gameListview1;

    @FXML
    private ListView gameListview2;


    private ObservableList<Game> gameObservableList;

    private ObservableList<Game> gameObservableList1;

    private ObservableList<Game> gameObservableList2;

    private MatchOverviewController _controller;

    @FXML
    private Button _viewModeButton;

    @FXML
    private TextField _searchBar;



    private ObservableList<Game> gameList;
    private ObservableList<Game> gameList1;
    private ObservableList<Game> gameList2;


    @FXML
    private Pane requestPane;
    @FXML
    private Pane yourTurnPane;
    @FXML
    private Pane theirTurnPane;

    public MatchOverview(){

    }

    public void loadFinished(){
        try {
            this._controller = this.getController(MatchOverviewController.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderGames();
    }

    private void renderGames(){
        gameObservableList = FXCollections.observableArrayList();
        gameObservableList1 = FXCollections.observableArrayList();
        gameObservableList2 = FXCollections.observableArrayList();
        gameListview.setItems(gameObservableList);
        gameListview1.setItems(gameObservableList1);
        gameListview2.setItems(gameObservableList2);
        gameObservableList.clear();
        gameObservableList1.clear();
        gameObservableList2.clear();

        List<Game> games = this._controller.getGames();

        for (var game : games) {
            switch (game.getGameState().getState()) {
                case "request": {
                    gameObservableList.add(game);
                    break;
                }

                case "playing": {
                    if (this._controller.isMyTurn(game)) {
                        gameObservableList1.add(game);
                    } else {
                        gameObservableList2.add(game);
                    }
                }

                case "finished": {
                    //TODO: show finished games
                }
                case "resigned": {
                    //TODO: show resigned games??
                }
            }

            gameListview.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                return listViewCell;
            });


            gameListview1.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                return listViewCell;
            });

            gameListview2.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                return listViewCell;
            });

        }
    }

    @FXML
    public void filter(){
        String filter = _searchBar.getText();
        FilteredList<Game> filteredGames = new FilteredList<>(gameObservableList, s -> true);
        FilteredList<Game> filteredGames1 = new FilteredList<>(gameObservableList1, s -> true);
        FilteredList<Game> filteredGames2 = new FilteredList<>(gameObservableList2, s -> true);
        if(filter == null || filter.length() == 0){
            filteredGames.setPredicate(s -> true);
        }
        else{
            filteredGames.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        if(filter == null || filter.length() == 0){
            filteredGames1.setPredicate(s -> true);
        }
        else{
            filteredGames1.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        if(filter == null || filter.length() == 0){
            filteredGames2.setPredicate(s -> true);
        }
        else{
            filteredGames2.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        gameListview.setItems(filteredGames);
        gameListview1.setItems(filteredGames1);
        gameListview2.setItems(filteredGames2);
    }
    // Shows all buttons whe have access to.
    private void showAccessibleButtons(){

    }

    @FXML
    private void invitationView(){

        try{
            this._controller.navigate("MatchInvitationView");
        }
        catch(Exception e){

        }
    }
}
