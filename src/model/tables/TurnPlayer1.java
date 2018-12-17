package model.tables;

import model.database.annotations.*;

@Table("turnplayer1")
public class TurnPlayer1 {

    public Turn turn;
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

    public TurnPlayer1() {
    }

    public TurnPlayer1(Integer gameId, Integer turnId, String usernamePlayer1, Integer score, Integer bonus, String turnactionType) {
        this._gameId = gameId;
        this._turnId = turnId;
        this._usernamePlayer1 = usernamePlayer1;
        this._score = score;
        this._bonus = bonus;
        this._turnactionType = turnactionType;
    }

    public Integer getGameId() {
        return this._gameId;
    }

    public Integer getturnId() {
        return this._turnId;
    }

    public String getUsernamePlayer1() {
        return this._usernamePlayer1;
    }

    public Integer getBonus() {
        return this._bonus;
    }

    public Integer getScore() {
        return this._score;
    }

    public String getTurnAction() {
        return this._turnactionType;
    }

}
