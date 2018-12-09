package view.ChatView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import view.View;

public class MessageView extends View {

    @FXML
    private Label MessageLabel;

    @FXML
    private Label MomentLabel;

    public void setMessageLabel(String message) {
        MessageLabel.setText(message);
    }

    public void setMomentLabel (String moment) {
        MomentLabel.setText(moment);
    }

    @Override
    protected void loadFinished() {
        System.out.println("Load is finished!");
    }
}
