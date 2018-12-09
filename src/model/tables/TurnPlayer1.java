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
    private Integer _gameId;

    @PrimaryKey
    @Column("turn_id")
    @ForeignKey(type = Turn.class, field = "turn_id", output = "turn")
    private Integer _turnId;

    @PrimaryKey
    @Column("username_player1")
    private String _usernamePlayer1;

    @Column("score")
    private Integer _score;

    @Column("bonus")
    private Integer _bonus;

    @Column("turnaction_type")
    private String _turnactionType;

    public Turn turn;

    public Integer getGameId() { return _gameId; }
    public Integer getturnId() { return _turnId; }
    public String getUsernamePlayer1() { return _usernamePlayer1; }
    public Integer getBonus() { return _bonus; }
    public Integer getScore() { return  _score; }
    public String getTurnAction() { return _turnactionType; }

    public TurnPlayer1() {}

    public TurnPlayer1(Integer gameId, Integer turnId, String usernamePlayer1, Integer score, Integer bonus, String turnactionType ) {
        _gameId = gameId;
        _turnId = turnId;
        _usernamePlayer1 = usernamePlayer1;
        _score = score;
        _bonus = bonus;
        _turnactionType = turnactionType;
    }

}
