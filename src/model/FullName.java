package model;

import model.database.annotations.*;

@Table(name = "names")
public class FullName {

    @Column
    @PrimaryKey
    @AutoIncrement
    public Integer id;

    @Column
    public String firstname;

    @Column
    @Nullable
    public String middlename;

    @Column
    public String lastname;

}
