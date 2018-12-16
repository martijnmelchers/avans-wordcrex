package view.MatchInvitationview;

import controller.MatchFixerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.helper.Log;
import view.View;

import java.util.ArrayList;

public class MatchInvitationview extends View{

    @FXML
    private ListView PlayerListView;
    @FXML
    private TextField SearchBar;
    private ObservableList<String> playerView;

    public void Initialize(){
        playerView = FXCollections.observableList(new ArrayList<String>());
        PlayerListView.setItems(playerView);
    }

    @FXML
    private void Search(){
        playerView.clear();
        try {
            playerView.addAll(this.getController(MatchFixerController.class).SearchPlayers(SearchBar.getText()));
        } catch (Exception e) {
            Log.error(e);
        }
    }
    @FXML
    private void RequestGame(){
        try {
            this.getController(MatchFixerController.class).RequestGame(PlayerListView.getSelectionModel().getSelectedItem().toString());
        } catch (Exception e) {
            Log.error(e);
        }
        try {
            this.getController(MatchFixerController.class).navigate("MatchOverview");
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @Override
    protected void loadFinished() {
        Initialize();
    }

}
