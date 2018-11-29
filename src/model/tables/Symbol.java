package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("symbol")
public class Symbol {

    @PrimaryKey
    @Column("letterset_code")
    @ForeignKey(type = LetterSet.class, field = "lettertype", output = "letterSet")
    private String _lettersetCode;


    @PrimaryKey
    @Column("symbol")
    private char _symbol;

    @Column("value")
    private int _value;

    @Column("Counted")
    private int _counted;


    public LetterSet letterSet;

    public Symbol() {}

}
