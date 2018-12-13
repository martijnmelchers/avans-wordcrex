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

    public AccountInfo()
    {

    }


    public AccountInfo(String role, String username){
        this.usernameId = username;
        this.roleId = role;
    }

    // What kind of naming is this??
    public String doStuff() {
        return usernameId;
    }

    public void setRoleId(String role) {
        this.roleId = role;
    }

    public String getUsername() {
        return this.roleId;
    }

    public String getRole(){
        return this.roleId;
    }
}
