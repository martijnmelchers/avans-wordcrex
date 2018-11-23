package Moderator;

import Moderator.Controller.ModeratorViewController;
import Moderator.Model.Moderator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


import java.net.URL;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View/Moderator.fxml"));
        Parent root = fxmlLoader.load();

        ModeratorViewController controller = fxmlLoader.getController();

        controller.setModerator(new Moderator());
        primaryStage.setTitle("Moderator");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    public static void main(String[] args) {
        /*Block t = Block.B1;
        for ( int i = 1; i < 20; i++){
            t = t.getBlock(i);
        }*/
        launch(args);
    }


}