package view.MatchOverview;
import controller.MatchOverviewController;
import javafx.scene.control.ListCell;
import model.matchfixer.Matchfixer;
import model.tables.Game;

public final class ListViewCell extends ListCell<Game> {

    @Override
    public void updateItem(Game game, boolean empty)
    {
        super.updateItem(game,empty);
        if(empty){
            setGraphic(null);
        }
        else{
            MatchView view = new MatchView(game);
            setGraphic(view.getAnchor());
        }
    }
}
