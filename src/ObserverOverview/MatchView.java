package ObserverOverview;

import controller.MatchOverviewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.GameSession;
import model.MatchOverviewModel;
import model.tables.Game;

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


    @FXML
    private Pane infoPane;

    private Game match;
    public MatchView(Game match){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlayerGame.fxml"));
        fxmlLoader.setController(this);
        MatchOverviewController controller = new MatchOverviewController();
        this.match = match;
        try
        {
            MatchOverviewModel mod = new MatchOverviewModel();
            MatchOverviewModel.GameScore scores = mod.getPlayerScores(match);

            fxmlLoader.load();
            boolean isMyTurn;

            try {
                 isMyTurn = MatchOverviewModel.isMyTurn(match);
            }
            catch (NullPointerException e){
                isMyTurn = true;
            }

            String player = GameSession.getUsername();
            String player1 = match.getPlayer1().getUsername();
            String player2 = match.getPlayer2().getUsername();
            String enemy  = player1.equals(player) ?  player2 : player1;

            if (match.getGameState().isRequest()) {


                infoPane.getChildren().clear();
                Text inviteTxt = new Text();
                Text inviteStatusTxt = new Text();
                matchPlayButton.setDisable(true);
                matchSurrenderButton.setDisable(true);
                if(GameSession.getUsername().equals(player1)){
                    //Uitnodiging van ons
                    inviteTxt.setText("Uitnodiging naar: " + player2);
                    String antwoord = "reactie: ";
                    switch(match.getAnswer().get_type()){
                        case "accepted": {
                            inviteStatusTxt.setText(antwoord+"Geaccepteerd");
                            matchPlayButton.setDisable(false);
                            matchSurrenderButton.setDisable(false);
                            break;
                        }
                        case "declined": {
                            inviteStatusTxt.setText(antwoord+"Geweigerd");
                            break;
                        }
                        case "unknown": {
                            inviteStatusTxt.setText(antwoord+"Nog geen antwoord gegeven.");
                            break;
                        }
                    }
                }

                else {
                    //Uitnodiging van tegenstander
                    inviteTxt.setText("Uitnodiging van: " + player1);
                }
                
                infoPane.getChildren().add(inviteTxt);
                infoPane.getChildren().add(inviteStatusTxt);
                inviteTxt.setX(5);
                inviteTxt.setY(20);
                inviteStatusTxt.setX(5);
                inviteStatusTxt.setY(35);
            }
            else {
                matchEnemy.setText(enemy);
                matchScore.setText(Integer.toString(scores.player1) + "/" + Integer.toString(scores.player2));
                matchTurn.setText(isMyTurn ? GameSession.getUsername() : enemy);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFinished() { }

    public HBox getAnchor(){ return  this.rootHbox; }

    public Button getMatchPlayButton(){ return this.matchPlayButton; }

    public Button getMatchSurrenderButton(){ return this.matchSurrenderButton; }

    @FXML
    private void onMatchPlay() { System.out.println(match.getGameId()); }
}
