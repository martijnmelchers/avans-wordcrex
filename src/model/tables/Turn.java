package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("turn")
public class Turn {

    @Column("game_id")
    @PrimaryKey
    @ForeignKey(type = Game.class, field = "game_id", output = "game")
    private Integer _gameId;

    @PrimaryKey
    @Column("turn_id")
    private Integer _turnId;

    public Game game;

    public Turn() {}
}
