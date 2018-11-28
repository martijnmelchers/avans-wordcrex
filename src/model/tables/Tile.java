package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("Tile")
public class Tile {

    @Column("x")
    @PrimaryKey
    private String _xPosition;

    @Column("y")
    @PrimaryKey
    private String _yPosition;

    @Column("tile_type")
    @ForeignKey(type = TileType.class, field = "type", output = "tiletype")
    private String _tileType;


}
