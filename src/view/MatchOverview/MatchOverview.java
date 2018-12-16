package view.MatchOverview;

import controller.MatchOverviewController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.tables.Game;
import view.View;

import java.io.InputStream;
import java.util.ArrayList;


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
    private FilterGameView _filterGameView;

    @FXML
    private ScrollPane _matchScrollPane;

    @FXML
    private Button _viewModeButton;

    private VBox _vBox;

    private Header _headerInvite;
    private Header _headerInvitations;
    private Header _headerYourTurn;
    private Header _headerTheirTurn;
    private Header _headerObserver;

    private ViewMode _viewMode = ViewMode.Play;


    public MatchOverview() {
        InputStream isBold = App.class.getResourceAsStream("/Fonts/Trueno/TruenoBd.otf");
        FONT_BOLD = Font.loadFont(isBold, 12.0);
        InputStream is = App.class.getResourceAsStream("/Fonts/Trueno/TruenoLt.otf");
        FONT = Font.loadFont(is, 12.0);
    }

    @Override
    protected void loadFinished() {
        try {
            controller = this.getController(MatchOverviewController.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderGames();
    }

    private void renderGames(){
        gameObservableList = FXCollections.observableArrayList();
        gameObservableList1 = FXCollections.observableArrayList();
        gameObservableList2 = FXCollections.observableArrayList();
        gameListview.setItems(gameObservableList);
        gameListview1.setItems(gameObservableList1);
        gameListview2.setItems(gameObservableList2);
        gameObservableList.clear();
        gameObservableList1.clear();
        gameObservableList2.clear();

        List<Game> games = this._controller.getGames();

        for (var game : games) {
            switch (game.getGameState().getState()) {
                case "request": {
                    gameObservableList.add(game);
                    break;
                }

        _filterGameView = new FilterGameView(this, _searchBar);

        // Check the role of the player.
        if(!checkObserverRoll(controller.getPlayerRoles()))
        {
            _viewModeButton.setVisible(false);
        }

        _vBox = new VBox();

        FillBackground(_vBox, LABEL_COLOR);
        FillBackground(_matchScrollPane, LABEL_COLOR);

        ChangeToPlayMode(null);

        // Add to application.
        new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {

                ScaleScreen(_content);
                _filterGameView.updateTimer(now - lastUpdate);
                lastUpdate = now;
            }
        }.start();
    }

    private boolean checkObserverRoll(ArrayList<String> playerRoles) {
        for(String string : playerRoles)
        {
            if(string.equals("observer"))
                return true;
        }

        return false;
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
            _headerInvitations.addPlayButton(controller, this::onInvitationClick, game, "Uitdaging naar " + opponentName + " gestuurd");
        }
        else{
            filteredGames.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }
    }

    private void initiateTheirTurnHeader(ArrayList<Game> games) {
        for (Game game : games) {
            String opponentName = game.player2.getUsername();

            _headerTheirTurn.addPlayButton(controller, this::onTheirTurnClick, game, opponentName + " moet zijn beurt nog spelen.");
        }
        else{
            filteredGames1.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        if(_viewMode == ViewMode.Observer)
        {
            ChangeToObserverMode(controller.searchForAllGamesAsObserver(currentGamesToSearch));
        }
        else
        {
            // Make model search for games of current player.
            ChangeToPlayMode(controller.searchForAllGamesAsPlayer(currentGamesToSearch));
        }
        else{
            filteredGames2.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
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

        _matchScrollPane.setContent(_vBox);
        //
    }

    private void ChangeToObserverMode(ArrayList<Game> foundGames) {
        _viewModeButton.setText("Speel Mode");

        _headerObserver = new Header("Spellen");

        _headerObserver.addObserverButton(controller, this::onObserverGameClick, foundGames != null ? foundGames : controller.getAllGames(), " replace this");

        _vBox.getChildren().addAll(_headerObserver.getContent());

        _matchScrollPane.setContent(_vBox);
    }

    @FXML
    private void invitationView(){

        try{
            this._controller.navigate("MatchInvitationView");
        }
    }

    private void DestroyViewList() {
        _headerInvite = null;
        _headerInvitations = null;
        _headerYourTurn = null;
        _headerTheirTurn = null;
        _headerObserver = null;

        _vBox.getChildren().clear();
    }

    protected static Color TEXT_COLOR = Color.web("#ecf0f1");
    protected static Color LABEL_COLOR = Color.web("#2980b9");
    protected static Color BUTTON_COLOR = Color.web("#3498db");

    protected static Font FONT;
    protected static Font FONT_BOLD;

    protected static void FillBackground(Region node, Color color) {
        node.setBackground(new Background(new BackgroundFill(color, null, null)));
    }
}
