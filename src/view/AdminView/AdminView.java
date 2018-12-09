package view.AdminView;

import controller.AdminController;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import model.helper.ErrorHandler;
import model.tables.Account;
import model.tables.AccountInfo;
import view.View;
import view.moderator.ModeratorView;

import java.util.List;

public class AdminView extends View {
    private AdminController controller;

    @FXML
    private ListView<String> userList;

    @FXML
    private ListView<String> roleList;
    private ObservableList<String> listViewItems;
    private ObservableList<String> roleViewItems;
    private FilteredList<String> filteredNames;

    @FXML
    private TextField searchField;

    public AdminView(){
        this.controller = new AdminController();
    }


    @FXML
    public void filter(){
        String filter = searchField.getText();
        this.filteredNames = new FilteredList<>(listViewItems, s -> true);
        if(filter == null || filter.length() == 0){
            filteredNames.setPredicate(s -> true);
        }
        else{
            filteredNames.setPredicate(s -> s.contains(filter));
        }
        userList.setItems(filteredNames);
    }

    public void initialize(){
        userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        roleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        userList.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ObservableList<String> items = userList.getSelectionModel().getSelectedItems();
                if(items.size() == 1){
                    showRoles(items.get(0));
                }
            }
        });


    }

    @Override
    protected void loadFinished() {
        showInfo();
    }


    private void showInfo(){
        if(this.filteredNames != null){
            this.filteredNames.setPredicate(s -> true);
            this.searchField.setText("");
        }
        listViewItems = userList.getItems();
        roleViewItems = roleList.getItems();


        if(this.filteredNames == null) {
            listViewItems.clear();
        }
        roleViewItems.clear();

        List<AccountInfo> userList = this.controller.getUserList();

        for (AccountInfo user: userList){

            if(!listViewItems.contains(user.account.getUsername())){
                listViewItems.add(user.account.getUsername());
            }

            if(!roleViewItems.contains(user.role.getRole())){
                roleViewItems.add(user.role.getRole());
            }
        }
    }

    @FXML
    private void assignRoles(){
        ObservableList<String> selectedUsers = userList.getSelectionModel().getSelectedItems();

        for (String user : selectedUsers){

            int index = 0;
            for(String role : this.roleList.getItems()){
                if(!this.roleList.getSelectionModel().isSelected((index++))){
                    this.controller.removeRole(user,role);
                }
                else{
                    this.controller.setRole(user,role);
                }
            }
        }
        this.showInfo();
        new Alert(Alert.AlertType.CONFIRMATION,"Success").show();
    }

    private void showRoles(String username){
        List<AccountInfo> roles = this.controller.getRoles(username);
        roleList.getSelectionModel().clearSelection();
        for (String role : roleList.getItems()){
            for (AccountInfo userRole : roles){
                if(role.equals(userRole.role.getRole())){
                    roleList.getSelectionModel().select(role);
                }
            }
        }
    }
}
