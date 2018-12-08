package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("account")
public class Account {

    @Column
    @PrimaryKey
    private String username;

    @Column
    private String password;

    public Account() { }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername(){ return username; }
}
