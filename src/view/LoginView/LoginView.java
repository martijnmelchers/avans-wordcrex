package view;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginView extends View
{

    private AccountController accountController;

    @FXML private Label labelError;

    @FXML private TextField textFieldUsername;

    @FXML private PasswordField passwordFieldPassword;

    @Override
    protected void loadFinished()
    {
        accountController = this.getController(AccountController.class);
    }

    public void loginClicked()
    {
        accountController.checkUserCredentials(textFieldUsername.getText(),passwordFieldPassword.getText());
    }

    public void registerClicked()
    {
        accountController.navigate("RegisterView", 350, 550);
    }

    public void showError(String error)
    {
        labelError.setText(error);
        labelError.setVisible(true);
    }

    public void loginSucces()
    {
        //TODO: accountController.navigatie("") << Main menu here
    }

    public void setCredentials(String username, String password)
    {
        textFieldUsername.setText(username);
        passwordFieldPassword.setText(password);
    }
}
