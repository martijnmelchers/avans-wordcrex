package view.LoginView;

import controller.LoginController;
import javafx.fxml.FXML;
import view.View;

public class LoginView extends View
{
    public LoginView()
    {

    }

    @FXML
    public void click()
    {
        LoginController loginController = this.getController(LoginController.class);
        loginController.navigate("MainView.fxml");
    }
}
