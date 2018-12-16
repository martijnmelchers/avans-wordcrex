package view.AccountInformation;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.GameSession;
import model.helper.Log;
import view.View;

public class AccountInformation extends View {

    private AccountController _accountController;
    @FXML
    private Label username;
    @FXML
    private Label role;
    @FXML
    private Label error;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmationPassword;

    @Override
    protected void loadFinished() {
        try {
            this._accountController = this.getController(AccountController.class);
        } catch (Exception e) {
            Log.error(e, true);
        }
        getAccountInformation();
    }

    private void getAccountInformation() {
        role.setText(GameSession.getRole().getRole());
        username.setText(GameSession.getUsername());
    }

    public void changePassword() {
        try {
            this._accountController.changePassword(username.getText(), password.getText(), confirmationPassword.getText());
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
        error.setText(errorMessage);
        error.setVisible(true);
    }
}
