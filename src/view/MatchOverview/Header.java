package view.MatchOverview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

    public void addInformationToButton(MatchOverview view, Consumer<Game> onClick, ArrayList<Game> games, String comment)
    {
        // programma toont de spelers, de <spelscore> en of een spel <bezig> of <geëindigd> is
        for (Game game : games)
        {
            var title = new Text(game.player1.getUsername() + " - " + game.player2.getUsername() + "\n");
            var description = new Text(game.letterSet.getDescription() + " " + comment);
            GameScore scores = getPlayerScores(game);
            String score1 = "0";
            String score2 = "0";

            if(scores != null)
            {
                score1 = Integer.toString(scores.player1);
                score2 = Integer.toString(scores.player2);
            }

            createButton(onClick, game, comment, title, description, 20);

            var textToAdd = new ArrayList<InformationData>();
            textToAdd.add(new InformationData("Score:", "Player 1 = " + score1 + " \nPlayer 2 = " + score2));
            AddTextFlowToButton(new Insets(0,0,0, 15), textToAdd);
        }
    }

    private void createButton(Consumer<Game> onClick, Game game, String comment, Text gameTitle, Text description, int widthOfTextFlow)
    {
        var flowPane = new FlowPane();
        flowPane.setRowValignment(VPos.CENTER);
        flowPane.setAlignment(Pos.BASELINE_LEFT);

        flowPane.setStyle("-fx-border-width: 1 0 1 0; -fx-border-color:#" + Integer.toHexString(MatchOverview.LABEL_COLOR.hashCode()) + ";");
        flowPane.setOnMouseClicked(event -> onClick.accept(game));

        // Pane for an image
        var pane = new Pane();
        pane.setPrefHeight(50);
        pane.setPrefWidth(50);

        // Text
        var text1 = gameTitle == null ? new Text(game.player2.getUsername() + " - " + game.letterSet.getDescription() + "\n") : gameTitle;
        text1.setFill(MatchOverview.TEXT_COLOR);
        text1.setFont(MatchOverview.FONT_BOLD);
        var text2 = description == null ? new Text(comment) : description;
        text2.setFill(MatchOverview.TEXT_COLOR);
        text2.setFont(MatchOverview.FONT);
        var textFlow = new TextFlow(text1, text2);
        textFlow.setPadding(new Insets(0, widthOfTextFlow, 0,0));

        MatchOverview.FillBackground(flowPane, MatchOverview.BUTTON_COLOR);
        flowPane.getChildren().addAll(pane, textFlow);
        _vBox.getChildren().add(flowPane);
    }

    // Add a text flow to the last button.
    private void AddTextFlowToButton(Insets margin, ArrayList<InformationData> textToAdd)
    {
        var children = _vBox.getChildren();
        if(children.size() == 0 || children == null)
            throw new RuntimeException("Please add a Button first");

        FlowPane button = (FlowPane)children.get(children.size() - 1);
        button.setPadding(new Insets(2,0,2,0));

        for (InformationData info : textToAdd)
        {
            var head = CreateText(info.getHead());
            var body = CreateText(info.getBody());

            var borderPane = new BorderPane();

//            head.setTextAlignment(TextAlignment.CENTER);
//            body.setTextAlignment(TextAlignment.CENTER);

            borderPane.setTop(head);
            borderPane.setCenter(body);

            MatchOverview.FillBackground(borderPane, MatchOverview.LABEL_COLOR);

            button.getChildren().add(borderPane);
            borderPane.setPadding(new Insets(0,2,0,2));
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

    // get turns from database query and use the game id.
    // has to be done in Model.
    private GameScore getPlayerScores(Game game) {
        GameScore score = new GameScore();

        ArrayList<Integer> player1 = new ArrayList<>();
        ArrayList<Integer> player2 = new ArrayList<>();

        return score;
    }

    class GameScore
    {
        public int player1;
        public int player2;
    }
}
