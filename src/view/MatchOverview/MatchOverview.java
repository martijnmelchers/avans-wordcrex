package view.MatchOverview;

import controller.BoardController;
import controller.MatchOverviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.Board;
import model.tables.Game;
import view.View;

public class MatchOverview extends View {

    @FXML
    private ListView gameListview;
    private ObservableList<Game> gameObservableList;

    private MatchOverviewController _controller;

    @FXML
    private Button _viewModeButton;
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

        ObservableList<String> items = gameListview.getItems();
    }
    // Shows all buttons whe have access to.
    private void showAccessibleButtons(){

    }
}
