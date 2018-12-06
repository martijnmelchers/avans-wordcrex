package view;

import controller.App;
import controller.Controller;
import javafx.scene.Scene;

public abstract class View
{
    private App application;

    protected Scene scene;

    public void setApp(App app)
    {
        application = app;
    }


    public void start() {}

    protected  <T extends Controller> T getController(Class<T> cType)
    {
        return application.getController(cType);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
