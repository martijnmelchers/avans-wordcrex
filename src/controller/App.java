package controller;


import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.helper.Log;
import view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class App {
    private ArrayList<Controller> controllers;
    private Stage primaryStage;
    private View view;

    private Scene _scene;

    public App(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setWidth(350);
        primaryStage.setHeight(550.5);
        primaryStage.setTitle("Wordcrex");
        primaryStage.show();

        loadControllers();
    }

    private void loadControllers() throws Exception {
        controllers = new ArrayList<>();
        controllers.add(new AccountController());
        controllers.add(new AdminController());
        controllers.add(new ChatController());
        controllers.add(new GameController());
        controllers.add(new MainController());
        controllers.add(new MatchFixerController());
        controllers.add(new MatchOverviewController());
        controllers.add(new ModeratorController());
        controllers.add(new ObserverController());
        controllers.add(new PlayerWordRequestController());

    }

    public <T extends Controller> T getController(Class<T> cType) throws Exception {
        for (Controller c : controllers) {
            if (c.getClass().isAssignableFrom(cType)) {
                c.setApp(this);


                return cType.cast(c);
            }
        }

        throw new Exception("Controller not found");
    }

    public <T> T getViewCasted() {
        @SuppressWarnings("unchecked")
        var c = (T) view;

        return c;
    }

    public View getView() {
        return view;
    }

    public void navigate(String fxmlFileName, boolean maximized) throws IOException {
        navigate(fxmlFileName, 800, 600, maximized);
    }

    public void navigate(String fxmlFileName, int width, int height, boolean maximized) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fxmlFileName + "/" + fxmlFileName + ".fxml"));
        Parent root = fxmlLoader.load();

        if (_scene == null) {
            _scene = new Scene(root);
        } else {
            _scene.setRoot(root);
        }
        view = fxmlLoader.getController();
        view.setApp(this);
        primaryStage.setScene(_scene);
        primaryStage.setHeight(height);
        primaryStage.setWidth(width);


        if(maximized)
        {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
        }
    }

    public Scene getScene()
    {
        return _scene;
    }
}
