package view.ChatView;

import controller.ChatController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
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
    private GridPane messagesGridpane;

    @FXML
    private ScrollPane messagesScrollpane;

    @FXML
    private TextField messageField;

    @FXML
    private VBox _parent;

    private int messageCount;

    public ChatView() {
        _controller = new ChatController();
    }

    public void initialize() {
        if(GameSession.getGame() != null) {
            displayMessages();
            checkForMessages();
        }
    }

    private void displayMessages() {
        messagesGridpane.getChildren().clear();

        ArrayList<Chatline> chatlines = _controller.getChatlines(GameSession.getGame().getGameId());

        this.messageCount = chatlines.size();

        for (Chatline chatline : chatlines) {
            displayMessage(chatline);
        }

        // set the scroll to the bottom
        messagesScrollpane.setVvalue(1.0);
    }

    private void displayMessage(Chatline chatline) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageView.fxml"));
            AnchorPane messagePane = loader.load();

            MessageView messageViewController = loader.getController();

            messageViewController.setMessageLabel(chatline.getMessage());
            messageViewController.setMomentLabel(chatline.getMoment().toString());

            if(chatline.account.getUsername().equals(GameSession.getUsername())) {
                messageViewController.setMessageAlignment(1);
                messagesGridpane.add(messagePane, 1, messagesGridpane.getRowCount() + 1);
            } else {
                messageViewController.setMessageAlignment(0);
                messagesGridpane.add(messagePane, 0, messagesGridpane.getRowCount() + 1);
            }
        } catch (IOException e) {
            Log.error(e);
        }
    }

    private void checkForMessages() {
        java.util.TimerTask task = new java.util.TimerTask() {
            @Override
            public void run() {
                int chatlinesCount = _controller.getChatlines(GameSession.getGame().getGameId()).size();

                if (chatlinesCount > messageCount) {
                    displayMessages();
                }
            }
        };
        java.util.Timer timer = new java.util.Timer(true);
        timer.schedule(task, 0, 1000);
    }

    public void sendMessage() {
        if(GameSession.getGame().getGameId() == null) {
            return;
        }


        String message = messageField.getText();

        if (!message.isEmpty()) {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            Chatline chatline = new Chatline(GameSession.getUsername(), GameSession.getGame().getGameId(), timestamp, message);
            _controller.sendChatline(chatline);

            messageField.clear();
            displayMessages();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onEnter() {
        sendMessage();
    }

    @Override
    protected void loadFinished() {
        new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                ScaleScreen(_parent);
            }
        }.start();
    }
}
