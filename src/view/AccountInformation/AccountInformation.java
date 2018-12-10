package view.AccountInformation;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.helper.ErrorHandler;
import view.View;

public class AccountInformation extends View {

    private AccountController accountController;
    @FXML private Label username;
    @FXML private Label role;
    @FXML private Label error;
    @FXML private TextField password;
    @FXML private TextField confirmationPassword;

    @Override
    protected void loadFinished()
    {
        try {
            accountController = this.getController(AccountController.class);
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
        getAccountInformation();
    }

    private void getAccountInformation()
    {
        role.setText(accountController.getRole());
        username.setText(accountController.getUsername());
    }

    public void changePassword()
    {
        accountController.changePassword(username.getText(), password.getText(), confirmationPassword.getText());
    }

    public void backMain()
    {
        try {
            this.getController(AccountController.class).navigate("MatchOverview");
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }

    public void showError(String errorMessage)
    {
        error.setText(errorMessage);
        error.setVisible(true);
    }
}
