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


    public Integer getX()
    {
        return  _xPosition;
    }

    public Integer getY()
    {
        return  _yPosition;
    }

    public TileType tileType;

    public Tile() {}

    public Tile(Integer x, Integer y, model.TileType tileType){
        _xPosition = x;
        _yPosition = y;
        _tileType = tileType.toString();
    }

}
