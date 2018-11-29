package controller;

public class AccountController extends Controller
{
    public AccountController()
    {

    }

    public boolean checkPasswords(String password, String confirmationPassword)
    {
        return password.equals(confirmationPassword);
    }

    public boolean registerUser(String username, String password)
    {
        return false;
    }

    public boolean checkPassword(String username,String password)
    {
        return false;
    }

}
