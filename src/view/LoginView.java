package view;

import controller.AdminController;
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
        AdminController loginController = this.getController(AdminController.class);
        loginController.navigate("AdminView.fxml");
    }
}
