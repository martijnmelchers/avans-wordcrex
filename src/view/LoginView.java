package view;

import controller.LoginController;
import controller.Main;
import controller.MainController;
import helperClasses.Controller;
import helperClasses.View;
import javafx.fxml.FXML;

public class LoginView extends View
{
    LoginController loginController;
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
