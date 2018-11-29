package view;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterView extends View
{



    private AccountController accountController;

    @FXML private Label labelError;

    @FXML private PasswordField passwordFieldPassword;
    @FXML private PasswordField passwordFieldConfirmationPassword;

    @FXML private TextField textFieldUsername;

    @Override
    protected void loadFinished()
    {
        accountController = this.getController();
    }

    public void backClicked()
    {
        accountController.navigate("LoginView.fxml", 350, 550);
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
