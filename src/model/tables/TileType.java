package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("tiletype")
public class TileType {

    @Column("type")
    @PrimaryKey
    private String _tileType;

    public TileType() {}
}
