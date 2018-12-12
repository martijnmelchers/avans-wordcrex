package view.MatchOverview;

import controller.App;
import controller.MatchOverviewController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
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
    }

    private void initiateYourTurnHeader(ArrayList<Game> games) {
        for (Game game : games) {
            _headerYourTurn.addPlayButton(controller, this::onYourTurnClick, game, "Speel je beurt");
        }
    }

    private void initiateTheirTurnHeader(ArrayList<Game> games) {
        for (Game game : games) {
            String opponentName = game.player2.getUsername();

            _headerTheirTurn.addPlayButton(controller, this::onTheirTurnClick, game, opponentName + " moet zijn beurt nog spelen.");
        }
    }

    private void onInvitationClick(Game game)
    {
        System.out.println(game.getGameID());
    }

    private void onYourTurnClick(Game game)
    {
        System.out.println(game.getGameID());
    }

    private void onTheirTurnClick(Game game)
    {
        System.out.println(game.getGameID());
    }

    private void onObserverGameClick(Game game)
    {
        System.out.println(game.getGameID());
    }

    protected void filterObserverGames(String currentGamesToSearch) {
        DestroyViewList();

        if(currentGamesToSearch.length() == 0) //Reset the view if nothing is searched.
        {
            if(_viewMode == ViewMode.Observer)
            {
                ChangeToObserverMode(null);
            }
            else
            {
                ChangeToPlayMode(null);
            }
            return;
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
    }

    private void ChangeToPlayMode(ArrayList<Game> foundGames) {
        _viewModeButton.setText("Observer Mode");

        _headerInvitations = new Header("Uitnodigingen");
        _headerYourTurn = new Header("Jouw Beurt");
        _headerTheirTurn = new Header("Hun Beurt");

        var invitations = new ArrayList<Game>();
        var yourTurns = new ArrayList<Game>();
        var theirTurns = new ArrayList<Game>();

        ArrayList<Game> games = foundGames != null ? foundGames : controller.getGames();
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
    private void switchViewMode() {
        DestroyViewList();

        if (_viewMode == ViewMode.Play) {
            _viewMode = ViewMode.Observer;
            ChangeToObserverMode(null);
        } else if (_viewMode == ViewMode.Observer) {
            _viewMode = ViewMode.Play;
            ChangeToPlayMode(null);
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
