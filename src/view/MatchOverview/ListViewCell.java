package view.MatchOverview;
import controller.App;
import controller.MatchOverviewController;
import javafx.application.Application;
import javafx.scene.control.ListCell;
import model.GameSession;
import model.matchfixer.Matchfixer;
import model.tables.Game;

public final class ListViewCell extends ListCell<Game> {

    private MatchOverviewController _controller;
    @Override
    public void updateItem(Game game, boolean empty)
    {
        super.updateItem(game,empty);
        if(empty){
            //TODO: headers can be implemented this way.
            setGraphic(null);
        }
        else{
            MatchView view = new MatchView(game);
            setGraphic(view.getAnchor());

            view.getMatchPlayButton().setOnAction((e) -> {
                GameSession.setGame(game);
                try{
                    this._controller.navigate("BoardView");
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            });

            view.getMatchSurrenderButton().setOnAction((e) -> {
                System.out.println(game.getGameID());
            });
        }
    }


    public void setController(MatchOverviewController controller){
        _controller = controller;
    }
}
