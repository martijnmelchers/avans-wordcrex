package view.LoginView;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.helper.Log;
import view.View;

public class LoginView extends View {

    private AccountController _accountController;

    @FXML private Label labelError;

    @FXML private TextField textFieldUsername;

    @FXML private PasswordField passwordFieldPassword;

    protected void loadFinished() {
        try {
            this._accountController = this.getController(AccountController.class);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void loginClicked() {
        this._accountController.checkUserCredentials(textFieldUsername.getText(), passwordFieldPassword.getText());
    }

    public void registerClicked() {
        try {
            this.getController(AccountController.class).navigate("RegisterView", 350, 550);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void showError(String error) {
        labelError.setText(error);
        labelError.setVisible(true);
    }

    public void loginSucces() {
        try {
            this.getController(AccountController.class).navigate("MatchOverview", 1600, 1200);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void setCredentials(String username, String password) {
        textFieldUsername.setText(username);
        passwordFieldPassword.setText(password);
    }
}
