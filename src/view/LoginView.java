package view;

import controller.LoginController;
import javafx.fxml.FXML;

public class LoginView extends View
{
    private LoginController loginController;
    public LoginView()
    {

    }

    public void loginClicked()
    {
        loginController = this.getController();
        loginController.navigate("MainView.fxml");
    }

    public void registerClicked()
    {
        loginController = this.getController();
        loginController.navigate("RegisterView.fxml", 350, 550);
    }
}
