package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("boardplayer1")
public class BoardPlayer1 {

    @PrimaryKey
    @Column("game_id")
    @ForeignKey(type = TurnPlayer1.class, field = "game_id", output = "turnPlayer1")
    private int _gameId;

    @PrimaryKey
    @Column("username")
    @ForeignKey(type = TurnPlayer1.class, field = "username_player 1", output = "turnPlayer1")
    private String _username;

    @PrimaryKey
    @Column("turn_id")
    @ForeignKey(type = TurnPlayer1.class, field = "turn_id", output = "turnPlayer1")
    private int _turnId;

    @PrimaryKey
    @Column("letter_id")
    @ForeignKey(type = Turn.class, field = "letter_id", output = "turn")
    private int _letterId;

    @Column("tyle_x")
    @ForeignKey(type = Tile.class, field = "tile_x", output = "tile")
    private int _tileX;

    @Column("tyle_y")
    @ForeignKey(type = Tile.class, field = "tile_y", output = "tile")
    private int _tileY;

    public TurnPlayer2 turnPlayer1;
    public Turn turn;
    public Tile tile;

    public BoardPlayer1() {}
}
