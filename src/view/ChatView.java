package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ChatView extends View{
    private ArrayList<String> messages = new ArrayList<>();

    @FXML
    private Pane messagesVbox;

    @FXML
    private TextField messageField;

    // temporary function for displaying messages
    public void displayMessages() {
        messagesVbox.getChildren().clear();

        for(String message : messages){
            Text displayMessage = new Text();
            displayMessage.setText(message);
            messagesVbox.getChildren().add(displayMessage);
        }
    }

    // temporary function to test display
    public void sendMessage() {
        String message = messageField.getText();

        if(!message.isEmpty()){
            messages.add(message);
            messageField.clear();
        }

        displayMessages();
    }

    // when enter key is pressed send the message
    public void onEnter(javafx.event.ActionEvent actionEvent) {
        sendMessage();
    }
}
