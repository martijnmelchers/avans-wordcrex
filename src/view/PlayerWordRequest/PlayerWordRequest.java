package view.PlayerWordRequest;

import controller.PlayerWordRequestController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.GameSession;
import model.moderator.GameWord;
import view.View;

import java.util.List;


public class PlayerWordRequest extends View {
    @FXML
    private TextField _text;

    @FXML
    private Label _woordIsVerstuurd;

    @FXML
    private Button _verstuur;

    @FXML
    private Button _terug;

    @FXML
    private ListView sentRequestsListview;

    private PlayerWordRequestController _controller;

    @Override
    protected void loadFinished() {
        try {
            _controller = getController(PlayerWordRequestController.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        _woordIsVerstuurd.setVisible(false);
        addTextLimiter(_text, 15);

        displayRequestedWords();
    }

    @FXML
    private void backButtonPressed()
    {
        _controller.navigate("MatchOverview", true);
    }

    @FXML
    private void sendButtonPressed()
    {
        String word = _text.getText();

        _controller.submitWord(new String[]{word});
        _woordIsVerstuurd.setVisible(true);

        displayRequestedWords();
    }

    private void displayRequestedWords() {
        sentRequestsListview.getItems().clear();
        List<GameWord> requestedWords = _controller.getByPlayerRequestedWords(GameSession.getUsername());

        ObservableList<String> requestedItems = sentRequestsListview.getItems();

        for(GameWord requestedWord : requestedWords) {
            requestedItems.add("" + requestedWord.getWord() + " - " + requestedWord.getState());
        }

    }

    private void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}
