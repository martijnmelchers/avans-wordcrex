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
    @ForeignKey(type = Game.class, field = "username", output = "game")
    private int _gameId;

    @PrimaryKey
    @Column("moment")
    private Timestamp _moment;

    @Column("message")
    private String _message;

    public Account account;
    public Game game;

    public Chatline() {}

}
