package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Table("letter")
public class Letter {

    public Symbol symbol;
    public Game game;
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

    public Letter(Integer _letterId, Integer _gameId, String _symbolLettersetCode, String _symbol) {
        this._letterId = _letterId;
        this._gameId = _gameId;
        this._symbolLettersetCode = _symbolLettersetCode;
        this._symbol = _symbol;
    }

    public Letter() {
    }

    public static List<String> defaultLetters() {
        String[] letters = new String[]{
                "A", "A", "A", "A", "A", "A", "A",
                "B", "B",
                "C", "C",
                "D", "D", "D", "D", "D",
                "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E",
                "F", "F",
                "G", "G", "G",
                "H", "H",
                "I", "I", "I", "I",
                "J", "J",
                "K", "K", "K",
                "L", "L", "L",
                "M", "M", "M",
                "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N",
                "O", "O", "O", "O", "O", "O",
                "P", "P",
                "Q",
                "R", "R", "R", "R", "R",
                "S", "S", "S", "S", "S",
                "T", "T", "T", "T", "T",
                "U", "U", "U",
                "V", "V",
                "W", "W",
                "X",
                "Y",
                "Z", "Z"
        };

        return Arrays.stream(letters).collect(Collectors.toList());
    }

    public String getSymbol() {
        return this._symbol;
    }

    public Integer getLetterId() {
        return this._letterId;
    }
}
