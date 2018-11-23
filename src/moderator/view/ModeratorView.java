package moderator.view;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class ModeratorView {
    private ListView<String> WordView;

    public ModeratorView(ListView wordView,ObservableList<String> words) {
        WordView = wordView;
        WordView.setItems(words);
    }
    public void DrawList(ObservableList<String> words){
        WordView.setItems(words);
    }
}
