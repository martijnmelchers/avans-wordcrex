package ObserverOverview;

import controller.MatchOverviewController;
import javafx.scene.control.ListCell;
import model.GameSession;
import model.tables.Game;

public final class ListViewCell extends ListCell<Game> {

    private MatchOverviewController _controller;

    @Override
    public void updateItem(Game game, boolean empty) {
        super.updateItem(game, empty);
        if (empty) {
            //TODO: headers can be implemented this way.
            setGraphic(null);
        } else {
            MatchView view = new MatchView(game);
            setGraphic(view.getAnchor());

            if (game.getGameState().isRequest() && !game.getAnswer().get_type().equals("accepted")) {
                if (!game.getPlayer1Username().equals(GameSession.getUsername())) {
                    view.getMatchSurrenderButton().setDisable(false);
                    view.getMatchPlayButton().setDisable(false);
                }
                view.getMatchPlayButton().setText("Accepteren");
                view.getMatchSurrenderButton().setText("Weigeren");
            }
            view.getMatchPlayButton().setOnAction((e) -> {
                GameSession.setGame(game);

                if (game.getGameState().isRequest() && !game.getAnswer().get_type().equals("accepted") && !game.getPlayer1Username().equals(GameSession.getUsername())) {
                    this._controller.acceptInvite(game);
                    System.out.println("Invite accepted");
                } else {
                    try {
                        this._controller.start();
                        this._controller.navigate("BoardView");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            view.getMatchSurrenderButton().setOnAction((e) -> {
                this._controller.surrender(game);
            });

        }
    }


    public void setController(MatchOverviewController controller) {
        _controller = controller;
    }
}
