package model;

import model.database.annotations.*;

@Table(name = "names")
public class FullName {

    @Column
    @PrimaryKey
    @AutoIncrement
    public Integer id;

    @Column
    private String firstname;

    @Column
    @Nullable
    private String middlename;

    @Column
    private String lastname;

    public FullName(String firstname, String middlename, String lastname) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
    }

    public FullName() {

    }

    public String getFullName() {
        return this.firstname + (this.middlename == null || this.middlename.equals("") ? " " : " " + this.middlename + " ") + this.lastname;
    }


}
