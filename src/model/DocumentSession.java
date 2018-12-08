package model;

import model.database.services.Connector;
import model.database.services.Database;

import java.sql.Connection;

public class DocumentSession
{

    private static String playerUsername;

    public static String getPlayerUsername()
    {
        return playerUsername;
    }

    public static void setPlayerUsername(String playerUsername)
    {
        DocumentSession.playerUsername =  playerUsername;
    }

    private static Database database;

    private static synchronized Database getConnection(boolean debug)
    {
        if(database == null)
        {
            try {
                Connection conn = new Connector().connect(EnvironmentVariables.CONN_IP, EnvironmentVariables.CONN_USERNAME, EnvironmentVariables.CONN_PASSWORD, EnvironmentVariables.CONN_TABLE);
                database = new Database(conn, debug);
                return database;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        return database;
    }

    public static Database getDatabase(boolean debug)
    {
        return DocumentSession.getConnection(debug);
    }

    public static Database getDatabase()
    {
        return DocumentSession.getConnection(false);
    }
}
