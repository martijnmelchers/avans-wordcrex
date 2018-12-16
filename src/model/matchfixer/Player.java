package model.matchfixer;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("accountrole")
public class Player {
    @Column
    @PrimaryKey
    private String username;
    @Column
    private String role;

    public String getUsername() {
        return username;
    }
}
