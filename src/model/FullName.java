package model;

import model.database.annotations.AutoIncrement;
import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table(name = "names")
public class FullName {

    @Column
    @PrimaryKey
    @AutoIncrement
    public Integer id;

    @Column
    public String firstname;

    @Column
    public String middlename;

    @Column
    public String lastname;

}
