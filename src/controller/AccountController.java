package controller;

import model.AccountModel;
import model.GameSession;
import model.helper.Log;
import model.tables.Account;
import view.LoginView.LoginView;
import view.RegisterView.RegisterView;
import view.AccountInformation.AccountInformation;

import java.io.IOException;

public class AccountController extends Controller
{

    private AccountModel _model;

    public AccountController()
    {
        this._model = new AccountModel();
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

        String error = this._model.registerAccount(username, password);

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

        Account account = this._model.getAccount(username, password);

        if(account == null)
        {
            loginView.showError("Inloggegevens onjuist");
            return;
        }

        GameSession.setSession(account);

        loginView.loginSucces();
    }

    public boolean changePassword(String username, String password, String confirmationPassword)
    {
        AccountInformation accountInformation = getViewCasted();
        if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty())
        {
            accountInformation.showError("Vul alle velden in");
            return false;
        }

        if(!checkPasswords(password,confirmationPassword ))
        {
            accountInformation.showError("De wachtwoorden komen niet overeen");
            return false;
        }

        String error = this._model.changePassword(username, password);

        if(!error.equals(""))
        {
            accountInformation.showError(error);
            return false;
        }

        return true;
    }

    public String getUsername()
    {
        return GameSession.getUsername();
    }

    public String getRole()
    {
        return this._model.getRole();
    }
}
