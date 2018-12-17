package view.MatchInvitationView;

import controller.MatchFixerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.helper.Log;
import view.View;

import java.util.ArrayList;

public class MatchInvitationView extends View {

    @FXML
    private ListView _playerListView;
    @FXML
    private TextField _searchBar;
    private ObservableList<String> _playerView;

    private MatchFixerController _controller;

    private void initialize() {
        try {
            this._controller = this.getController(MatchFixerController.class);
        } catch (Exception e) {
            Log.error(e);
        }
        this._playerView = FXCollections.observableList(new ArrayList<>());
        this._playerListView.setItems(this._playerView);
        this._controller.fetchCache();
        this.search();
    }

    @FXML
    private void search() {
        this._playerView.clear();
        try {
            this._playerView.addAll(this._controller.searchPlayers(this._searchBar.getText()));
        } catch (Exception e) {
            Log.error(e);
        }
    }
    @FXML
    private void requestGame() {
        try {
            this._controller.RequestGame(this._playerListView.getSelectionModel().getSelectedItem().toString());
        } catch (Exception e) {
            Log.error(e);
        }
        try {
            this._controller.navigate("MatchOverview", 620, 769);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @FXML
    private void home(){
        _controller.navigate("MatchOverview");
    }

    @Override
    protected void loadFinished() {
        this.initialize();
    }

}
