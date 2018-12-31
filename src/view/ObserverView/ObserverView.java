package view.ObserverView;


import controller.ObserverController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.helper.Log;
import view.View;

import java.util.ArrayList;

public class ObserverView extends View {

    @FXML
    private ListView PlayerListView;
    @FXML
    private TextField SearchBar;
    private ObservableList<String> gameview;

    private void initialize() {
        this.gameview = FXCollections.observableList(new ArrayList<String>());
        this.PlayerListView.setItems(this.gameview);
    }

    @FXML
    private void search() {
        this.gameview.clear();
        try {
            this.gameview.addAll(this.getController(ObserverController.class).getGames(this.SearchBar.getText()));
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @FXML
    private void watchGame() {
        try {
            this.getController(ObserverController.class).WatchGame(Integer.parseInt(this.PlayerListView.getSelectionModel().getSelectedItem().toString().split(" ")[0]));
        } catch (Exception e) {
            Log.error(e);
        }
        try {
            this.getController(ObserverController.class).navigate("BoardView", true);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @Override
    protected void loadFinished() {
        this.initialize();
        this.search();
    }

}
