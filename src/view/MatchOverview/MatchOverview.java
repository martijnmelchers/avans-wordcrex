package view.MatchOverview;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import view.View;

public class MatchOverview extends View {


    @FXML
    private ListView gameListview;
    public MatchOverview(){

    }

    public void loadFinished(){
        gameListview.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ListViewCell();
            }
        });
    }
}
