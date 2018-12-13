package controller;

import model.AccountModel;
import model.helper.Log;
import view.LoginView.LoginView;
import view.RegisterView.RegisterView;

import java.io.IOException;

public class AccountController extends Controller
{

    private AccountModel model;

    public AccountController()
    {
        model = new AccountModel();
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

        String error = model.registerAccount(username, password);

        if(!error.equals(""))
        {
            registerView.showError(error);
            return;
        }

        try {
            navigate("loginView", 350, 550);
        } catch (IOException e) {
            Log.error(e, true);
        }

        LoginView loginView = getViewCasted();
        loginView.setCredentials(username,password);
    }

    public void checkUserCredentials(String username, String password)
    {
        LoginView loginView = getViewCasted();
        if (username.isEmpty() || password.isEmpty())
        {
            loginView.showError("Vul alle velden in");
            return;
        }

        if(model.getAccount(username,password ) == null)
        {
            loginView.showError("Inloggegevens onjuist");
            return;
        }

        loginView.loginSucces();
    }
}
