package view.ChatView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import view.View;

public class MessageView extends View {

    @FXML
    private Label MessageLabel;

    @FXML
    private Label MomentLabel;

    @FXML
    private VBox MessageVbox;

    void setMessageLabel(String message) {
        MessageLabel.setText(message);
    }

    void setMomentLabel(String moment) {
        MomentLabel.setText(moment);
    }

    void setMessageAlignment (int alignment) {
        if (alignment == 0) {
            MessageVbox.setStyle("-fx-background-color: #207BCF");
        } else {
            MessageVbox.setStyle("-fx-background-color: purple");
        }
    }

    @Override
    protected void loadFinished() {
        System.out.println("Load is finished!");
    }
}
