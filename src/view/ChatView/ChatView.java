package view.ChatView;

import controller.ChatController;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.GameSession;
import model.helper.Log;
import model.tables.Chatline;
import view.View;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChatView extends View {

    private ChatController _controller;

    @FXML
    private GridPane _messagesGridPane;

    @FXML
    private ScrollPane _messagesScrollPane;

    @FXML
    private TextField messageField;

    @FXML
    private VBox _parent;

    private int messageCount;

    private Timeline messageChecker;

    public ChatView() {
        this._controller = new ChatController();
    }

    public void initialize() {
        if (GameSession.getGame() != null) {
            this.displayMessages();
            this.checkForMessages();
        }
    }

    private void displayMessages() {
        // clear the gridpane
        this._messagesGridPane.getChildren().clear();

        // get the chatlines from the database by id
        ArrayList<Chatline> chatlines = this._controller.getChatlines(GameSession.getGame().getGameId());

        this.messageCount = chatlines.size();

        for (Chatline chatline : chatlines) {
            this.displayMessage(chatline);
        }

        // set the scroll to the bottom
        this._messagesScrollPane.applyCss();
        this._messagesScrollPane.layout();
        this._messagesScrollPane.setVvalue(1.0);
    }

    private void displayMessage(Chatline chatline) {
        try {
            // load the message view into an object
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("MessageView.fxml"));
            AnchorPane messagePane = loader.load();

            // load the corresponding controller for the message view
            MessageView messageViewController = loader.getController();

            // set the messagelabel and momentlabel in the message view
            messageViewController.setMessageLabel(chatline.getMessage());
            messageViewController.setMomentLabel(chatline.getMoment().toString());

            // place the messages in the correct gridpane column with the correct color
            if (chatline.account.getUsername().equals(GameSession.getUsername())) {
                messageViewController.setMessageAlignment(1);
                this._messagesGridPane.add(messagePane, 1, this._messagesGridPane.getRowCount() + 1);
            } else {
                messageViewController.setMessageAlignment(0);
                this._messagesGridPane.add(messagePane, 0, this._messagesGridPane.getRowCount() + 1);
            }
        } catch (IOException e) {
            Log.error(e);
        }
    }

    private void checkForMessages() {
        // start a new timeline that will check every second for new messages
        this.messageChecker = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            int chatlinesCount = this._controller.getChatlines(GameSession.getGame().getGameId()).size();

            if (chatlinesCount > this.messageCount) {
                this.displayMessages();
            }
        }));
        this.messageChecker.setCycleCount(Animation.INDEFINITE);
        this.messageChecker.play();
    }

    public void sendMessage() {
        // if there is no game id set there can't be a chat
        if (GameSession.getGame().getGameId() == null) {
            return;
        }

        String message = this.messageField.getText();

        if (!message.isEmpty()) {
            // get the current time and date
            Timestamp timestamp = new Timestamp(new Date().getTime());

            // create a new chatline object with the correct parameters and send it
            Chatline chatline = new Chatline(GameSession.getUsername(), GameSession.getGame().getGameId(), timestamp, message);
            this._controller.sendChatline(chatline);

            // clear the textfield and display the new message
            this.messageField.clear();
            this.displayMessages();

            // delay for 1 second because there can't be multiple messages with the same timestamp
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Log.error(e);
            }
        }
    }

    public void onEnter() {
        this.sendMessage();
    }

    @Override
    protected void loadFinished() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                ChatView.this.ScaleScreen(ChatView.this._parent);
            }
        }.start();
    }

    public void closeMessageChecker() {
        this.messageChecker.stop();
    }
}
