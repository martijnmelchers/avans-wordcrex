package model.matchfixer;

import model.GameSession;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.enumerators.LinkMethod;
import model.database.services.Database;
import model.helper.Log;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Matchfixer {

    Database dB;

    private List<Clause> playersLike;
    private List<Clause> playersIAmIngameWith;

    public Matchfixer(){
        try {
            dB = DocumentSession.getDatabase();
        } catch (SQLException e) {
            Log.error(e);
        }
        playersLike = new ArrayList<Clause>();
        playersLike.add(new Clause(new TableAlias("accountrole",-1), "role", CompareMethod.EQUAL, "player"));
        playersLike.add(null);
        playersIAmIngameWith = new ArrayList<Clause>();
        playersIAmIngameWith.add(new Clause(new TableAlias("game",-1), "username_player1", CompareMethod.EQUAL, GameSession.getUsername(), LinkMethod.OR));
        playersIAmIngameWith.add(new Clause(new TableAlias("game",-1), "username_player2", CompareMethod.EQUAL, GameSession.getUsername(),LinkMethod.OR));

        //playersIAmIngameWith.add(null);
        //playersIAmIngameWith.add(null);
    }

    public boolean invitePlayer(String Player){
        try {
            Match temp = new Match("request","NL", GameSession.getUsername(),Player,"unknown");
            dB.insert(temp);
            return true;
        } catch (Exception e) {
            Log.error(e);
        }

        return false;
    }
    public List<Player> searchPlayers(String name){
        playersLike.set(1,new Clause(new TableAlias("accountrole",-1), "username", CompareMethod.LIKE, "%"+name+"%"));


        try {
            return filteredPlayers( dB.select(Player.class,playersLike));
        } catch (Exception e) {
            Log.error(e);
        }
        return  null;
    }
    public List<Player> filteredPlayers(List<Player> Players){

        List<Match> matches = null;
        try {
            matches = dB.select(Match.class, playersIAmIngameWith);
        } catch (Exception e) {
            //Log.error(e);
            e.printStackTrace();
        }


        for (int i  = 0; i < Players.size();i++){
            final String name = Players.get(i).getUsername();
            Optional<Match> optional = matches.stream().filter(x -> x.Participates(name)).findFirst();
            if (optional.isPresent())
                Players.remove(i);
        }
        return Players;
    }

}
