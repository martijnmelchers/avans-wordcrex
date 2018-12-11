package controller;

import model.helper.Log;
import view.View;

import java.io.IOException;

public abstract class Controller
{
    private App application;

    protected void setApp(App app)
    {
        application = app;
    }

    protected <T> T getViewCasted()
    {
        return application.getViewCasted();
    }

    protected View getView()
    {
        return application.getView();
    }

    public App getApplication()
    {
        return application;
    }

    public void navigate(String fxmlFileName, int width, int height) throws IOException {
        application.navigate(fxmlFileName, width, height);
    }

    public void navigate(String fxmlFileName)
    {
        try
        {
            application.navigate(fxmlFileName);
        }
        catch (Exception e)
        {
            Log.error(e,false );
        }
    }
}
