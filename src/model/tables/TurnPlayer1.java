package model.tables;

import model.database.annotations.*;

@Table("turnplayer1")
public class TurnPlayer1
{

    @PrimaryKey
    @Column("game_id")
    @ForeignKey(type = Turn.class, field = "game_id", output = "turn")
    private Integer _gameId;

    @PrimaryKey
    @Column("turn_id")
    @ForeignKey(type = Turn.class, field = "turn_id", output = "turn")
    private Integer _turnId;

    @PrimaryKey
    @Column("username_player1")
    private String _usernamePlayer1;

    @Column("bonus")
    private Integer _bonus;

    @Column("score")
    private Integer _score;

    @Nullable
    @Column("turnaction_type")
    private String _turnactionType;

    public Turn turn;

    public TurnPlayer1() {}

    public String getTurnActionType(){ return _turnactionType; }

    public Integer getScore() {return _score;}

    public Integer getBonus() {return _bonus;}
}
