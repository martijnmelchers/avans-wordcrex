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

    public GameState getGameState() {
        return gameState;
    }

    public LetterSet getLetterSet() {
        return letterSet;
    }

    public Account getPlayer1() {
        return player1;
    }

    public Account getPlayer2() {
        return player2;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Account getWinner() {
        return winner;
    }

    public String getPlayer1Username() {
        return this._usernamePlayer1;
    }

    public String getPlayer2Username() {
        return this._usernamePlayer1;
    }

    public boolean isParticipating(String name) {

        return (isActive() && (this._usernamePlayer1.equals(name) || this._usernamePlayer1.equals(name)));
    }

    private boolean isActive() {
        return (this._gameState.equals("playing") || this._answerPlayer2.equals("unknown") || (this._gameState.equals("request") && this._answerPlayer2.equals("accepted")));
    }

    public Integer getGameID() {
        return _gameId;
    }
}
