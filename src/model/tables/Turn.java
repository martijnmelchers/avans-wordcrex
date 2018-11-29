package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("Turn")
public class Turn {

    @Column("game_id")
    @PrimaryKey
    @ForeignKey(type = Game.class, field = "game_id", output = "game")
    private int _gameId;

    @PrimaryKey
    @Column("turn_id")
    private int _turnId;

    public Game game;

    public Turn() {}
}
