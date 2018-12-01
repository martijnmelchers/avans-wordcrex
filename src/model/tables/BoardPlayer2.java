package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("boardplayer2")
public class BoardPlayer2 {

    @PrimaryKey
    @Column("game_id")
    @ForeignKey(type = TurnPlayer2.class, field = "game_id", output = "turnPlayer2")
    private Integer _gameId;

    @PrimaryKey
    @Column("username")
    @ForeignKey(type = TurnPlayer2.class, field = "username_player2", output = "turnPlayer2")
    private String _username;

    @PrimaryKey
    @Column("turn_id")
    @ForeignKey(type = TurnPlayer2.class, field = "turn_id", output = "turnPlayer2")
    private Integer _turnId;

    @PrimaryKey
    @Column("letter_id")
    @ForeignKey(type = Turn.class, field = "letter_id", output = "turn")
    private Integer _letterId;

    @Column("tyle_x")
    @ForeignKey(type = Tile.class, field = "tile_x", output = "tile")
    private Integer _tileX;

    @Column("tyle_y")
    @ForeignKey(type = Tile.class, field = "tile_y", output = "tile")
    private Integer _tileY;

    public TurnPlayer2 turnPlayer2;
    public Turn turn;
    public Tile tile;

    public BoardPlayer2() {}
}
