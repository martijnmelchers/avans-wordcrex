package model.matchfixer;

import model.database.annotations.*;

@Table("game")
public class Match {
    @Column
    @PrimaryKey
    @AutoIncrement
    private Integer game_id;
    @Column
    private String game_state;
    @Column
    private String letterset_code;
    @Column
    private String username_player1;
    @Column
    private String username_player2;
    @Column
    private String answer_player2;
    @Column
    @Nullable
    private String username_winner;

    public Match(){

    }

    public Match(String game_state, String letterset_code, String username_player1, String username_player2, String answer_player2) {
        //this.game_id = null;
        this.game_state = game_state;
        this.letterset_code = letterset_code;
        this.username_player1 = username_player1;
        this.username_player2 = username_player2;
        this.answer_player2 = answer_player2;
        //this.username_winner = null;
    }
    public boolean Participates(String name) {

        return (IsOngoing() && (username_player1.equals(name) || username_player2.equals(name))  );
    }
    public boolean IsOngoing(){
        return (game_state.equals("playing") || answer_player2.equals("unknown") || (game_state.equals("request") && answer_player2.equals("accepted")));
        //answer_player2.equals("unknown") || game_state.equals("playing") || (game_state.equals("request") && !answer_player2.equals("rejected"))
    }

}
