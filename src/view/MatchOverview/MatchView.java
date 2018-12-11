package view.MatchOverview;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MatchView {


    @FXML
    private AnchorPane anchor;


    public MatchView(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./test.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AnchorPane getAnchor(){
        return  this.anchor;
    }
}
