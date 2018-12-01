package controller;

import model.AdminModel;
import model.tables.Account;
import view.AdminView;
import view.View;

import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {
    private AdminModel adminModel;
    private List<Account> userList = new ArrayList<>();

    public AdminController(){
        this.adminModel = new AdminModel();
    }

    public List<Account> getUserList(){
        return this.adminModel.getUsers();
    }
}
