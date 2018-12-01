package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("game")
public class Game {

    @PrimaryKey
    @Column("game_id")
    private int _gameId;

    @Column("game_state")
    @ForeignKey(type = GameState.class, field = "game_state", output = "gameState")
    private String _gameState;

    @Column("letterset_code")
    @ForeignKey(type = LetterSet.class, field = "letterset_code", output = "letterSet")
    private String _letterSetCode;

    @Column("username_player1")
    @ForeignKey(type = Account.class, field = "username", output = "account")
    private String _usernamePlayer1;

    @Column("username_player2")
    @ForeignKey(type = Account.class, field = "username", output = "account")
    private String _usernamePlayer2;

    @Column("answer_player2")
    @ForeignKey(type = Answer.class, field = "type", output = "answer")
    private String _answerPlayer2;

    public GameState gameState;
    public LetterSet letterSet;
    public Account account;
    public Answer answer;

    public Game(){}
}
