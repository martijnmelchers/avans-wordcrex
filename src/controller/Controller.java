package controller;

import model.helper.Log;
import view.View;

import java.io.IOException;

public abstract class Controller {
    private App application;

    protected void setApp(App app) {
        application = app;
    }

    protected <T> T getViewCasted() {
        return application.getViewCasted();
    }

    protected View getView() {
        return application.getView();
    }

    public App getApplication() {
        return application;
    }

    public void navigate(String fxmlFileName, int width, int height, boolean maximized) throws IOException {
        this.application.navigate(fxmlFileName, width, height, maximized);
    }

    public void navigate(String fxmlFileName, boolean maximized) {
        try {
            this.application.navigate(fxmlFileName, maximized);
        } catch (Exception e) {
            Log.error(e, false);
        }
    }

    public <T extends Controller> T getController(Class<T> cType) throws Exception {
        return application.getController(cType);
    }

}
