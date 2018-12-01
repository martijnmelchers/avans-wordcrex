package model;

import model.database.classes.Clause;
import model.database.services.Connector;
import model.database.services.Database;
import model.tables.Account;
import model.tables.AccountInfo;
import model.tables.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminModel {
    private Database _db;
    /**
     * Instantiate database connection
     */
    public AdminModel(){
        try{
            Connection conn = new Connector().connect("databases.aii.avans.nl", "fjmelche", "Ab12345", "smendel_db2");
            this._db = new Database(conn);
        }
        catch(Exception e){

        }
    }

    /**
     * Haalt alle users uit de db.
     * @return List<Account>
     */
    public List<Account> getUsers(){

        var clauses = new ArrayList <Clause>();
        List<Account> accounts = new ArrayList<>();

        try{
            accounts = this._db.select(Account.class,clauses);
        }

        catch(Exception e){

        }
        return accounts;
    }

    public void createRole(Role role) throws Exception {
        try{
            this._db.insert(role);
        }
        catch (Exception e){
            throw e;
        }
    }

    public void removeRole(){
        //TODO
    }

    public void assignRole(){
        //TODO
    }

    public void deAssignRole(){
        //TODO
    }
}