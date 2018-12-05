package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("tile")
public class Tile {

    @Column("x")
    @PrimaryKey
    private Integer _xPosition;

    @Column("y")
    @PrimaryKey
    private Integer _yPosition;

    @Column("tile_type")
    @ForeignKey(type = TileType.class, field = "type", output = "tileType")
    private String _tileType;


    public TileType tileType;

    public Tile() {}

}
