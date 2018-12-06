package view.ChatView;

import controller.ChatController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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
    private TextField messageField;

    public ChatView() {
        // TODO: run displaymessages after all the fxml elements have loaded
    }

    // function for displaying messages
    public void displayMessages() {
        messagesVbox.getChildren().clear();

        ArrayList<Chatline> chatlines = _controller.getChatlines(500);

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

            System.out.println(timestamp);

            // TODO: base variables on game that is being played and user
            Chatline chatline = new Chatline("ger", 500, timestamp, message);
            _controller.sendChatline(chatline);

            displayMessages();
        }
    }

    // when enter key is pressed send the message
    public void onEnter(javafx.event.ActionEvent actionEvent) {
        sendMessage();
    }
}
