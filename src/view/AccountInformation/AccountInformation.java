package view.AccountInformation;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.GameSession;
import model.helper.Log;
import view.View;

public class AccountInformation extends View {

    private AccountController _controller;
    @FXML
    private Label username;
    @FXML
    private Label role;
    @FXML
    private Label error;

    @FXML   
    private Label passwordShow;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmationPassword;

    @Override
    protected void loadFinished() {
        try {
            this._controller = this.getController(AccountController.class);
        } catch (Exception e) {
            Log.error(e, true);
        }
        this.getAccountInformation();
    }

    private void getAccountInformation() {

        for (var roleObj : GameSession.getRoles()) {
            this.role.setText(this.role.getText() + "\n" + roleObj.getRole());
        }
        this.username.setText(GameSession.getUsername());

        this.passwordShow.setText(GameSession.getAccount().getPassword());
    }

    public void changePassword() {
        try {
            this._controller.changePassword(this.username.getText(), this.password.getText(), this.confirmationPassword.getText());
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void backMain() {
        try {
            this.getController(AccountController.class).navigate("MatchOverview");
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void showError(String errorMessage) {
        this.error.setText(errorMessage);
        this.error.setVisible(true);
    }
}
