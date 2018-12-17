package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("dictionary")
public class WordDictionary {

    @PrimaryKey
    @Column("word")
    private String _word;

    @PrimaryKey
    @Column("letterset_code")
    @ForeignKey(type = LetterSet.class, field =  "code", output = "_letterSet")
    private String _letterSetCode;

    @Column("state")
    @ForeignKey(type = WordState.class, field = "state", output = "_wordState")
    private String _state;

    @Column("username")
    @ForeignKey(type = Account.class, field = "username", output = "_account")
    private String _username;

    public String getWord() { return _word; }

    private LetterSet _letterSet;
    private Account _account;
    private WordState _wordState;

}
