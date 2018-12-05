package view;

import controller.LoginController;
import javafx.fxml.FXML;

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

    @Override
    protected void loadFinished() {

    }
}
