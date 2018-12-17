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
    private void keyPressed(KeyEvent e)
    {
        if(e.getCode() == KeyCode.TAB)
        {
            passwordFieldPassword.requestFocus();
        }
        else if(e.getCode() == KeyCode.ENTER)
        {
            loginClicked();
        }
    }

    @FXML private void passwordFieldKeyPressed(KeyEvent e)
    {
        if(e.getCode() == KeyCode.TAB)
        {
            textFieldUsername.requestFocus();
        }
        else if(e.getCode() == KeyCode.ENTER)
        {
            loginClicked();
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
        this._accountController.loginUser(textFieldUsername.getText(), passwordFieldPassword.getText());
    }

    public void registerClicked() {
        try {
            this._accountController.navigate("RegisterView", 350, 550);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void showError(String error) {
        labelError.setText(error);
        labelError.setVisible(true);
    }

    public void loginSuccess() {
        try {
            this._accountController.navigate("MatchOverview", 620, 769);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }
}
