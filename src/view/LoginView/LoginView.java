package view.LoginView;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.helper.Log;
import view.View;

public class LoginView extends View {

    private AccountController _accountController;

    @FXML
    private Button buttonLogin;

    @FXML
    private Label labelError;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordFieldPassword;

    @FXML
    private void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.TAB) {
            this.passwordFieldPassword.requestFocus();
        } else if (e.getCode() == KeyCode.ENTER) {
            this.loginClicked();
        }
    }

    @FXML
    private void passwordFieldKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.TAB) {
            this.textFieldUsername.requestFocus();
        } else if (e.getCode() == KeyCode.ENTER) {
            this.loginClicked();
        }
    }

    protected void loadFinished() {
        try {
            this._accountController = this.getController(AccountController.class);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void loginClicked() {
        this._accountController.loginUser(this.textFieldUsername.getText(), this.passwordFieldPassword.getText());
    }

    public void registerClicked() {
        try {
            this._accountController.navigate("RegisterView", 350, 550, false);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void showError(String error) {
        this.labelError.setText(error);
        this.labelError.setVisible(true);
    }

    public void loginSuccess() {
        try {
            this._accountController.navigate("MatchOverview", 861, 920, true);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }
}
