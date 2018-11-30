package model;

import model.database.services.Connector;
import model.database.services.Database;
import model.tables.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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