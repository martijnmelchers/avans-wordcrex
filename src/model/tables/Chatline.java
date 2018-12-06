package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

import java.sql.Timestamp;

@Table("chatline")
public class Chatline {

    @PrimaryKey
    @Column("username")
    @ForeignKey(type = Account.class, field = "username", output = "account")
    private String _username;

    @PrimaryKey
    @Column("game_id")
    @ForeignKey(type = Game.class, field = "game_id", output = "game")
    private Integer _gameId;

    @PrimaryKey
    @Column("moment")
    private Timestamp _moment;

    @Column("message")
    private String _message;

    public Account account;
    public Game game;

    public Chatline() {

    }

    public Chatline(String _username, Integer _gameId, Timestamp _moment, String _message) {
        this._username = _username;
        this._gameId = _gameId;
        this._moment = _moment;
        this._message = _message;
    }

    public String getMessage() {
        return _message;
    }

}
