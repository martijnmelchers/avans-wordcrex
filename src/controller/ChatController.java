package controller;

import model.ChatlineModel;
import model.tables.Chatline;

import java.util.ArrayList;

public class ChatController extends Controller {

    private ChatlineModel model;

    public ChatController() {
        this.model = new ChatlineModel();
    }

    public ArrayList<Chatline> getChatlines(int gameId) {
        return model.getChatlines(gameId);
    }

    public void sendChatline(Chatline chatline) {
        model.sendChatline(chatline);
    }

}
