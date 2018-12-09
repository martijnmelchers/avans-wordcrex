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

    public Game(Integer gameId, String gameState, String letterSetCode, String usernamePlayer1, String usernamePlayer2, String answerPlayer2){
        _gameId = gameId;
        _gameState = gameState;
        _letterSetCode = letterSetCode;
        _usernamePlayer1 = usernamePlayer1;
        _usernamePlayer2 = usernamePlayer2;
        _answerPlayer2 = answerPlayer2;
    }

    public Integer getGameId() { return _gameId; }

    public String getGamestate() { return _gameState; }

    public String getLetterSetCode() { return _letterSetCode; }

    public String getUsernamePlayer1() { return _usernamePlayer1; }

    public String getUsernamePlayer2() { return _usernamePlayer2; }

    public Game(){}

    public String getAnswerPlayer2() { return _answerPlayer2; }

}
