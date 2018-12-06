package model.tables;

import model.database.annotations.Column;
import model.database.annotations.ForeignKey;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("game")
public class Game {

    @PrimaryKey
    @Column("game_id")
    private Integer _gameId;

    @Column("game_state")
    @ForeignKey(type = GameState.class, field = "state", output = "gameState")
    private String _gameState;

    @Column("letterset_code")
    @ForeignKey(type = LetterSet.class, field = "code", output = "letterSet")
    private String _letterSetCode;

    @Column("username_player1")
    @ForeignKey(type = Account.class, field = "username", output = "player1")
    private String _usernamePlayer1;

    @Column("username_player2")
    @ForeignKey(type = Account.class, field = "username", output = "player2")
    private String _usernamePlayer2;

    @Column("answer_player2")
    @ForeignKey(type = Answer.class, field = "type", output = "answer")
    private String _answerPlayer2;

    public GameState gameState;
    public LetterSet letterSet;
    public Account player1;
    public Account player2;
    public Answer answer;

    public Game(){}

    public Integer getGameID() { return _gameId; }
}
