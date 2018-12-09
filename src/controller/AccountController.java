package controller;

import model.AccountModel;
import view.AccountInformation.AccountInformation;
import view.LoginView;
import view.RegisterView.RegisterView;

public class AccountController extends Controller
{

    AccountModel model;

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

        navigate("loginView.fxml", 350, 550);

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

    public void changePassword(String username, String password, String confirmationPassword)
    {
        AccountInformation accountInformation = getViewCasted();
        if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty())
        {
            accountInformation.showError("Vul alle velden in");
            return;
        }

        if(!checkPasswords(password,confirmationPassword ))
        {
            accountInformation.showError("De wachtwoorden komen niet overeen");
            return;
        }

        String error = model.changePassword(username, password);

        if(!error.equals(""))
        {
            accountInformation.showError(error);
            return;
        }
    }

    public String getUsername()
    {
        return model.getUsername();
    }
}
