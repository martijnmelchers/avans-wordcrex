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
import model.helper.Log;
import model.tables.AccountInfo;
import model.tables.Role;
import view.View;

import java.util.List;

public class AdminView extends View {
    private AdminController controller;

    @FXML
    private ListView<String> userList;

    @FXML
    private ListView<String> roleList;
    private ObservableList<String> listViewItems;
    private FilteredList<String> filteredNames;

    @FXML
    private TextField searchField;

    public AdminView() {

    }


    @FXML
    public void filter() {

        String filter = this.searchField.getText();
        this.filteredNames = new FilteredList<>(this.listViewItems, s -> true);
        if (filter == null || filter.length() == 0) {
            this.filteredNames.setPredicate(s -> true);
        } else {
            this.filteredNames.setPredicate(s -> s.contains(filter));
        }
        this.userList.setItems(this.filteredNames);
    }


    @Override
    protected void loadFinished() {

        try {
            this.controller = this.getController(AdminController.class);
        } catch (Exception e) {
            Log.error(e);
        }


        this.userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.roleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.userList.setOnMouseClicked((EventHandler<Event>) event -> {
            ObservableList<String> items = this.userList.getSelectionModel().getSelectedItems();
            if (items.size() == 1) {
                this.showRoles(items.get(0));
            }
        });

        this.showInfo();
    }


    /**
     * Shows all data on the screen,
     */
    private void showInfo() {
        if (this.filteredNames != null) {
            this.filteredNames.setPredicate(s -> true);
            this.searchField.setText("");
        }

        this.listViewItems = this.userList.getItems();
        ObservableList<String> roleViewItems = this.roleList.getItems();


        if (this.filteredNames == null) {
            this.listViewItems.clear();
        }
        roleViewItems.clear();

        var userList = this.controller.getUserList();
        var roleList = this.controller.getRoles();

        for (Role role : roleList)
            roleViewItems.add(role.getRole());

        for (AccountInfo user : userList) {
            if (!this.listViewItems.contains(user.getAccount().getUsername())) {
                this.listViewItems.add(user.getAccount().getUsername());
            }
        }
    }

    /**
     * Assigns all selected roles to the selected user.
     */
    @FXML
    private void assignRoles() {
        ObservableList<String> selectedUsers = this.userList.getSelectionModel().getSelectedItems();

        for (String user : selectedUsers) {

            int index = 0;
            for (String role : this.roleList.getSelectionModel().getSelectedItems()) {


                    this.controller.setRole(user, role);

            }
        }
        this.showInfo();
        new Alert(Alert.AlertType.CONFIRMATION, "Success").show();
    }
    @FXML
    private void removeRoles() {
        ObservableList<String> selectedUsers = this.userList.getSelectionModel().getSelectedItems();

        for (String user : selectedUsers) {

            int index = 0;
            for (String role : this.roleList.getSelectionModel().getSelectedItems()) {


                this.controller.removeRole(user, role);

            }
        }
        this.showInfo();
        new Alert(Alert.AlertType.CONFIRMATION, "Success").show();
    }
    @FXML
    private void goBack(){
        try {
            this.getController(AdminController.class).navigate("MatchOverview",true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the selected user its rows.
     * @param username
     */
    private void showRoles(String username) {
        List<AccountInfo> roles = this.controller.getRoles(username);
        this.roleList.getSelectionModel().clearSelection();
        for (String role : this.roleList.getItems()) {
            for (AccountInfo userRole : roles) {
                if (role.equals(userRole.getRole().getRole())) {
                    this.roleList.getSelectionModel().select(role);
                }
            }
        }
    }
}
