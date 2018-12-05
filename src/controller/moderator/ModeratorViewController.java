package controller.moderator;

import javafx.scene.control.Button;
import model.moderator.Moderator;
import view.moderator.ModeratorView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class ModeratorViewController {


    public ModeratorViewController(){
        WordList = FXCollections.observableList(new ArrayList<String>());
        WordList.add("1sukerkool");
        WordList.add("2sukerkool");


    }
    public void initialize(){
        WordView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        moderatorView = new ModeratorView(WordView,WordList);

    }


    @FXML private ListView WordView;
    @FXML private Button DenyButton;
    @FXML private Button AddButton;

    private ModeratorView moderatorView;
    private ObservableList<String> WordList;
    private Moderator moderator;
    private String mode = "pending";

    public void setModerator(Moderator moderator) {
        this.moderator = moderator;


    }
    @FXML
    public void refreshAccepted(){
        mode = "accepted";
        Refresh();
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
             for (String temp : moderator.getSuggestedWords()) {
                WordList.add(temp);
            }
        }
        else if(mode == "declined"){
            for (String temp : moderator.getDeclinedWords()) {
                WordList.add(temp);
            }
        }
        else if (mode == "accepted"){
            for (String temp : moderator.getAcceptedWords()) {
                WordList.add(temp);
            }
        }


    }
    @FXML
    private void AddWord(){
        if (WordView.getSelectionModel().getSelectedItems().size() == 0) return;
        moderator.acceptSuggestedWords(Arrays.copyOf(WordView.getSelectionModel().getSelectedItems().toArray(),
                WordView.getSelectionModel().getSelectedItems().size(),
                String[].class));
        Refresh();
    }
    @FXML
    private void IgnoreWord(){
        if (WordView.getSelectionModel().getSelectedItems().size() == 0) return;
        moderator.rejectSuggestedWords(Arrays.copyOf(WordView.getSelectionModel().getSelectedItems().toArray(),
                WordView.getSelectionModel().getSelectedItems().size(),
                String[].class));
        Refresh();
    }

    public void Dispose(){
        Stage Temp = (Stage)WordView.getScene().getWindow();
        moderator.
        Temp.close();
    }

}
