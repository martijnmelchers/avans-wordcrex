package view.MatchOverview;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.tables.Game;

import java.io.IOException;

public class MatchView {
    @FXML
    private AnchorPane rootAnchor;

    @FXML
    private Text matchScore;

    @FXML
    private Text matchEnemy;

    @FXML
    private Text matchTurn;

    public MatchView(Game match){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./PlayerGame.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
            matchScore.setText(match.gameState.getState());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public AnchorPane getAnchor(){
        return  this.rootAnchor;
    }
}
