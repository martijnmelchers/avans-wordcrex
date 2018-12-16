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

public class ObserverView extends View{

    @FXML
    private ListView PlayerListView;
    @FXML
    private TextField SearchBar;
    private ObservableList<String> gameview;

    public void Initialize(){
        gameview = FXCollections.observableList(new ArrayList<String>());
        PlayerListView.setItems(gameview);
    }

    @FXML
    private void Search(){
        gameview.clear();
        try {
            gameview.addAll(this.getController(ObserverController.class).getGames(SearchBar.getText()));
        } catch (Exception e) {
            Log.error(e);
        }
    }
    @FXML
    private void WatchGame(){
        try {
            this.getController(ObserverController.class).WatchGame( Integer.parseInt( PlayerListView.getSelectionModel().getSelectedItem().toString().split(" ")[0]));
        } catch (Exception e) {
            Log.error(e);
        }
        try {
            this.getController(ObserverController.class).navigate("BoardView");
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @Override
    protected void loadFinished() {
        Initialize();
        Search();
    }

}
