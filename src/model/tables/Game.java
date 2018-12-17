package model.tables;

import model.database.annotations.*;
import model.database.enumerators.JoinMethod;

@Table("game")
public class Game {
    @PrimaryKey
    @Column("game_id")
    @AutoIncrement
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

    private GameState gameState;
    private LetterSet letterSet;
    private Account player1;
    private Account player2;
    private Answer answer;
    private Account winner;

    public Game() {
    }

    public Game(String game_state, String letterset_code, String username_player1, String username_player2, String answer_player2) {
        this._gameState = game_state;
        this._letterSetCode = letterset_code;
        this._usernamePlayer1 = username_player1;
        this._usernamePlayer2 = username_player2;
        this._answerPlayer2 = answer_player2;
    }

    public Game(Integer _gameId, String _gameState, String _letterSetCode, String _usernamePlayer1, String _usernamePlayer2, String _answerPlayer2) {
        this._gameId = _gameId;
        this._gameState = _gameState;
        this._letterSetCode = _letterSetCode;
        this._usernamePlayer1 = _usernamePlayer1;
        this._usernamePlayer2 = _usernamePlayer2;
        this._answerPlayer2 = _answerPlayer2;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(String _gameState) {
        this._gameState = _gameState;
    }

    public LetterSet getLetterSet() {
        return this.letterSet;
    }

    public Account getPlayer1() {
        return this.player1;
    }

    public Account getPlayer2() {
        return this.player2;
    }

    public Answer getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this._answerPlayer2 = answer;
    }

    public Account getWinner() {
        return this.winner;
    }

    public void setWinner(String username) {
        this._usernameWinner = username;
    }

    public String getPlayer1Username() {
        return this._usernamePlayer1;
    }

    public String getPlayer2Username() {
        return this._usernamePlayer2;
    }

    public boolean isParticipating(String name) {

        return (this.isActive() && (this._usernamePlayer1.equals(name) || this._usernamePlayer2.equals(name)));
    }

    public Integer getGameId() {
        return this._gameId;
    }

    public void setState(String state) {
        this._gameState = state;
    }

    private boolean isActive() {
        return (this._gameState.equals("playing") || this._answerPlayer2.equals("unknown") || (this._gameState.equals("request") && this._answerPlayer2.equals("accepted")));
    }
}
