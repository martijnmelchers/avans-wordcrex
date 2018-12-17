package model.tables;

import model.database.annotations.*;

@Table("turnplayer2")
public class TurnPlayer2 {

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
    @Column("username_player2")
    private String _usernamePlayer2;
    @Column("bonus")
    private Integer _bonus;
    @Column("score")
    private Integer _score;
    @Nullable
    @Column("turnaction_type")
    private String _turnactionType;

    public TurnPlayer2() {
    }

    public TurnPlayer2(Integer gameId, Integer turnId, String usernamePlayer2, Integer score, Integer bonus, String turnactionType) {
        this._gameId = gameId;
        this._turnId = turnId;
        this._usernamePlayer2 = usernamePlayer2;
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

    public String getUsernamePlayer2() {
        return this._usernamePlayer2;
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
