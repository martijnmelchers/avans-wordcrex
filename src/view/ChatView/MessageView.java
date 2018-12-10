package view.ChatView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import view.View;

public class MessageView extends View {

    @FXML
    private Label MessageLabel;

    @FXML
    private Label MomentLabel;

    void setMessageLabel(String message) {
        MessageLabel.setText(message);
    }

    void setMomentLabel(String moment) {
        MomentLabel.setText(moment);
    }

    @Override
    protected void loadFinished() {
        System.out.println("Load is finished!");
    }
}
