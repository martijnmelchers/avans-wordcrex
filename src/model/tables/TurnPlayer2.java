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
    private String _usernamePlayer2;

    @Column("bonus")
    private Integer _bonus;

    @Column("score")
    private Integer _score;

    @Column("turnaction_type")
    private String _turnactionType;

    public Turn turn;

    public Integer getGameId() { return _gameId; }
    public Integer getturnId() { return _turnId; }
    public String getUsernamePlayer2() { return _usernamePlayer2; }
    public Integer getBonus() { return _bonus; }
    public Integer getScore() { return  _score; }
    public String getTurnAction() { return _turnactionType; }


    public TurnPlayer2(Integer gameId, Integer turnId, String usernamePlayer2, Integer score, Integer bonus, String turnactionType ) {
        _gameId = gameId;
        _turnId = turnId;
        _usernamePlayer2 = usernamePlayer2;
        _score = score;
        _bonus = bonus;
        _turnactionType = turnactionType;
    }
}
