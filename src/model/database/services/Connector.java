package model.database.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private static final String MYSQL_DEFAULT_CONNECTION_STRING = "jdbc:mysql://";
    private static final int MYSQL_DEFAULT_CONNECTION_PORT = 3306;
    private static final String MYSQL_CONNECTION_OPTIONS = "?allowMultiQueries=true";


    public Connection connect(String url, Integer port, String username, String password, String scheme) throws SQLException {
        return this.establishConnection(String.format("%s%s:%s/%s%s", MYSQL_DEFAULT_CONNECTION_STRING, url, port, scheme, MYSQL_CONNECTION_OPTIONS), username, password);

    }

    public Connection connect(String url, String username, String password, String scheme) throws SQLException {
        return this.establishConnection(String.format("%s%s:%s/%s%s", MYSQL_DEFAULT_CONNECTION_STRING, url, MYSQL_DEFAULT_CONNECTION_PORT, scheme, MYSQL_CONNECTION_OPTIONS), username, password);
    }

    /*
        Establish connection
     */
    private Connection establishConnection(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);

    }

}
