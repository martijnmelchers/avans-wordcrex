package view.MatchOverview;

import controller.App;
import controller.MatchOverviewController;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Scale;
import model.tables.Game;
import view.View;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.function.Consumer;


// TODO:
// Show everything as observer.
//      Both player names should be shown.
//      Both score should be shown.
//          If there is a winner, the winner's score should be shown.
// Show score on the games.
//
public class MatchOverview extends View {
    private enum ViewMode {
        Play,
        Observer
    }

    private MatchOverviewController controller;

    @FXML
    private VBox _content;

    @FXML
    private FlowPane _toolBar;

    @FXML
    private TextField _searchBar;

    @FXML
    private ScrollPane _matchScrollPane;

    @FXML
    private Button _viewModeButton;

    private VBox _vBox;

    private Header _headerInvite;
    private Header _headerInvitations;
    private Header _headerYourTurn;
    private Header _headerTheirTurn;

    private ViewMode _viewMode = ViewMode.Play;


    public MatchOverview() {
        InputStream isBold = App.class.getResourceAsStream("/Fonts/Trueno/TruenoBd.otf");
        fontBold = Font.loadFont(isBold, 12.0);
        InputStream is = App.class.getResourceAsStream("/Fonts/Trueno/TruenoLt.otf");
        font = Font.loadFont(is, 12.0);

        _headerInvitations = new Header("Uitnodigingen");
        _headerYourTurn = new Header("Jouw Beurt");
        _headerTheirTurn = new Header("Hun Beurt");


    }

