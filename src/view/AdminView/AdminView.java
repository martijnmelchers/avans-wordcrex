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
    private ObservableList<String> roleViewItems;
    private FilteredList<String> filteredNames;

    @FXML
    private TextField searchField;

    public AdminView() {
        try {
            this.controller = this.getController(AdminController.class);
        } catch (Exception e) {
            Log.error(e);
        }
    }


    @FXML
    public void filter() {
        String filter = searchField.getText();
        this.filteredNames = new FilteredList<>(listViewItems, s -> true);
        if (filter == null || filter.length() == 0) {
            filteredNames.setPredicate(s -> true);
        } else {
            filteredNames.setPredicate(s -> s.contains(filter));
        }
        userList.setItems(filteredNames);
    }

    public void initialize() {
        userList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        roleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        userList.setOnMouseClicked((EventHandler<Event>) event -> {
            ObservableList<String> items = userList.getSelectionModel().getSelectedItems();
            if (items.size() == 1) {
                showRoles(items.get(0));
            }
        });


    }

    @Override
    protected void loadFinished() {
        showInfo();
    }


    private void showInfo() {
        if (this.filteredNames != null) {
            this.filteredNames.setPredicate(s -> true);
            this.searchField.setText("");
        }

        this.listViewItems = this.userList.getItems();
        this.roleViewItems = this.roleList.getItems();


        if (this.filteredNames == null) {
            this.listViewItems.clear();
        }
        this.roleViewItems.clear();

        var userList = this.controller.getUserList();
        var roleList = this.controller.getRoles();

        for (Role role : roleList)
            this.roleViewItems.add(role.getRole());

        for (AccountInfo user : userList) {
            if (!listViewItems.contains(user.getAccount().getUsername())) {
                listViewItems.add(user.getAccount().getUsername());
            }
        }
    }

    @FXML
    private void assignRoles() {
        ObservableList<String> selectedUsers = userList.getSelectionModel().getSelectedItems();

        for (String user : selectedUsers) {

            int index = 0;
            for (String role : this.roleList.getItems()) {
                if (!this.roleList.getSelectionModel().isSelected((index++))) {
                    this.controller.removeRole(user, role);
                } else {
                    this.controller.setRole(user, role);
                }
            }
        }
        this.showInfo();
        new Alert(Alert.AlertType.CONFIRMATION, "Success").show();
    }

    private void showRoles(String username) {
        List<AccountInfo> roles = this.controller.getRoles(username);
        roleList.getSelectionModel().clearSelection();
        for (String role : roleList.getItems()) {
            for (AccountInfo userRole : roles) {
                if (role.equals(userRole.getRole().getRole())) {
                    roleList.getSelectionModel().select(role);
                }
            }
        }
    }
}
