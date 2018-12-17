package model;

import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.Chatline;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChatlineModel {

    private Database _db;

    public ChatlineModel() {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (SQLException e) {
            Log.error(e, true);
        }
    }

    public ArrayList<Chatline> getChatlines(int gameId) {
        ArrayList<Chatline> chatlines = new ArrayList<>();
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("chatline", -1), "game_id", CompareMethod.EQUAL, gameId));

        try {
            chatlines.addAll(this._db.select(Chatline.class, clauses));
        } catch (Exception e) {
            Log.error(e, true);
        }

        chatlines.sort((lineOne, lineTwo) -> {
            if (lineOne.getMoment() == null || lineTwo.getMoment() == null)
                return 0;
            return lineOne.getMoment().compareTo(lineTwo.getMoment());
        });

        return chatlines;
    }

    public void sendChatline(Chatline chatline) {
        try {
            this._db.insert(chatline);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }
}
