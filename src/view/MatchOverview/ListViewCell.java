package view.MatchOverview;

import controller.MatchOverviewController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import model.GameSession;
import model.helper.Log;
import model.tables.Game;

public final class ListViewCell extends ListCell<Game> {

    private MatchOverviewController _controller;

    private MatchOverview _matchOverview;

    @Override
    public void updateItem(Game game, boolean empty) {
        super.updateItem(game, empty);
        if (empty) {
            this.setGraphic(null);
        } else {

            String player = GameSession.getUsername();
            String player1 = game.getPlayer1().getUsername();
            String player2 = game.getPlayer2().getUsername();
            String enemy = player1.equals(player) ? player2 : player1;
            MatchView view = new MatchView(game);
            this.setGraphic(view.getAnchor());


            // We change the buttontext when the entry is a request
            if (game.getGameState().isRequest() && !game.getAnswer().get_type().equals("accepted")) {
                if (!game.getPlayer1Username().equals(GameSession.getUsername())) {
                    view.getMatchSurrenderButton().setDisable(false);
                    view.getMatchPlayButton().setDisable(false);
                }
                view.getMatchPlayButton().setText("Accepteren");
                view.getMatchSurrenderButton().setText("Weigeren");
            }

            // Disable the buttons when the game has ended
            if (game.getGameState().isFinished() || game.getGameState().isResigned()) {
                view.getMatchSurrenderButton().setDisable(true);
                view.getMatchPlayButton().setDisable(true);
            }


            view.getMatchPlayButton().setOnAction((e) -> {
                GameSession.setGame(game);


                // onAction handler for request
                if (game.getGameState().isRequest() && !game.getAnswer().get_type().equals("accepted") && !game.getPlayer1Username().equals(GameSession.getUsername())) {
                    this._controller.acceptInvite(game);
                    this._matchOverview.renderGames();
                }

                // onAction handler for other entries
                else {
                    try {
                        this._controller.start();
                        this._controller.navigate("BoardView", true);
                    } catch (Exception ex) {
                        Log.error(ex);
                    }
                }
            });

            view.getMatchSurrenderButton().setOnAction((e) -> {

                // onAction handler for request
                if (game.getGameState().isRequest() && !game.getAnswer().get_type().equals("accepted") && !game.getPlayer1Username().equals(GameSession.getUsername())) {
                    this._controller.declineInvite(game);
                }

                // onAction handler for other entries
                else {
                    var alert = new Alert(Alert.AlertType.CONFIRMATION, "Weet je het zeker?");

                    alert.setHeaderText("Spel met " + enemy + " opgeven: ");
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.OK)
                        this._controller.surrender(game);

                }

                this._matchOverview.renderGames();
            });

        }
    }


    public void setController(MatchOverviewController controller) {
        this._controller = controller;
    }

    // We need to do this to be able to refresh the list on the view.
    public void setMatchOverview(MatchOverview overview) {
        this._matchOverview = overview;
    }
}
