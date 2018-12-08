package view.ChatView;

import controller.ChatController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.DocumentSession;
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

    private void displayMessages() {
        messagesVbox.getChildren().clear();

        ArrayList<Chatline> chatlines = _controller.getChatlines(502);

        for (Chatline chatline : chatlines) {
            displayMessage(chatline);
        }

        // set the scroll to the bottom
        MessagesScrollpane.setVvalue(1.0);
    }

    private void displayMessage(Chatline chatline) {
        // create vbox for a single message
        VBox messageVBox = new VBox();
        messageVBox.setPrefWidth(200);
        messageVBox.setStyle("-fx-background-color: #800080;");

        if(chatline.account.getUsername().equals(DocumentSession.getPlayerUsername())) {
           // messageVBox.getStyleClass().add("Chat-Message-Container-Right");
        } else {
           // messageVBox.getStyleClass().add("Chat-Message-Container-Left");
        }

        // create text for message
        Text message = new Text();
        message.setText(chatline.getMessage());
        message.getStyleClass().add("Chat-Message");

        // create text for moment
        Text moment = new Text();
        moment.setText(chatline.getMoment().toString());
        moment.getStyleClass().add("Chat-Moment");

        // insert the message and moment into the vbox
        messageVBox.getChildren().add(message);
        messageVBox.getChildren().add(moment);

        // insert vbox into vbox for all the messages
        messagesVbox.getChildren().add(messageVBox);
    }

    public void sendMessage() {
        String message = messageField.getText();

        if (!message.isEmpty()) {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            // TEST
            System.out.println(timestamp);

            // TODO: insert gameid dynamically
            Chatline chatline = new Chatline(DocumentSession.getPlayerUsername(), 502, timestamp, message);
            _controller.sendChatline(chatline);

            messageField.clear();
            displayMessages();
        }
    }

    public void onEnter(javafx.event.ActionEvent actionEvent) {
        sendMessage();
    }

    private void displayOpponentsName() {
        // TODO: get opponents name from db and display it
        NameLabel.setText("John Doe");
    }

    @Override
    protected void loadFinished() {

    }
}
