import controller.App;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.DocumentSession;
import model.EnvironmentVariables;

public class Main extends Application {
    public static void main(String[] args) {
        launch(EnvironmentVariables.MAIN_VIEW);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeApp(primaryStage);
    }

    private void initializeApp(Stage primaryStage) {
        var tryAgainButton = new ButtonType("Probeer opnieuw");
        var closeAppButton = new ButtonType("Afsluiten");

        /* First initialize database */
        try {
            DocumentSession.getDatabase(EnvironmentVariables.DEBUG);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Tijdens opstarten van de database is de volgende fout opgetreden:\n\n" + e.getMessage(), closeAppButton, tryAgainButton);
            alert.showAndWait();

            if (EnvironmentVariables.DEBUG)
                e.printStackTrace();

            if (alert.getResult() == tryAgainButton)
                initializeApp(primaryStage);
            else
                System.exit(1);
        }

        /* Start the main app */
        try {
            var app = new App(primaryStage);

            app.navigate(EnvironmentVariables.MAIN_VIEW);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Er is een fatale fout opgetreden tijdens het starten van de applicatie!\n\n" + e.getMessage(), closeAppButton);
            alert.showAndWait();

            if (EnvironmentVariables.DEBUG)
                e.printStackTrace();

            System.exit(1);
        }
    }
}
