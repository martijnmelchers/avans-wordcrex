package view;

import controller.ChatController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import model.tables.Chatline;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ChatView extends View {

    private ChatController _controller = new ChatController();

    @FXML
    private Pane messagesVbox;

    @FXML
    private TextField messageField;


    public void initialize() {
        displayMessages();
    }

    // function for displaying messages
    public void displayMessages() {
        messagesVbox.getChildren().clear();

        ArrayList<Chatline> chatlines = _controller.getChatlines(1);

        for (Chatline chatline : chatlines) {
            Text displayMessage = new Text();
            displayMessage.setText(chatline.getMessage());
            messagesVbox.getChildren().add(displayMessage);
        }
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
}
