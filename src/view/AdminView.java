package view;

import controller.AdminController;
import controller.LoginController;
import model.AdminModel;
import model.tables.Account;

import java.util.ArrayList;
import java.util.List;

public class AdminView extends View {
    private List<Account> userList = new ArrayList<>();
    private AdminController _controller;

    public AdminView(){
        this._controller = new AdminController();
    }
    
    public void initialize(){
        /**
         * Jay Deze dingetje word gedraait en print alle passwords van alle users., gebruikt ac.getUsername voor de username van de gebruiker
         */
        this.userList = _controller.getUserList();
        for (Account ac : this.userList) {
            System.out.println(ac.getPassword());
        }
    }

}
