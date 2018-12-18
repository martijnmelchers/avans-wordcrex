package view.RegisterView;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.helper.Log;
import view.View;

import java.io.IOException;

public class RegisterView extends View {
    private AccountController _accountController;

    @FXML
    private Label labelError;
    @FXML
    private PasswordField passwordFieldPassword;
    @FXML
    private PasswordField passwordFieldConfirmationPassword;
    @FXML
    private TextField textFieldUsername;

    @Override
    protected void loadFinished() {
        try {
            this._accountController = this.getController(AccountController.class);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void backClicked() {
        try {
            this._accountController.navigate("LoginView", 350, 550, false);
        } catch (IOException e) {
            Log.error(e, true);
        }
    }

    public void registerClicked() {
        this._accountController.registerUser(this.textFieldUsername.getText(), this.passwordFieldPassword.getText(), this.passwordFieldConfirmationPassword.getText());
    }

    public void showError(String error) {
        this.labelError.setText(error);
        this.labelError.setVisible(true);
    }

    public void registerSuccess() {
        try {
            this._accountController.navigate("MatchOverview", 861, 920, true);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }
}
