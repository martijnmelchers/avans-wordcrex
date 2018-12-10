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

    public boolean isRequest() { return _state.equals("request"); }
    public boolean isPlaying() { return _state.equals("playing"); }
    public boolean isFinished() { return _state.equals("finished"); }
    public boolean isResigned() { return _state.equals("resigned"); }

    public String getState(){return _state;}
}
