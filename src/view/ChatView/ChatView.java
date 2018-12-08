package view.ChatView;

import controller.ChatController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.tables.Chatline;
import view.View;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ChatView extends View {

    private ChatController _controller = new ChatController();

    @FXML
    private Pane messagesVbox;

    @FXML
    private ScrollPane MessagesScrollpane;

    @FXML
    private Label NameLabel;

    @FXML
    private TextField messageField;


    public void initialize() {
        displayOpponentsName();
        displayMessages();
    }

    // function for displaying messages
    public void displayMessages() {
        messagesVbox.getChildren().clear();

        ArrayList<Chatline> chatlines = _controller.getChatlines(1);

        for (Chatline chatline : chatlines) {
            VBox messageVBox = new VBox();
            messageVBox.getStyleClass().add("Chat-Message-Container");

            Text message = new Text();
            message.setText(chatline.getMessage());
            message.getStyleClass().add("Chat-Message");

            Text moment = new Text();
            moment.setText(chatline.getMoment().toString());
            moment.getStyleClass().add("Chat-Moment");

            messageVBox.getChildren().add(message);
            messageVBox.getChildren().add(moment);

            messagesVbox.getChildren().add(messageVBox);
        }

        // set the scroll to the bottom
        MessagesScrollpane.setVvalue(1.0);
    }

    // temporary function to test display
    public void sendMessage() {
        String message = messageField.getText();

        if (!message.isEmpty()) {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            // TEST
            System.out.println(timestamp);

            // TODO: base variables on game that is being played and user
            Chatline chatline = new Chatline("Chatter", 1, timestamp, message);
            _controller.sendChatline(chatline);

            messageField.clear();
            displayMessages();
        }
    }

    // when enter key is pressed send the message
    public void onEnter(javafx.event.ActionEvent actionEvent) {
        sendMessage();
    }

    public void displayOpponentsName() {
        // TODO: get opponents name from db and display it
        NameLabel.setText("John Doe");
    }

    @Override
    protected void loadFinished()
    {

    }
}
