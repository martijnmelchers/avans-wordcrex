package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(name = "role")
public class Role {

    @Column
    @PrimaryKey
    private String role;


}
