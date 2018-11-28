package view;

public class LoginView extends View
{


    @Override
    protected void loadFinished()
    {

    }

    public void loginClicked()
    {
        this.getController().navigate("MainView.fxml");
    }

    public void registerClicked()
    {
        this.getController().navigate("RegisterView.fxml", 350, 550);
    }


}
