package view;

import controller.LoginController;
import javafx.fxml.FXML;

public class LoginView extends View
{
    private LoginController loginController;

    public LoginView()
    {

    }

    @FXML
    public void click()
    {
        loginController = this.getController();
        loginController.navigate("MainView.fxml");
    }
}
