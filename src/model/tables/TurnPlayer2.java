package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("turnplayer2")
public class TurnPlayer2 {

    @PrimaryKey
    @Column("game_id")
    @ForeignKey(type = Turn.class, field = "game_id", output = "turn")
    private Integer _gameId;

    @PrimaryKey
    @Column("turn_id")
    @ForeignKey(type = Turn.class, field = "turn_id", output = "turn")
    private Integer _turnId;

    @PrimaryKey
    @Column("username_player2")
    private String _usernamePlayer1;

    @Column("bonus")
    private Integer _bonus;

    @Column("score")
    private Integer _score;

    @Column("turnaction_type")
    private String _turnactionType;

    public Turn turn;

    public TurnPlayer2() {}
}
