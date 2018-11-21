package model;

import model.annotations.*;

@Table(name = "user")
public class User {

    @Column
    @AutoIncrement
    @PrimaryKey
    private Integer userId;
    @Column
    private String username;
    @Column(name = "e_mail")
    @Nullable
    private String email;

    public User() {

    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(Integer userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInformation() {
        return "User #" + this.userId + " is registered with username: " + this.username + " and email: " + this.email;
    }
}
