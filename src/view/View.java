package view;

import controller.App;
import controller.Controller;
import javafx.scene.Scene;

public abstract class View {
    private App application;

    protected Scene scene;
    public void setApp(App app) {
        application = app;
        loadFinished();
    }

    protected <T extends Controller> T getController(Class<T> cType) throws Exception {
        return application.getController(cType);
    }

    protected abstract void loadFinished();
}
