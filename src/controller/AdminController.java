package controller;

import model.AdminModel;
import model.helper.ErrorHandler;
import model.tables.Account;
import model.tables.AccountInfo;
import model.tables.Role;
import view.AdminView.AdminView;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {
    private AdminModel adminModel;
    private List<Account> userList = new ArrayList<>();

    public AdminController(){
        this.adminModel = new AdminModel();
    }

    public List<AccountInfo> getUserList(){
        return this.adminModel.getUsers();
    }


    public void setRole(String username, String role){
        AccountInfo info = new AccountInfo(role, username);
        try{
            this.adminModel.setRole(info);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    public void removeRole(String username, String role){
        AccountInfo info = new AccountInfo(role, username);
        try{
            this.adminModel.removeRole(info);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<AccountInfo> getRoles(String username){
        return this.adminModel.getRoles(username);
    }
}
