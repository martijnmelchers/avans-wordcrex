package model.database.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private static final String MySQLDefaultConnectionString = "jdbc:mysql://";
    private static final int MySQLDefaultConnectionPort = 3306;


    public Connection connect(String url, Integer port, String username, String password, String scheme) throws SQLException {
        return this.establishConnection(String.format("%s%s:%s/%s", MySQLDefaultConnectionString, url, port, scheme), username, password);

    }

    public Connection connect(String url, String username, String password, String scheme) throws SQLException {
        return this.establishConnection(String.format("%s%s:%s/%s", MySQLDefaultConnectionString, url, MySQLDefaultConnectionPort, scheme), username, password);
    }

    private Connection establishConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);

    }

}
