package view.MatchOverview;

import controller.MatchOverviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import model.tables.Game;
import view.View;

public class MatchOverview extends View {

    @FXML
    private ListView gameListview;
    private ObservableList<Game> gameObservableList;

    private MatchOverviewController _controller;
    public MatchOverview(){
        this._controller = new MatchOverviewController();
    }

    public void loadFinished(){
        gameObservableList = FXCollections.observableArrayList();
        gameListview.setItems(gameObservableList);
        gameObservableList.clear();
        gameObservableList.addAll(this._controller.getAllGames());
        gameListview.setCellFactory(studentListView -> new ListViewCell());
        ObservableList<String> items = gameListview.getItems();
    }
}
