package view.MatchOverview;

import controller.MatchOverviewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.MatchOverviewModel;
import model.tables.Game;

import java.util.ArrayList;
import java.util.function.Consumer;

class Header {
    private Label _label;
    private VBox _vBox;

    public Header(String headerName) {
        _label = new Label(headerName);
        _label.setMaxWidth(Double.MAX_VALUE);
        _label.setAlignment(Pos.CENTER);
        _label.setFont(MatchOverview.FONT);
        MatchOverview.FillBackground(_label, MatchOverview.LABEL_COLOR);
        _label.setTextFill(MatchOverview.TEXT_COLOR);
        _vBox = new VBox();
    }

    public ArrayList<Node> getContent() {
        var nodes = new ArrayList<Node>();
        nodes.add(_label);
        nodes.add(_vBox);

        return nodes;
    }


    // TODO make the caller of this function decide what the description is and the comment.
    public void addPlayButton(Consumer<Game> onClick, Game game, String comment) {
        createButton(onClick, game, comment, null, null, 0);
    }

    public void addObserverButton(MatchOverviewController controller, Consumer<Game> onClick, ArrayList<Game> games, String comment)
    {
        // programma toont de spelers, de <spelscore> en of een spel <bezig> of <geëindigd> is
        for (Game game : games)
        {
            var title = new Text(game.player1.getUsername() + " - " + game.player2.getUsername() + "\n");
            var description = new Text(game.letterSet.getDescription() + " " + comment);
            MatchOverviewModel.GameScore scores = controller.getPlayerScores(game);
            String score1 = "0";
            String score2 = "0";

            if(scores != null)
            {
                score1 = Integer.toString(scores.player1);
                score2 = Integer.toString(scores.player2);
            }

            createButton(onClick, game, comment, title, description, 40);

            // check what to add
            var textToAdd = new ArrayList<InformationData>();
            if(!game.gameState.isRequest())
            {
                textToAdd.add(new InformationData("Punten:", "Speler 1 = " + score1 + " \nSpeler 2 = " + score2));
            }
            textToAdd.add(new InformationData("Status:", game.gameState.getState()));

            if(game.winner != null && (game.gameState.isFinished() || game.gameState.isResigned()))
            {
                textToAdd.add(new InformationData("Winnaar:", game.winner.getUsername()));
            }

            AddTextFlowToButton(new Insets(0,100,0, 0), textToAdd);
        }
    }

    private void createButton(Consumer<Game> onClick, Game game, String comment, Text gameTitle, Text description, int widthOfTextFlow)
    {
        var hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);

        hBox.setStyle("-fx-border-width: 1 0 1 0; -fx-border-color:#" + Integer.toHexString(MatchOverview.LABEL_COLOR.hashCode()) + ";");
        hBox.setOnMouseClicked(event -> onClick.accept(game));

        // Pane for an image
        var pane = new Pane();
        pane.setPrefHeight(50);
        pane.setPrefWidth(50);
        pane.setMaxHeight(50);
        pane.setMaxWidth(50);

        // Text
        var text1 = gameTitle == null ? new Text(game.player2.getUsername() + " - " + game.letterSet.getDescription() + "\n") : gameTitle;
        text1.setFill(MatchOverview.TEXT_COLOR);
        text1.setFont(MatchOverview.FONT_BOLD);
        var text2 = description == null ? new Text(comment) : description;
        text2.setFill(MatchOverview.TEXT_COLOR);
        text2.setFont(MatchOverview.FONT);
        var textFlow = new TextFlow(text1, text2);
        textFlow.setPadding(new Insets(0, widthOfTextFlow, 0,0));

        MatchOverview.FillBackground(hBox, MatchOverview.BUTTON_COLOR);
        hBox.getChildren().addAll(pane, textFlow);
        _vBox.getChildren().add(hBox);
    }

    // Add a text flow to the last button.
    private void AddTextFlowToButton(Insets margin, ArrayList<InformationData> textToAdd)
    {
        var children = _vBox.getChildren();
        if(children.size() == 0 || children == null)
            throw new RuntimeException("Please add a Button first");

        var button = (HBox)children.get(children.size() - 1);
        button.setPadding(new Insets(2 + margin.getTop(),0 + margin.getRight(), 2 + margin.getBottom(),0 + margin.getLeft()));

        for (InformationData info : textToAdd)
        {
            var head = CreateText(info.getHead());
            var body = CreateText(info.getBody());

            var borderPane = new BorderPane();

            borderPane.setTop(head);
            borderPane.setCenter(body);

            borderPane.setMaxHeight(60);
            borderPane.setMaxWidth(60);

            MatchOverview.FillBackground(borderPane, MatchOverview.LABEL_COLOR);
            borderPane.setStyle("-fx-border-color: #" + Integer.toHexString(MatchOverview.BUTTON_COLOR.hashCode()) + " ;\n" +
                                "    -fx-border-width: 1 ; ");

            button.getChildren().add(borderPane);
            borderPane.setPadding(new Insets(0,2,0,2));
        }

        int btnCount = button.getChildren().size();
        for (Node node : button.getChildren())
        {
            if(node instanceof Region)
            {
                Region region = (Region)node;
                region.prefWidthProperty().bind(button.widthProperty().divide(btnCount));
                region.prefWidthProperty().bind(button.widthProperty().divide(btnCount));
            }
        }
    }

    private Text CreateText(String head) {
        Text text = new Text(head);

        text.setFill(MatchOverview.TEXT_COLOR);
        text.setFont(MatchOverview.FONT_BOLD);

        return text;
    }

    private class InformationData
    {
        public String getHead() {
            return _Head;
        }

        public String getBody() {
            return _body;
        }

        private String _Head;


        private String _body;

        public InformationData(String head, String text)
        {
            _Head = head;
            _body = text;
        }
    }
}
