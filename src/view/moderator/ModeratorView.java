package view.moderator;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import view.View;

public class ModeratorView extends View {
    private ListView<String> WordView;

    public ModeratorView(ListView wordView,ObservableList<String> words) {
        WordView = wordView;
        WordView.setItems(words);
        getController().

    }
    public void DrawList(ObservableList<String> words){
        WordView.setItems(words);
    }
}
