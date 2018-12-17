package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("tile")
public class Tile {

    public TileType tileType;
    @Column("x")
    @PrimaryKey
    private Integer _xPosition;
    @Column("y")
    @PrimaryKey
    private Integer _yPosition;
    @Column("tile_type")
    @ForeignKey(type = TileType.class, field = "type", output = "tileType")
    private String _tileType;

    public Tile() {
    }

    public Tile(Integer x, Integer y, model.TileType tileType) {
        this._xPosition = x;
        this._yPosition = y;
        this._tileType = tileType.toString();
    }

    public Integer getX() {
        return this._xPosition;
    }

    public Integer getY() {
        return this._yPosition;
    }

}
