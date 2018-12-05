package model;

import model.database.services.Connector;
import model.database.services.Database;

import java.sql.Connection;

public abstract class Model
{
    public static Connection connection;
    public static Database db;

    public static Database getConnection()
    {
        if(connection == null)
        {
            try {
                Connection conn = new Connector().connect(EnvironmentVariables.ip, EnvironmentVariables.username, EnvironmentVariables.password, EnvironmentVariables.table);
                db = new Database(conn);
                return db;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public Database getDatabase()
    {
        return Model.getConnection();
    }
}
