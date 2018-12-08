package model.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.EnvironmentVariables;

public class ErrorHandler {
    public static void handle(Exception e) {
        var closeAppButton = new ButtonType("Afsluiten");

        if (EnvironmentVariables.DEBUG)
            e.printStackTrace();

        Alert alert = new Alert(Alert.AlertType.ERROR, "Er is een fatale fout opgetreden!\n\n" + e.getMessage(), closeAppButton);
        alert.showAndWait();

        System.exit(1);
    }
}
