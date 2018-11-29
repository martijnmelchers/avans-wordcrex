package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(name = "account")
public class Account {

    @Column
    @PrimaryKey
    private String username;
    @Column
    private String password;

    public Account()
    {

    }

    public boolean checkPassword(String password)
    {
        if(password.equals(this.password))
        {
            return true;
        }
        return false;
    }
}
