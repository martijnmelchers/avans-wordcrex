package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.DriverManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        dbTest();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void dbTest()
    {
        String DB_URL = "jdbc:mysql://databases.aii.avans.nl:3306/";
        String SETTINGS = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        String USER = "sjjverba_db2";
        String PASS = "Ab12345";

        try{
            var connection = DriverManager.getConnection(DB_URL+SETTINGS,USER,PASS);
            new Alert(Alert.AlertType.CONFIRMATION, "db connection succes").showAndWait();
        }
        catch(Exception e)
        {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }
}
