package controller;

import com.mysql.jdbc.log.Log;
import model.AccountModel;
import view.LoginView;
import view.RegisterView;

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

        if(model.registerAccount(username,password ))
        {
            registerView.showError("Registreren mislukt");
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
}
