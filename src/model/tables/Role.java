package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("role")
public class Role {

    @Column
    @PrimaryKey
    private String role;

    public Role(){}
    public Role(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
