package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("turnplayer1")
public class TurnPlayer1 {

    @PrimaryKey
    @Column("game_id")
    @ForeignKey(type = Turn.class, field = "game_id", output = "turn")
    private int _gameId;

    @PrimaryKey
    @Column("turn_id")
    @ForeignKey(type = Turn.class, field = "turn_id", output = "turn")
    private int _turnId;

    @PrimaryKey
    @Column("username_player1")
    private String _usernamePlayer1;

    @Column("bonus")
    private int _bonus;

    @Column("score")
    private int _score;

    @Column("turnaction_type")
    private String _turnactionType;

    public Turn turn;

    public TurnPlayer1() {}

}
