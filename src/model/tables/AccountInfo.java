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


    private Account account;
    private Role role;

    public AccountInfo() {

    }

    public AccountInfo(String role, String username) {
        this.usernameId = username;
        this.roleId = role;

    }
    public void setAccount(Account account){
        this.account = account;
    }

    public String getUsername() {
        return this.usernameId;
    }


    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String role) {
        this.roleId = role;
    }

    public Role getRole() {
        return new Role( this.roleId);
    }

    public Account getAccount() {
        return this.account;
    }
}
