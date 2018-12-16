package model;

import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.database.services.Database;
import model.helper.Log;
import model.tables.AccountInfo;
import model.tables.Role;

import java.util.ArrayList;
import java.util.List;

public class AdminModel {
    private Database _db;

    /**
     * Load the database connection
     */
    public AdminModel() {
        try {
            this._db = DocumentSession.getDatabase();
        } catch (Exception e) {
            Log.error(e, true);
        }
    }

    public List<AccountInfo> getUsers() {
        var clauses = new ArrayList<Clause>();
        List<AccountInfo> accounts = new ArrayList<>();
        try {
            accounts = this._db.select(AccountInfo.class, clauses);
        } catch (Exception e) {
            Log.error(e);
        }
        return accounts;
    }

    public void setRole(AccountInfo info) throws Exception {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("accountrole", -1), "username", CompareMethod.EQUAL, info.getUsername()));
        clauses.add(new Clause(new TableAlias("accountrole", -1), "role", CompareMethod.EQUAL, info.getRole()));
        if (this._db.select(AccountInfo.class, clauses).size() == 0) {
            this._db.insert(info);
        }
    }

    public void removeRole(AccountInfo info) throws Exception {
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("accountrole", -1), "username", CompareMethod.EQUAL, info.getUsername()));
        clauses.add(new Clause(new TableAlias("accountrole", -1), "role", CompareMethod.EQUAL, info.getRole()));

        List<AccountInfo> roles = this._db.select(AccountInfo.class, clauses);
        if (roles.size() > 0) {
            this._db.delete(roles);
        }
    }


    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();

        try {
            roles = this._db.select(Role.class);
        } catch (Exception e) {
            Log.error(e);
        }

        return roles;
    }

    public List<AccountInfo> getRoles(String username) {
        List<AccountInfo> roles = new ArrayList<>();
        var clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new TableAlias("accountrole", -1), "username", CompareMethod.EQUAL, username));
        try {
            roles = this._db.select(AccountInfo.class, clauses);
        } catch (Exception e) {
            Log.error(e);
        }
        return roles;
    }
}