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
    private ListView playerListView;
    @FXML
    private TextField searchBar;
    private ObservableList<String> _filteredPlayers;
    private MatchFixerController _controller;

    @FXML
    private void Search() {
        _filteredPlayers.clear();
        try {
            _filteredPlayers.addAll(this.getController(MatchFixerController.class).SearchPlayers(searchBar.getText()));
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @FXML
    private void RequestGame() {
        try {
            this._controller.RequestGame(playerListView.getSelectionModel().getSelectedItem().toString());

            try {
                this.getController(MatchFixerController.class).navigate("MatchOverview", 620, 769);
            } catch (Exception e) {
                Log.error(e);
            }
        } catch (Exception e) {
            Log.error(new Exception("Er is een fout opgetreden tijdens het maken van het spel!", e), true);
        }

    }

    @Override
    protected void loadFinished() {
        try {
            this._controller = this.getController(MatchFixerController.class);
        } catch (Exception e) {
            Log.error(e, true);
        }
        _filteredPlayers = FXCollections.observableList(new ArrayList<>());
        playerListView.setItems(_filteredPlayers);

        /* Show all players as base */
        _filteredPlayers.addAll(this._controller.SearchPlayers(""));
    }

}
