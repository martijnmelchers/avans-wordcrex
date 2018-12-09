package model.tables;

import model.database.annotations.*;
import model.database.enumerators.JoinMethod;

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


    @Nullable
    @Column("username_winner")
    @ForeignKey(type = Account.class, field = "username", output = "winner", joinMethod = JoinMethod.LEFT)
    private String _usernameWinner;

    public GameState gameState;
    public LetterSet letterSet;
    public Account player1;
    public Account player2;
    public Answer answer;
    public Account winner;


    public Game(Integer gameId, String gameState, String letterSetCode, String usernamePlayer1, String usernamePlayer2, String answerPlayer2, String usernameWinner){
        _gameId = gameId;
        _gameState = gameState;
        _letterSetCode = letterSetCode;
        _usernamePlayer1 = usernamePlayer1;
        _usernamePlayer2 = usernamePlayer2;
        _answerPlayer2 = answerPlayer2;
        _usernameWinner = usernameWinner;
    }

    public Integer getGameId() { return _gameId; }

    public String getGamestate() { return _gameState; }

    public String getLetterSetCode() { return _letterSetCode; }

    public String getUsernamePlayer1() { return _usernamePlayer1; }

    public String getUsernamePlayer2() { return _usernamePlayer2; }

    public String getAnswerPlayer2() { return _answerPlayer2; }

    public Game(){}
}
