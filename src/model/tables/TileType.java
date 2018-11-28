package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(value = "tiletype")
public class TileType {

    @Column(value = "type")
    @PrimaryKey
    private String _tileType;
}
