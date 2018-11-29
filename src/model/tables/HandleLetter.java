package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("handleletter")
public class HandleLetter {

    @Column("game_id")
    @PrimaryKey
    @ForeignKey(type = Letter.class, field = "game_id", output = "letter")
    private int _gameId;

    @Column("turn_id")
    @PrimaryKey
    @ForeignKey(type = Turn.class, field = "turn_id", output = "turn")
    private int _turnId;

    @Column("letter_id")
    @PrimaryKey
    @ForeignKey(type = Letter.class, field = "letter_id", output = "letter")
    private int _letterId;

    public Letter letter;
    public Turn turn;

    public HandleLetter() {}
}
