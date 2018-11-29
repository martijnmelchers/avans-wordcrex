package view;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class RegisterView extends View
{

    private AccountController accountController;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmationPassword;

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
        if(!accountController.checkPasswords(password.getText(), confirmationPassword.getText()))
        {
            return;
        }

        accountController.navigate("MainView.fxml");
    }
}
