package view.ChatView;

import controller.ChatController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.GameSession;
import model.tables.Account;
import model.tables.Chatline;
import view.View;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ChatView extends View {

    private ChatController _controller;
    private GameSession _session;

    @FXML
    private Pane messagesVbox;

    @FXML
    private ScrollPane MessagesScrollpane;

    @FXML
    private Label NameLabel;

    @FXML
    private TextField messageField;

    public ChatView() {
        _controller = new ChatController();
        _session = new GameSession();
    }

    public void initialize() {
        _session.setSession(new Account("bookowner", "no"));
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
        /*// create vbox for a single message
        VBox messageVBox = new VBox();
        messageVBox.setPrefWidth(200);
        messageVBox.setStyle("-fx-background-color: #800080;");

        if (chatline.account.getUsername().equals(_session.getUsername())) {
            // messageVBox.getStyleClass().add("Chat-MessageView-Container-Right");
        } else {
            // messageVBox.getStyleClass().add("Chat-MessageView-Container-Left");
        }

        // create text for message
        Text message = new Text();
        message.setText(chatline.getMessage());
        message.getStyleClass().add("Chat-MessageView");

        // create text for moment
        Text moment = new Text();
        moment.setText(chatline.getMoment().toString());
        moment.getStyleClass().add("Chat-Moment");

        // insert the message and moment into the vbox
        messageVBox.getChildren().add(message);
        messageVBox.getChildren().add(moment);
*/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Incomingmessage.fxml"));

            Pane messagePane = loader.load();

            MessageView messageView = loader.getController();

            messageView.setMessageLabel(chatline.getMessage());
            messageView.setMomentLabel(chatline.getMoment().toString());

            // insert vbox into vbox for all the messages
            messagesVbox.getChildren().add(messagePane);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendMessage() {
        String message = messageField.getText();

        if (!message.isEmpty()) {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            // TODO: insert gameid dynamically
            Chatline chatline = new Chatline(_session.getUsername(), 502, timestamp, message);
            _controller.sendChatline(chatline);

            messageField.clear();
            displayMessages();
        }
    }

    public void onEnter() {
        sendMessage();
    }

    private void displayOpponentsName() {
        // TODO: get opponents name from db and display it
        NameLabel.setText("John Doe");
    }

    @Override
    protected void loadFinished() {
        System.out.println("Load is finished!");
    }
}
