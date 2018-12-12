package view.MatchOverview;

import controller.BoardController;
import controller.MatchOverviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.Board;
import model.GameSession;
import model.tables.Game;
import view.View;

public class MatchOverview extends View {

    @FXML
    private ListView gameListview;
    private ObservableList<Game> gameObservableList;

    private MatchOverviewController _controller;

    @FXML
    private Button _viewModeButton;

    @FXML
    private TextField _searchBar;

    private FilteredList<Game> filteredGames;



    private ObservableList<Game> gameList;
    public MatchOverview(){

    }

    public void loadFinished(){
        try {
            this._controller = this.getController(MatchOverviewController.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameObservableList = FXCollections.observableArrayList();
        gameListview.setItems(gameObservableList);
        gameObservableList.clear();
        gameObservableList.addAll(this._controller.getGames());

        gameListview.setCellFactory(studentListView -> {
            var listViewCell = new ListViewCell();
            listViewCell.setController(this._controller);

            return listViewCell;
        });
        this.gameList = gameObservableList;
    }

    @FXML
    public void filter(){
        System.out.println("ssd");
        String filter = _searchBar.getText();
        this.filteredGames = new FilteredList<>(gameList, s -> true);
        if(filter == null || filter.length() == 0){
            filteredGames.setPredicate(s -> true);
        }
        else{
            filteredGames.setPredicate(s -> {
                return (s.player1.getUsername().contains(filter) || s.player2.getUsername().contains(filter));
            });
        }

        gameListview.setItems(filteredGames);
    }
    // Shows all buttons whe have access to.
    private void showAccessibleButtons(){

    }
}
