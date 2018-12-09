package controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.helper.Log;
import view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class App {
    private final boolean _isMaximized = false;

    private ArrayList<Controller> controllers;
    private Stage primaryStage;
    private View view;

    private Scene scene;

    public App(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Wordcrex");
        primaryStage.show();

        loadControllers();
        navigate(this.getParameters().getRaw().get(0));

        view.start();


        if(_isMaximized)
        {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
        }
    }

    private void loadControllers() throws Exception {
        controllers = new ArrayList<>();
        File[] files;
        files = new File(App.class.getResource("/controller").toURI().getPath()).listFiles();


        if (files == null)
            throw new Exception("No files were found!");

        for (File file : files) {
            try {
                Class<?> controllerClass = ClassLoader.getSystemClassLoader().loadClass("controller." + file.getName().replace(".class", ""));
                if (controllerClass.isAssignableFrom(Controller.class)) {
                    continue;
                }

                Controller controllerInstance = (Controller) controllerClass.getConstructor().newInstance();

                controllers.add(controllerInstance);
            } catch (Exception e) {
                Log.warn("Could not create controller: " + e.getMessage());
            }

        }
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

    public void navigate(String fxmlFileName) throws IOException {
        navigate(fxmlFileName, 1600, 1200 );
    }

    public void navigate(String fxmlFileName, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/" + fxmlFileName + "/" + fxmlFileName + ".fxml"));
        Parent root = fxmlLoader.load();

        if (scene == null) {
            scene = new Scene(root);
        } else {
            scene.setRoot(root);
        }

        view = fxmlLoader.getController();
        view.setApp(this);
        primaryStage.setScene(scene);
        primaryStage.setHeight(height);
        primaryStage.setWidth(width);
    }
}
