import model.DocumentSession;

import java.sql.SQLException;

public class BeforeShutdown extends Thread {
    @Override
    public void run() {
        /* This will be run when the app is closed */
        try {
            DocumentSession.getDatabase().close();
            System.out.println("Database connection closed. Closing application");
        } catch (SQLException e) {
            System.out.println("An error occurred shutting down the application!");
        }
    }
}
