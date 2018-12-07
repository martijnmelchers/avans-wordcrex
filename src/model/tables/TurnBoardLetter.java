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
    private Integer _letterId;

    @Column("game_id")
    @PrimaryKey
    @ForeignKey(type = Letter.class, field = "game_id", output = "letter")
    private Integer _gameId;

    @Column("turn_id")
    @ForeignKey(type = Turn.class, field = "turn_id", output = "letter")
    private Integer _turnId;

    @Column("tile_x")
    @ForeignKey(type = Tile.class, field = "x", output = "tile")
    private Integer _tileX;

    @Column("tile_y")
    @ForeignKey(type = Tile.class, field = "y", output = "tile")
    private Integer _tileY;

    public Letter letter;
    public Tile tile;

    public TurnBoardLetter() {}
    public TurnBoardLetter(Integer letterId, Integer gameId, Integer turnId, Integer x, Integer y) {
        _letterId = letterId;
        _gameId = gameId;
        _turnId = turnId;
        _tileX = x;
        _tileY = y;
    }
}
