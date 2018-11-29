package controller;

import view.LoginView;
import view.RegisterView;

public class AccountController extends Controller
{
    public AccountController()
    {

    }

    private boolean checkPasswords(String password, String confirmationPassword)
    {
        return  password.equals(confirmationPassword);
    }

    public void registerUser(String username, String password,String confirmationPassword)
    {
        RegisterView registerView = getViewCasted();
        if(!checkPasswords(password,confirmationPassword ))
        {
            registerView.showError("De wachtwoorden komen niet overeen");
        }
        //TODO: register user
    }

    public void checkUserCredentials(String username ,String password)
    {
        LoginView loginView = getViewCasted();
        if(true)
        {
            loginView.showError("Inloggegevens onjuist");
        }
        // TODO: check user credentials in model
    }
}
