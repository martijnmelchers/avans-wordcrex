package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("gamestate")
public class GameState {

    @PrimaryKey
    @Column("state")
    private String _state;

    public GameState() {}
}
