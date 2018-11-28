package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(value = "turnboardletter")
public class TurnBoardLetter {

    @Column(value = "letter_id")
    @PrimaryKey
    @ForeignKey(type = Letter.class, field = "letter_id", output = "letter")
    private String _letterId;

    @Column(value = "game_id")
    @PrimaryKey
    @ForeignKey(type = Letter.class, field = "letter_id", output = "letter")
    private String _gameId;

    @Column(value = "turn_id")
    @ForeignKey(type = Turn.class, field = "letter_id", output = "letter")
    private String _turnId;

    @Column(value = "tile_x")
    @ForeignKey(type = Tile.class, field = "x", output = "Tile")
    private String _tileX;

    @Column(value = "tile_y")
    @ForeignKey(type = Tile.class, field = "y", output = "Tile")
    private String _tileY;
}
