package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("symbol")
public class Symbol {

    @PrimaryKey
    @Column("letterset_code")
    @ForeignKey(type = LetterSet.class, field = "code", output = "letterSet")
    private String _lettersetCode;


    @PrimaryKey
    @Column("symbol")
    private char _symbol;

    @Column("value")
    private Integer _value;

    @Column("Counted")
    private Integer _counted;


    public LetterSet letterSet;

    public Symbol() {}

}
