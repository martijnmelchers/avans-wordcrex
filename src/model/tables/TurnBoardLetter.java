package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("turnboardletter")
public class TurnBoardLetter {

    @Column("letter_id")
    @PrimaryKey
    @ForeignKey(type = Letter.class, field = "letter_id", output = "letter")
    private String _letterId;

    @Column("game_id")
    @PrimaryKey
    @ForeignKey(type = Letter.class, field = "letter_id", output = "letter")
    private String _gameId;

    @Column("turn_id")
    @ForeignKey(type = Turn.class, field = "letter_id", output = "letter")
    private String _turnId;

    @Column("tile_x")
    @ForeignKey(type = Tile.class, field = "x", output = "Tile")
    private String _tileX;

    @Column("tile_y")
    @ForeignKey(type = Tile.class, field = "y", output = "Tile")
    private String _tileY;
}
