package model;

import model.annotations.Column;
import model.annotations.Nullable;

public class User {

    @Column()
    private Integer userId;
    @Column()
    private String username;
    @Column(name = "e_mail")
    @Nullable
    private String email;

    public User() {

    }

    public User(Integer userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getInformation() {
        return "User #" + this.userId + " is registered with username: " + this.username + " and email:     " + this.email;
    }
}
