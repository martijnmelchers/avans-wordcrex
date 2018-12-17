package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("gamestate")
public class GameState {

    @PrimaryKey
    @Column("state")
    private String _state;

    public GameState() {
    }

    public boolean isRequest() {
        return this._state.equals("request");
    }

    public boolean isPlaying() {
        return this._state.equals("playing");
    }

    public boolean isFinished() {
        return this._state.equals("finished");
    }

    public boolean isResigned() {
        return this._state.equals("resigned");
    }

    public String getState() {
        return this._state;
    }


}
