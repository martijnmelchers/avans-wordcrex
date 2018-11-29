package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("accountrole")
public class AccountInfo {

    @PrimaryKey
    @Column("username")
    @ForeignKey(type = Account.class, field = "username", output = "account")
    private String usernameId;

    @PrimaryKey
    @Column("role")
    @ForeignKey(type = Role.class, field = "role", output = "role")
    private String roleId;

    public Account account;
    public Role role;

    public AccountInfo(){}

    public String doStuff() {
        System.out.println("waa!!");
        return "Dit is stuff";
    }
}
