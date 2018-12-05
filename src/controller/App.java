package controller;


import view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class App extends Application
{
    private ArrayList<Controller> controllers;
    private Stage primaryStage;
    private View view;

    private Scene scene;

    public void load(String startingFxml)
    {
        launch(startingFxml);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Wordcrex");
        primaryStage.show();
        loadControllers();
        navigate(this.getParameters().getRaw().get(0));
    }

    private void loadControllers()
    {
        controllers = new ArrayList<>();
        File[] files;
        try
        {
            files = new File(App.class.getResource("/controller").toURI().getPath()).listFiles();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }

        for(File file : files)
        {
            try
            {
                Class<?> controllerClass = ClassLoader.getSystemClassLoader().loadClass("controller."+file.getName().replace(".class","" ));
                if(controllerClass.isAssignableFrom(Controller.class))
                {
                    continue;
                }
                Controller controllerInstance = (Controller) controllerClass.getConstructor().newInstance();
                controllers.add(controllerInstance);
            }
            catch (Exception e)
            {
                continue;
            }
        }


    }

    public <T extends Controller> T getController(Class<T> cType)
    {
        for (Controller c : controllers)
        {
            try
            {
                if(c.getClass().isAssignableFrom(cType));
                {
                    c.setApp(this);
                    return cType.cast(c);
                }
            }
            catch (Exception e) { }
        }
        new Exception("Controller not found").printStackTrace();
        return null;
    }

    public <T> T getViewCasted()
    {
        try
        {
            return (T)view;
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public View getView()
    {
        return view;
    }

    public void navigate(String fxmlFileName)
    {
        navigate(fxmlFileName, 600,400 );
    }

    public void navigate(String fxmlFileName, int width, int height)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/"+fxmlFileName));
            Parent root = fxmlLoader.load();

            if(scene == null)
            {
                scene = new Scene(root);
            }
            else
            {
                scene.setRoot(root);
            }

            view = fxmlLoader.getController();
            view.setApp(this);
            primaryStage.setScene(scene);
            primaryStage.setHeight(height);
            primaryStage.setWidth(width);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
