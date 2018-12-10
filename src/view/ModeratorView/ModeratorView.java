package view.ModeratorView;

import controller.ModeratorController;
import javafx.scene.control.Button;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.View;

import java.util.ArrayList;
import java.util.Arrays;


///size 350x by 660Y zet de grootte als je deze maakt
///
public class ModeratorView extends View {


    public ModeratorView(){
        WordList = FXCollections.observableList(new ArrayList<String>());



    }
    public void initialize(){
        WordView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        WordView.setItems(WordList);


    }

    @FXML private AnchorPane MainPane;
    @FXML private ListView WordView;
    @FXML private Button DenyButton;
    @FXML private Button AddButton;


    private ObservableList<String> WordList;
    private ModeratorController moderatorController;
    private String mode = "pending";



    @FXML
    public void refreshAccepted(){
        mode = "accepted";
        if(WordList.size() < 1) Refresh();
    }
    @FXML
    public void refreshPending(){
        mode = "pending";
        Refresh();
    }
    @FXML
    public void refreshDeclined(){
        mode = "declined";
        Refresh();
    }

    @FXML
    private void Refresh(){
        WordList.clear();

        if(mode == "pending") {
             for (String temp : moderatorController.getSuggestedWords()) {
                WordList.add(temp);
            }
        }
        else if(mode == "declined"){
            for (String temp : moderatorController.getDeclinedWords()) {
                WordList.add(temp);
            }
        }
        else if (mode == "accepted"){
            for (String temp : moderatorController.getAcceptedWords()) {
                WordList.add(temp);
            }
        }
        if (WordList.size() == 0) WordList.add("There are no words pending at the moment") ;


    }
    @FXML
    private void AddWord(){
        if (WordView.getSelectionModel().getSelectedItems().size() == 0) return;
        moderatorController.acceptSuggestedWords(Arrays.copyOf(WordView.getSelectionModel().getSelectedItems().toArray(),
                WordView.getSelectionModel().getSelectedItems().size(),
                String[].class));
        Refresh();
    }
    @FXML
    private void IgnoreWord(){
        if (WordView.getSelectionModel().getSelectedItems().size() == 0) return;
        moderatorController.rejectSuggestedWords(Arrays.copyOf(WordView.getSelectionModel().getSelectedItems().toArray(),
                WordView.getSelectionModel().getSelectedItems().size(),
                String[].class));
        Refresh();
    }

    public void Dispose(){
        Stage Temp = (Stage)WordView.getScene().getWindow();
        moderatorController.close();
        Temp.close();
    }

    @Override
    protected void loadFinished() {
        try {
            this.moderatorController = this.getController(ModeratorController.class);
        } catch (Exception e) {
            Log.error(e);
        }

    }
}
