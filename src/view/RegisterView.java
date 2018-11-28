package view;

import controller.RegisterController;

public class RegisterView extends View
{

    private RegisterController registerController;

    public void backClicked()
    {
        registerController = this.getController();
        registerController.navigate("LoginView.fxml", 350, 550);
    }

    public void registerClicked()
    {
        registerController = this.getController();
        registerController.navigate("MainView.fxml");
    }

}
