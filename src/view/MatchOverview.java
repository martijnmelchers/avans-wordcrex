package view;

import controller.MatchOverviewController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.tables.Game;

import java.util.ArrayList;


public class MatchOverview extends View
{
    private MatchOverviewController controller;

    @FXML
    private ScrollPane _matchScrollPane;
    private VBox _vBox;

    // NOTE: the person who invites is player1.
    private Header _headerInvite;
    private Header _headerInvitations;
    private Header _headerYourTurn;
    private Header _headerOurturn;

    public MatchOverview()
    {
        _headerInvitations = new Header("Uitnodigingen");
        _headerYourTurn = new Header("Jouw Beurt");
    }

    public void start()
    {
        controller = this.getController(MatchOverviewController.class);

        var invitations = new ArrayList<Game>();
        var yourTurns = new ArrayList<Game>();

        _vBox = new VBox();
        _vBox.setPrefWidth(_matchScrollPane.getWidth());
        _vBox.setPrefHeight(_matchScrollPane.getHeight());
        //_vBox.getChildren().addAll(new Button("test")); TEST THIS

        ArrayList<Game> games = controller.getGames();
        for (Game game : games)
        {
            //String buttonName = game.opponent.getUsername() + " - " + game.letterSet.getDescription();
            //Button gameButton = new Button(buttonName);
            //gameButton.setMaxWidth(Double.MAX_VALUE);
            //buttons.add(gameButton);
            if(game.gameState.isRequest())
            {
                invitations.add(game);
            }
            else if(game.gameState.isPlaying())
            {
                yourTurns.add(game);
            }
        }

        // Invitation
        initiateHeader(_headerInvitations, invitations);

        // Our Turn
        // Their Turn
        initiateHeader(_headerYourTurn, yourTurns);

        //_vBox.getChildren().addAll(buttons);
        _vBox.getChildren().addAll(_headerInvitations.getContent());
        _vBox.getChildren().addAll(_headerYourTurn.getContent());
        _matchScrollPane.setContent(_vBox);
    }

    private void initiateHeader(Header header, ArrayList<Game> games) {
        for(Game game : games)
        {
            String opponentName = game.opponent.getUsername();
            header.addButton(opponentName,
                            "Invited " + opponentName,
                            game.letterSet.getDescription());
        }
    }

    private class Header
    {
        private final Color textColor = Color.WHITE;
        private final Color labelColor = new Color(0.2392, 0.2235, 0.7921, 1);
        private final Color buttonColor = new Color(0.2784, 0.5216, 0.8, 1);


        private Label _label;
        private VBox _vBox;

        public Header(String headerName)
        {
            _label = new Label(headerName);
            _label.setMaxWidth(Double.MAX_VALUE);
            _label.setAlignment(Pos.CENTER);
            fillBackground(_label, labelColor);
            _label.setTextFill(textColor);
            _vBox = new VBox();
        }

        public ArrayList<Node> getContent() {
            var nodes = new ArrayList<Node>();
            nodes.add(_label);
            nodes.add(_vBox);

            return nodes;
        }

        public void addButton(String opponent, String comment, String language)
        {
            var flowPane = new FlowPane();
            flowPane.setRowValignment(VPos.CENTER);
            flowPane.setAlignment(Pos.BASELINE_LEFT);

            var pane = new Pane();
            pane.setPrefHeight(70);
            pane.setPrefWidth(70);

            var text1 = new Text(opponent + " - " + language + "\n");
            text1.setFill(textColor);
            var text2 = new Text(comment);
            text2.setFill(textColor);
            var textFlow = new TextFlow(text1, text2);

            text1.setStyle("-fx-font-weight: bold;");

            fillBackground(flowPane, buttonColor);
            flowPane.getChildren().addAll(pane, textFlow);
            _vBox.getChildren().add(flowPane);
        }

        private void fillBackground(Region node, Color color)
        {
            node.setBackground(new Background(new BackgroundFill(color, null, null)));
        }
    }
}