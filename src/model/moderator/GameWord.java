package model.moderator;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(name = "dictionary")
public class GameWord {
    @Column
    @PrimaryKey
    private String word;
    @Column

    private String letterset_code;
    @Column
    private String state;
    @Column
    private String username;

    public String getWord() {
        return word;
    }

    public String getLetterset_code() {
        return letterset_code;
    }

    public String getState() {
        return state;
    }

    public String getUsername() {
        return username;
    }
}
