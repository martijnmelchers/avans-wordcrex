package view.LoginView;

import controller.LoginController;
import javafx.fxml.FXML;
import view.View;

import java.io.IOException;

public class LoginView extends View
{
    public LoginView()
    {

    }

    @FXML
    public void click() throws Exception {
        LoginController loginController = this.getController(LoginController.class);
        loginController.navigate("MainView.fxml");
    }
}
