package model;

import model.database.services.Connector;
import model.database.services.Database;

import java.sql.Connection;
import java.sql.SQLException;

public class DocumentSession
{
    private static Database database;

    private static String playerUsername;

    private static synchronized Database getConnection(boolean debug) throws SQLException {
        if(database == null)
        {
            Connection conn = new Connector().connect(EnvironmentVariables.CONN_IP, EnvironmentVariables.CONN_USERNAME, EnvironmentVariables.CONN_PASSWORD, EnvironmentVariables.CONN_TABLE);
            database = new Database(conn, debug);
            return database;
        }

        return database;
    }

    public static String getPlayerUsername() {
        return playerUsername;
    }

    public static void setPlayerUsername(String playerUsername) {
        DocumentSession.playerUsername = playerUsername;
    }

    public static Database getDatabase(boolean debug) throws SQLException {
        return DocumentSession.getConnection(debug);
    }

    public static Database getDatabase() throws SQLException {
        return DocumentSession.getConnection(false);
    }
}
