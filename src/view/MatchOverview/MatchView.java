package view.MatchOverview;

import controller.Controller;
import controller.MatchOverviewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.GameSession;
import model.MatchOverviewModel;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.services.Database;
import model.tables.Game;
import model.tables.Turn;
import view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchView {
    @FXML
    private HBox rootHbox;

    @FXML
    private Text matchScore;

    @FXML
    private Text matchEnemy;

    @FXML
    private Text matchTurn;

    @FXML
    private Button matchPlayButton;

    @FXML
    private Button matchSurrenderButton;



    private Game match;
    public MatchView(Game match){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./PlayerGame.fxml"));
        fxmlLoader.setController(this);
        MatchOverviewController controller = new MatchOverviewController();
        this.match = match;
        try
        {
            MatchOverviewModel mod = new MatchOverviewModel();
            MatchOverviewModel.GameScore scores = mod.getPlayerScores(match);
            fxmlLoader.load();
            boolean isMyTurn = mod.isMyTurn(match);

            String player = GameSession.getUsername();
            String player1 =  match.player1.getUsername();
            String player2 =  match.player2.getUsername();
            String enemy  = player1.equals(player) ?  player2 : player1;

            matchEnemy.setText(enemy);
            matchScore.setText(Integer.toString(scores.player1) + "/" + Integer.toString(scores.player2));
            matchTurn.setText(isMyTurn ? GameSession.getUsername() : enemy);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFinished(){

    }

    public HBox getAnchor(){
        return  this.rootHbox;
    }

    public Button getMatchPlayButton(){
        return this.matchPlayButton;
    }

    public Button getMatchSurrenderButton(){
        return this.matchSurrenderButton;
    }

    @FXML
    private void onMatchPlay() {
        System.out.println(match.getGameID());
    }
}
