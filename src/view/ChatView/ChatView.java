package view.ChatView;

import controller.ChatController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.GameSession;
import model.helper.Log;
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
    private Pane messagesVboxLeft;

    @FXML
    private Pane messagesVboxRight;

    @FXML
    private ScrollPane MessagesScrollpane;

    @FXML
    private Label NameLabel;

    @FXML
    private TextField messageField;

    @FXML
    private VBox _parent;

    private int messageCount;

    public ChatView() {
        _controller = new ChatController();
    }

    public void initialize() {
        GameSession.setSession(new Account("lidewij", "no"));
        displayOpponentsName();
        displayMessages();
        checkForMessages();
    }

    private void displayMessages() {
        messagesVboxLeft.getChildren().clear();
        messagesVboxRight.getChildren().clear();

        ArrayList<Chatline> chatlines = _controller.getChatlines(502);

        this.messageCount = chatlines.size();

        for (Chatline chatline : chatlines) {
            displayMessage(chatline);
        }

        // set the scroll to the bottom
        MessagesScrollpane.setVvalue(1.0);
    }

    private void checkForMessages() {
        java.util.TimerTask task = new java.util.TimerTask() {
            @Override
            public void run() {
                int chatlinesCount = _controller.getChatlines(502).size();

                if (chatlinesCount > messageCount) {
                    displayMessages();
                }
            }
        };
        java.util.Timer timer = new java.util.Timer(true);
        timer.schedule(task, 0, 1000);
    }

    private void displayMessage(Chatline chatline) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageView.fxml"));

            AnchorPane messagePane = loader.load();

            MessageView messageViewController = loader.getController();

            messageViewController.setMessageLabel(chatline.getMessage());
            messageViewController.setMomentLabel(chatline.getMoment().toString());


            if(chatline.account.getUsername().equals("bookowner")) {
                messagesVboxRight.getChildren().add(messagePane);
                messagesVboxLeft.getChildren().add(createEmptyPane(messagePane, chatline));
            } else {
                messagesVboxLeft.getChildren().add(messagePane);
                messagesVboxRight.getChildren().add(createEmptyPane(messagePane, chatline));
            }



        } catch (IOException e) {
            Log.error(e);
        }


    }

    public void sendMessage() {
        String message = messageField.getText();

        if (!message.isEmpty()) {
            Timestamp timestamp = new Timestamp(new Date().getTime());

            // TODO: insert gameid dynamically
            Chatline chatline = new Chatline(GameSession.getUsername(), 502, timestamp, message);
            _controller.sendChatline(chatline);

            messageField.clear();
            displayMessages();
        }
    }

    private Pane createEmptyPane(Pane messagePane, Chatline chatline) {
        Pane emptyPane = new Pane();

        emptyPane.setPrefSize(messagePane.getPrefWidth(), messagePane.getPrefHeight());
        emptyPane.setMinSize(messagePane.getMinWidth(),messagePane.getMaxHeight());
        emptyPane.setMaxSize(messagePane.getMaxWidth(), messagePane.getMaxHeight());

        // TEST
        emptyPane.setStyle("-fx-background-color: RED");

        String test2 = chatline.getMessage().replaceAll(".", " ");

        Text test = new Text(test2);

        emptyPane.getChildren().add(test);
        return emptyPane;
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
