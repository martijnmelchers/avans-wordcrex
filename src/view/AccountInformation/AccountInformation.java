package view.AccountInformation;

import controller.AccountController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import view.View;

public class AccountInformation extends View {

    private AccountController accountController;
    @FXML private Label username;
    @FXML private Label error;
    @FXML private TextField password;
    @FXML private TextField confirmationPassword;

    @Override
    protected void loadFinished()
    {
        accountController = this.getController(AccountController.class);
        getAccountInformation();
    }

    private void getAccountInformation()
    {
        username.setText(accountController.getUsername());
    }

    public void changePassword()
    {
        accountController.changePassword(username.getText(), password.getText(), confirmationPassword.getText());
    }

    public void backMain()
    {
        accountController.navigate("BoardView");
    }

    public void showError(String errorMessage)
    {
        error.setText(errorMessage);
        error.setVisible(true);
    }
}
