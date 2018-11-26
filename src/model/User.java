package model;

import model.database.annotations.*;

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
    @Column
    @ForeignKey(type = FullName.class, field = "id", output = "name")
    private Integer nameId;

    private FullName name;

    public User() {

    }

    public User(String username, String email, FullName name) {
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public User(Integer userId, String username, String email, FullName name) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.name = name;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInformation() {
        return "User ID #" + this.userId + " with username " + this.username + " and email " + this.email + "\n" +
                "Name ID #" + this.nameId + " and name: " + this.name.getFullName();
    }
}
