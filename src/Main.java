import controller.App;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.EnvironmentVariables;
import model.GameSession;
import model.database.DocumentSession;
import model.database.classes.Clause;
import model.database.classes.TableAlias;
import model.database.enumerators.CompareMethod;
import model.helper.Log;
import model.tables.Account;
import model.tables.AccountInfo;
import model.tables.Role;

import java.util.ArrayList;

public class Main extends Application {
    public static void main(String[] args) {
        Log.info("Launching application...");
        launch(EnvironmentVariables.MAIN_VIEW);
    }


    @Override
    public void start(Stage primaryStage) {
        initializeApp(primaryStage);
    }

    private void initializeApp(Stage primaryStage) {
        var tryAgainButton = new ButtonType("Probeer opnieuw");
        var closeAppButton = new ButtonType("Afsluiten");

        /* Initialize the database */
        Log.info("Initializing database...");
        try {
            DocumentSession.getDatabase();
            Log.info("Database connection established!");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Tijdens opstarten van de database is de volgende fout opgetreden:\n\n" + e.getMessage(), closeAppButton, tryAgainButton);
            alert.showAndWait();

            Log.error(e);

            if (alert.getResult() == tryAgainButton)
                initializeApp(primaryStage);
            else
                System.exit(1);
        }

        /* Start the main app */
        Log.info("Starting views...");
        try {
            var app = new App(primaryStage);

            app.navigate(EnvironmentVariables.MAIN_VIEW, 350, 550);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Er is een fatale fout opgetreden tijdens het starten van de applicatie!\n\n" + e.getMessage(), closeAppButton);
            alert.showAndWait();

            Log.error(e);

            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new BeforeShutdown());
    }
}
