package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("letterset")
public class LetterSet {

    @Column("code")
    @PrimaryKey
    private String _code;

    @Column("description")
    private String _description;

    public LetterSet() {
    }

    public String getDescription() {
        return this._description;
    }
}
