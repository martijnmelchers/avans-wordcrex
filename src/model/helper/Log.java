package model.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.EnvironmentVariables;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
    private static String ERROR_COLOR = "\u001B[31m(ERROR)\u001B[0m";
    private static String ERROR = "(ERROR)";
    private static String INFO_COLOR = "\u001B[34m(INFO)\u001B[0m";
    private static String INFO = "(INFO)";
    private static String WARNING_COLOR = "\u001B[33m(WARN)\u001B[0m";
    private static String WARNING = "(WARN)";
    private static String QUERY_COLOR = "\u001B[35m(QUERY)\u001B[0m";
    private static String QUERY = "(QUERY)";


    public static void info(String content) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        if (EnvironmentVariables.DEBUG_LEVEL >= 3)
            System.out.println("[" + timeStamp + "] " + (EnvironmentVariables.USE_COLOR ? INFO_COLOR : INFO) + " " + content);
    }

    public static void warn(String content) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        if (EnvironmentVariables.DEBUG_LEVEL >= 2)
            System.out.println("[" + timeStamp + "] " + (EnvironmentVariables.USE_COLOR ? WARNING_COLOR : WARNING) + " " + content);
    }

    public static void error(Exception exception) {
        Log.error(exception, false);
    }

    public static void error(Exception exception, boolean visual) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        if (EnvironmentVariables.DEBUG_LEVEL >= 1) {
            System.out.print("[" + timeStamp + "] " + (EnvironmentVariables.USE_COLOR ? ERROR_COLOR : ERROR) + " ");
            exception.printStackTrace();

        }

        if (visual) {
            var closeAppButton = new ButtonType("Oké");

            Alert alert = new Alert(Alert.AlertType.ERROR, "Er is een fatale fout opgetreden!\n\n" + exception.getMessage(), closeAppButton);
            alert.showAndWait();
        }
    }

    public static void query(String query) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        if (EnvironmentVariables.LOG_QUERIES)
            System.out.println("[" + timeStamp + "] " + (EnvironmentVariables.USE_COLOR ? QUERY_COLOR : QUERY) + " " + query);
    }
}
