import model.database.DocumentSession;
import model.helper.Log;

import java.sql.SQLException;

public class BeforeShutdown extends Thread {
    @Override
    public void run() {
        /* This will be run when the app is closed */
        Log.info("App is shutting down...");
        try {
            DocumentSession.getDatabase().close();
            Log.info("Database connection has successfully been closed.");
        } catch (SQLException e) {
            Log.error(new Exception("An error occurred shutting down the application!"));
        }
    }
}
