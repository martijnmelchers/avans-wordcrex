package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("letter")
public class Turn {

    @PrimaryKey
    @Column("_letterId")
    private String _letterId;

    @Column("game_id")
    @ForeignKey(type = HandleLetter.class, field = "game_id", output = "HandleLetter")
    private String _gameId;

}
