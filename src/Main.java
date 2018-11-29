<<<<<<< HEAD
import controller.moderator.ModeratorViewController;
import model.database.services.Connector;
import model.database.services.Database;
import model.moderator.Moderator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/moderator/Moderator.fxml"));
        Parent root = fxmlLoader.load();

        ModeratorViewController controller = fxmlLoader.getController();

        controller.setModerator(new Moderator(new Database(new Connector().connect("databases.aii.avans.nl","ddfschol","Ab12345","smendel_db2"))));
        primaryStage.setTitle("controller/moderator");
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
=======
import controller.App;

public class Main
{
    public static void main(String[] args)
    {
        App application = new App();
        application.load("LoginView.fxml");
    }
}

>>>>>>> development
