package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(name = "tiletype")
public class TileType {

    @Column(name = "type")
    @PrimaryKey
    private String _tileType;
}
