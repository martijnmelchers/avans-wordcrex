package model.database.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(value = "Tile")
public class Tile {

    @Column(value = "x")
    @PrimaryKey
    private String _xPosition;

    @Column(value = "y")
    @PrimaryKey
    private String _yPosition;

    @Column(value = "tile_type")
    @ForeignKey(type = TileType.class, field = "type", output = "tiletype")
    private String _tileType;


}
