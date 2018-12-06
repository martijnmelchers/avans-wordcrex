package model;

import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Connector;
import model.database.services.Database;
import model.tables.Chatline;

import java.sql.Connection;
import java.util.ArrayList;

public class ChatlineModel {

    private Database _db;

    public ChatlineModel() {
        try
        {
            Connection conn = new Connector().connect("sql20.main-hosting.eu", "u895965828_avans", "urobj5CBGORAg", "u895965828_avans");
            this._db = new Database(conn, true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Chatline> getChatlines(int gameId) {
        ArrayList<Chatline> chatlines = new ArrayList<>();
        var clauses = new ArrayList<Clause>();

        clauses.add(new Clause(new TableAlias("chatline", -1), "game_id", CompareMethod.EQUAL, gameId));

        try {
            chatlines.addAll(_db.select(Chatline.class, clauses));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatlines;
    }

    public void sendChatline(Chatline chatline) {
        try {
            _db.insert(chatline);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
