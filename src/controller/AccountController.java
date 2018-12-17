package controller;

import model.AccountModel;
import model.GameSession;
import model.helper.Log;
import model.tables.AccountInfo;
import model.tables.Role;
import view.AccountInformation.AccountInformation;
import view.LoginView.LoginView;
import view.RegisterView.RegisterView;

import java.util.ArrayList;

public class AccountController extends Controller {

    private AccountModel _model;

    public AccountController() {
        this._model = new AccountModel();
    }

    private boolean passwordsMatch(String password, String confirmationPassword) {
        return password.equals(confirmationPassword);
    }

    public void registerUser(String username, String password, String confirmationPassword) {
        RegisterView registerView = this.getViewCasted();

        try {
            if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty())
                throw new Exception("Vul alle velden in.");

            if (!this.passwordsMatch(password, confirmationPassword))
                throw new Exception("Wachtwoorden komen niet overeen.");

            this._model.register(username, password);

            var account = this._model.login(username, password);

            GameSession.setSession(account.getAccount());


            GameSession.setRoles(getAccountRoles(account));
            registerView.registerSuccess();
        } catch (Exception e) {
            Log.error(e);
            registerView.showError(e.getMessage());
        }
    }


    private ArrayList<Role> getAccountRoles(AccountInfo account){

        var accountInfos = this._model.getRoles(account);
        var roleList = new ArrayList<Role>();
        for (var role : accountInfos){
            roleList.add(role.getRole());
        }
        return roleList;
    }

    public void loginUser(String username, String password) {
        LoginView loginView = this.getViewCasted();

        try {
            if (username.isEmpty() || password.isEmpty())
                throw new Exception("Vul alle velden in");

            var account = this._model.login(username, password);

            GameSession.setSession(account.getAccount());
            GameSession.setRoles(getAccountRoles(account));

            loginView.loginSuccess();
        } catch (Exception e) {
            Log.error(e);
            loginView.showError("Wachtwoord of gebruikersnaam kloppen niet!");
        }
    }

    public void changePassword(String username, String password, String confirmationPassword) {
        AccountInformation accountInformation = this.getViewCasted();


        try {
            if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty())
                throw new Exception("Vul alle velden in");

            if (!this.passwordsMatch(password, confirmationPassword))
                throw new Exception("De wachtwoorden komen niet overeen");

            this._model.changePassword(username, password);
        } catch (Exception e) {
            Log.error(e);
            accountInformation.showError(e.getMessage());
        }
    }
}
