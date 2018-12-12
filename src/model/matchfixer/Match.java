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

        return (IsOngoing() && username_player1 == name || username_player2 == name  );
    }
    public boolean IsOngoing(){
        return (game_state == "playing" || game_state == "request" && answer_player2 != "rejected");
    }

}
