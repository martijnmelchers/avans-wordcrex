package Moderator.Controller;

import Moderator.Model.Moderator;
import Moderator.View.ModeratorView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeratorViewController {


    public ModeratorViewController(){
        WordList = FXCollections.observableList(new ArrayList<String>());
        WordList.add("KEWL");


    }
    public void initialize(){
        WordView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        moderatorView = new ModeratorView(WordView,WordList);

    }


    @FXML private ListView WordView;


    private ModeratorView moderatorView;
    private ObservableList<String> WordList;
    private Moderator moderator;

    public void setModerator(Moderator moderator) {
        this.moderator = moderator;


    }

    @FXML
    private void Refresh(){
        WordList.clear();
        for(String temp: moderator.getSuggestedWords()){
            WordList.add(temp);
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
        Temp.close();
    }

}
