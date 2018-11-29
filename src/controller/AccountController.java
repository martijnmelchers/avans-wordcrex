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

    public void registerUser(String username, String password, String confirmationPassword)
    {
        RegisterView registerView = getViewCasted();
        if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty())
        {
            registerView.showError("Vul alle velden in");
            return;
        }

        if(!checkPasswords(password,confirmationPassword ))
        {
            registerView.showError("De wachtwoorden komen niet overeen");
            return;
        }
        //TODO: register user
    }

    public void checkUserCredentials(String username, String password)
    {
        LoginView loginView = getViewCasted();
        if (username.isEmpty() || password.isEmpty())
        {
            loginView.showError("Vul alle velden in");
            return;
        }

        if(true)
        {
            loginView.showError("Inloggegevens onjuist");
            return;
        }
        // TODO: check user credentials in model
    }
}
