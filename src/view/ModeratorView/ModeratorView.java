package view.ModeratorView;

import controller.ModeratorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.helper.Log;
import view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


///size 350x by 660Y zet de grootte als je deze maakt
///
public class ModeratorView extends View {


    @FXML
    private AnchorPane MainPane;
    @FXML
    private ListView _wordView;
    @FXML
    private Button DenyButton;
    @FXML
    private Button AddButton;

    private ObservableList<String> _wordList;
    private ModeratorController _controller;

    private String mode = "pending";

    public ModeratorView() {
        this._wordList = FXCollections.observableList(new ArrayList<>());


    }

    public void initialize() {
        this._wordView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this._wordView.setItems(this._wordList);
    }

    @FXML
    public void refreshAccepted() {
        this.mode = "accepted";
        if (this._wordList.size() < 1) this.refresh();
    }

    @FXML
    public void refreshPending() {
        this.mode = "pending";
        this.refresh();
    }

    @FXML
    public void refreshDeclined() {
        this.mode = "declined";
        this.refresh();
    }

    @FXML
    private void refresh() {
        this._wordList.clear();

        switch (this.mode) {
            case "pending":
                Collections.addAll(this._wordList, this._controller.getSuggestedWords());
                break;
            case "declined":
                Collections.addAll(this._wordList, this._controller.getDeclinedWords());
                break;
            case "accepted":
                this._wordList.addAll(Arrays.asList(this._controller.getAcceptedWords()));
                break;
        }
        if (this._wordList.size() == 0) this._wordList.add("There are no words pending at the moment");


    }

    @FXML
    private void AddWord() {
        if (this._wordView.getSelectionModel().getSelectedItems().size() == 0) return;
        this._controller.acceptSuggestedWords(Arrays.copyOf(this._wordView.getSelectionModel().getSelectedItems().toArray(),
                this._wordView.getSelectionModel().getSelectedItems().size(),
                String[].class));
        this.refresh();
    }

    @FXML
    private void ignoreWord() {
        if (this._wordView.getSelectionModel().getSelectedItems().size() == 0) return;
        this._controller.rejectSuggestedWords(Arrays.copyOf(this._wordView.getSelectionModel().getSelectedItems().toArray(),
                this._wordView.getSelectionModel().getSelectedItems().size(),
                String[].class));
        this.refresh();
    }

    public void dispose() {
        _controller.navigate("MatchOverview", true);
    }

    @Override
    protected void loadFinished() {
        try {
            this._controller = this.getController(ModeratorController.class);
        } catch (Exception e) {
            Log.error(e);
        }

    }
}