    @Override
    public void start() {
        controller = this.getController(MatchOverviewController.class);

        var invitations = new ArrayList<Game>();
        var yourTurns = new ArrayList<Game>();
        var theirTurns = new ArrayList<Game>();

        _vBox = new VBox();

        ArrayList<Game> games = controller.getGames();
        for (Game game : games) {
            if (game.gameState.isRequest()) {
                invitations.add(game);
            } else if (game.gameState.isPlaying()) {
                // Differ in your turns
                if (currentTurnHasAction(game) && !player2TurnHasAction(game)) {
                    theirTurns.add(game);
                } else // Their turn played(and not yours)
                {
                    yourTurns.add(game);
                }
            }
        }
        // Invitation
        initiateInvitationHeader(invitations);

        // Our Turn
        initiateYourTurnHeader(yourTurns);

        // Their Turn
        initiateTheirTurnHeader(theirTurns);

        if (invitations.size() != 0)
            _vBox.getChildren().addAll(_headerInvitations.getContent());

        if (yourTurns.size() != 0)
            _vBox.getChildren().addAll(_headerYourTurn.getContent());

        if (theirTurns.size() != 0)
            _vBox.getChildren().addAll(_headerTheirTurn.getContent());

        fillBackground(_vBox, labelColor);
        fillBackground(_matchScrollPane, labelColor);

        _matchScrollPane.setContent(_vBox);

        // Add to application.
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                ScaleScreen();
            }
        }.start();

        _searchBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchTextChanged(observable, oldValue, newValue);
            }
        });
    }

    private void ScaleScreen()
    {
        final double newWidth = scene.getWidth();
        final double newHeight = scene.getHeight();

        final double ratio = 1.2;

        double scaleFactor =
                newWidth / newHeight > ratio ? newHeight / 600 : newWidth / 800;

        if (scaleFactor > 1) {
            Scale scale = new Scale(scaleFactor, scaleFactor);
            scale.setPivotX(0);
            scale.setPivotY(0);
            scene.getRoot().getTransforms().setAll(scale);

            _content.setPrefWidth(newWidth / scaleFactor);
            _content.setPrefHeight(newHeight / scaleFactor);
        } else {
            _content.setPrefWidth(Math.max(800, newWidth));
            _content.setPrefHeight(Math.max(600, newHeight));
        }
    }

    private boolean player2TurnHasAction(Game game) {
        return controller.currentTurnPlayer2HasAction(game);
    }

    private boolean currentTurnHasAction(Game game) {
        return controller.currentTurnHasAction(game);
    }

    private void initiateInvitationHeader(ArrayList<Game> games) {
        for (Game game : games) {
            String opponentName = game.player2.getUsername();
            _headerInvitations.addPlayButton(this::onInvitationClick, game, "Uitdaging naar " + opponentName + " gestuurd");
        }
    }

    private void initiateYourTurnHeader(ArrayList<Game> games) {
        for (Game game : games) {
            _headerYourTurn.addPlayButton(this::onYourTurnClick, game, "Speel je beurt");
        }
    }

    private void initiateTheirTurnHeader(ArrayList<Game> games) {
        for (Game game : games) {
            String opponentName = game.player2.getUsername();

            _headerTheirTurn.addPlayButton(this::onTheirTurnClick, game, opponentName + " moet zijn beurt nog spelen.");
        }
    }

    private void onInvitationClick(Game game) {
        System.out.println(game.getGameID());
    }

    private void onYourTurnClick(Game game) {
        System.out.println(_matchScrollPane.getHeight());
    }

    private void onTheirTurnClick(Game game) {
        System.out.println(game.getGameID());
    }

    @FXML
    private void switchViewMode() {
        DestroyViewList();
        if (_viewMode == ViewMode.Play) {
            _viewMode = ViewMode.Observer;
            ChangeToObserverMode();
        } else if (_viewMode == ViewMode.Observer) {
            _viewMode = ViewMode.Play;
            ChangeToPlayMode();
        }
    }

    private void DestroyViewList() {
        DestroyHeader(_headerInvite);
        DestroyHeader(_headerInvitations);
        DestroyHeader(_headerYourTurn);
        DestroyHeader(_headerTheirTurn);
    }

    private void DestroyHeader(Header header)
    {
        if(header != null)
        {
            _vBox.getChildren().removeAll(header.getContent());
        }
    }

    private void ChangeToPlayMode() {
        _viewModeButton.setText("Observer Mode");
    }

    private void ChangeToObserverMode() {
        _viewModeButton.setText("Speel Mode");
    }

    private void searchTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue)
    {

    }

    private class Header {
        private Label _label;
        private VBox _vBox;

        public Header(String headerName) {
            _label = new Label(headerName);
            _label.setMaxWidth(Double.MAX_VALUE);
            _label.setAlignment(Pos.CENTER);
            _label.setFont(font);
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

        public void addPlayButton(Consumer<Game> onClick, Game game, String comment) {
            var flowPane = new FlowPane();
            flowPane.setRowValignment(VPos.CENTER);
            flowPane.setAlignment(Pos.BASELINE_LEFT);

            flowPane.setStyle("-fx-border-width: 1 0 1 0; -fx-border-color:#" + Integer.toHexString(labelColor.hashCode()) + ";");
            flowPane.setOnMouseClicked(event -> onClick.accept(game));

            var pane = new Pane();
            pane.setPrefHeight(50);
            pane.setPrefWidth(50);

            var text1 = new Text(game.player2.getUsername() + " - " + game.letterSet.getDescription() + "\n");
            text1.setFill(textColor);
            text1.setFont(fontBold);
            var text2 = new Text(comment);
            text2.setFill(textColor);
            text2.setFont(font);
            var textFlow = new TextFlow(text1, text2);


            fillBackground(flowPane, buttonColor);
            flowPane.getChildren().addAll(pane, textFlow);
            _vBox.getChildren().add(flowPane);
        }
    }

    private static Color textColor = Color.web("#ecf0f1");
    private static Color labelColor = Color.web("#2980b9");
    private static Color buttonColor = Color.web("#3498db");

    private static Font font;
    private static Font fontBold;

    private static void fillBackground(Region node, Color color) {
        node.setBackground(new Background(new BackgroundFill(color, null, null)));
    }
}