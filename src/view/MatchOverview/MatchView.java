package view.MatchOverview;

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
    private Text turnLabel;


    @FXML
    private Text scoreLabel;

    @FXML
    private Button matchPlayButton;

    @FXML
    private Button matchSurrenderButton;


    @FXML
    private Pane infoPane;

    private Game match;

    public MatchView(Game match) {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("./PlayerGame.fxml"));
        fxmlLoader.setController(this);

        this.match = match;

        try {
            var mod = new MatchOverviewModel();
            MatchOverviewModel.GameScore scores = mod.getPlayerScores(match);

            fxmlLoader.load();

            var player = GameSession.getUsername();

            var player1 = match.getPlayer1().getUsername();
            var player2 = match.getPlayer2().getUsername();

            var enemy = player1.equals(player) ? player2 : player1;

            if (match.getGameState().isRequest()) {
                this.infoPane.getChildren().clear();

                var inviteTxt = new Text();
                var inviteStatusTxt = new Text();

                this.matchPlayButton.setDisable(true);
                this.matchSurrenderButton.setDisable(true);

                if (GameSession.getUsername().equals(player1)) {
                    //Uitnodiging van ons
                    inviteTxt.setText("Uitnodiging naar: " + player2);

                    var answer = "reactie: ";

                    switch (match.getAnswer().get_type()) {
                        case "accepted": {
                            inviteStatusTxt.setText(answer + "Geaccepteerd");
                            this.matchPlayButton.setDisable(false);
                            this.matchSurrenderButton.setDisable(false);
                            break;
                        }
                        case "declined": {
                            inviteStatusTxt.setText(answer + "Geweigerd");
                            break;
                        }
                        case "unknown": {
                            inviteStatusTxt.setText(answer + "Nog geen antwoord gegeven.");
                            break;
                        }
                    }
                } else {
                    //Uitnodiging van tegenstander
                    inviteTxt.setText("Uitnodiging van: " + player1);
                }

                this.infoPane.getChildren().add(inviteTxt);
                this.infoPane.getChildren().add(inviteStatusTxt);

                inviteTxt.setX(5);
                inviteTxt.setY(20);

                inviteStatusTxt.setX(5);
                inviteStatusTxt.setY(35);
            } else {

                this.matchEnemy.setText(enemy);
                this.matchScore.setText(Integer.toString(scores.player1) + "/" + Integer.toString(scores.player2));
                this.matchTurn.setText(MatchOverviewModel.isMyTurn(match) ? GameSession.getUsername() : enemy);

                if(match.getGameState().isFinished() || match.getGameState().isResigned()){

                    this.scoreLabel.setText("Eindscore: ");
                    this.turnLabel.setText("Winnaar: ");
                    this.matchTurn.setText(match.getWinner().getUsername());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFinished() {
    }

    public HBox getAnchor() {
        return this.rootHbox;
    }

    public Button getMatchPlayButton() {
        return this.matchPlayButton;
    }

    public Button getMatchSurrenderButton() {
        return this.matchSurrenderButton;
    }

    @FXML
    private void onMatchPlay() {
        System.out.println(this.match.getGameId());
    }
}
