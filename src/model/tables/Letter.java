package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("letter")
public class Letter {

    @Column("letter_id")
    @PrimaryKey
    private Integer _letterId;

    @PrimaryKey
    @Column("game_id")
    @ForeignKey(type = Game.class, field = "game_id", output = "game")
    private Integer _gameId;

    @Column("symbol_letterset_code")
    @ForeignKey(type = Symbol.class, field = "letterset_code", output = "symbol")
    private String _symbolLettersetCode;

    @Column("symbol")
    @ForeignKey(type = Symbol.class, field = "symbol", output = "symbol")
    private String _symbol;

    public Symbol symbol;
    public Game game;

    public Letter(){}

    public String get_symbol()
    {
        return _symbol;
    }

    public Integer get_letterId()
    {
        return _letterId;
    }
}
