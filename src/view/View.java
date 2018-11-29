package view;

import controller.App;
import controller.Controller;
import javafx.fxml.FXML;

public abstract class View
{
    private App application;

    public void setApp(App app)
    {
        application = app;
        loadFinished();
    }

    protected  <T extends Controller> T getController()
    {
        return application.getController();
    }

    protected abstract void loadFinished();
}
