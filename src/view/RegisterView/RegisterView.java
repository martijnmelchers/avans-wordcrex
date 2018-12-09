package view.RegisterView;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.helper.Log;
import view.View;

import java.io.IOException;

public class RegisterView extends View
{



    private AccountController accountController;

    @FXML private Label labelError;

    @FXML private PasswordField passwordFieldPassword;
    @FXML private PasswordField passwordFieldConfirmationPassword;

    @FXML private TextField textFieldUsername;


    protected void loadFinished()
    {
        try {
            accountController = this.getController(AccountController.class);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public void backClicked()
    {
        try {
            accountController.navigate("LoginView", 350, 550);
        } catch (IOException e) {
            Log.error(e, true);
        }
    }

    public void registerClicked()
    {
        accountController.registerUser(textFieldUsername.getText(), passwordFieldPassword.getText(),passwordFieldConfirmationPassword.getText());
    }

    public void showError(String error)
    {
        labelError.setText(error);
        labelError.setVisible(true);
    }
}
