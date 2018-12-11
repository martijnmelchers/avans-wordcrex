package model.matchfixer;

import model.GameSession;
import model.database.DocumentSession;
import model.database.services.Database;
import model.helper.Log;
import model.tables.GameState;


import java.sql.SQLException;
import java.util.List;

public class Matchfixer {

    Database dB;

    public Matchfixer(){
        try {
            dB = DocumentSession.getDatabase();
        } catch (SQLException e) {
            Log.error(e);
        }
    }

    public boolean invitePlayer(String Player){
        dB.update(new Match("request","NL", GameSession.getUsername(),));

        return false;
    }
    public List<String> searchPlayers(String name){

    }
}
