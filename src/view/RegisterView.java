package view;

import controller.AccountController;

public class RegisterView extends View
{

    private AccountController accountController;

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
        accountController.navigate("MainView.fxml");
    }
}
