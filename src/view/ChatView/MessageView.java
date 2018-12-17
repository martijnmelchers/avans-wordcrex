package view.ChatView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import view.View;

public class MessageView extends View {

    @FXML
    private Label _messageLabel;

    @FXML
    private Label _momentLabel;

    @FXML
    private VBox _messageVBox;

    void setMessageLabel(String message) {
        this._messageLabel.setText(message);
    }

    void setMomentLabel(String moment) {
        this._momentLabel.setText(moment);
    }

    void setMessageAlignment(int alignment) {
        if (alignment == 0) {
            this._messageVBox.setStyle("-fx-background-color: #207BCF");
        } else {
            this._messageVBox.setStyle("-fx-background-color: purple");
        }
    }

    @Override
    protected void loadFinished() {

    }
}
