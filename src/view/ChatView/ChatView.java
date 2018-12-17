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
import java.sql.Time;
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
        this._messagesGridPane.getChildren().clear();

        ArrayList<Chatline> chatlines = this._controller.getChatlines(GameSession.getGame().getGameId());

        this.messageCount = chatlines.size();

        for (Chatline chatline : chatlines) {
            this.displayMessage(chatline);
        }

        // set the scroll to the bottom
        _messagesScrollPane.applyCss();
        _messagesScrollPane.layout();
        _messagesScrollPane.setVvalue(1.0);
    }

    private void displayMessage(Chatline chatline) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("MessageView.fxml"));
            AnchorPane messagePane = loader.load();

            MessageView messageViewController = loader.getController();

            messageViewController.setMessageLabel(chatline.getMessage());
            messageViewController.setMomentLabel(chatline.getMoment().toString());

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
        this.messageChecker = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            int chatlinesCount = _controller.getChatlines(GameSession.getGame().getGameId()).size();

            if (chatlinesCount > messageCount) {
                displayMessages();
            }
        }));
        this.messageChecker.setCycleCount(Animation.INDEFINITE);
        this.messageChecker.play();
    }

    public void sendMessage() {
        if (GameSession.getGame().getGameId() == null) {
            return;
        }


        String message = this.messageField.getText();

        if (!message.isEmpty()) {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            Chatline chatline = new Chatline(GameSession.getUsername(), GameSession.getGame().getGameId(), timestamp, message);
            this._controller.sendChatline(chatline);

            this.messageField.clear();
            this.displayMessages();

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
