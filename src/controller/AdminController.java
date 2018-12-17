package controller;

import model.AdminModel;
import model.helper.Log;
import model.tables.AccountInfo;
import model.tables.Role;

import java.util.List;

public class AdminController extends Controller {
    private AdminModel adminModel;


    public AdminController() {
        this.adminModel = new AdminModel();
    }

    public List<AccountInfo> getUserList() {
        return this.adminModel.getUsers();
    }


    public void setRole(String username, String role) {
        AccountInfo info = new AccountInfo(role, username);
        try {
            this.adminModel.setRole(info);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }


    public void removeRole(String username, String role) {
        AccountInfo info = new AccountInfo(role, username);
        try {
            this.adminModel.removeRole(info);
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public List<AccountInfo> getRoles(String username) {
        return this.adminModel.getRoles(username);
    }

    public List<Role> getRoles() {
        return this.adminModel.getAllRoles();
    }
}
