package model.database.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(value = "account")
public class Account {

    @Column
    @PrimaryKey
    private String username;
    @Column
    private String password;

    public Account() {

    }
}
