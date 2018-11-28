package controller;

import view.View;

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

    public void navigate(String fxmlFileName)
    {
        application.navigate(fxmlFileName);
    }

    public void navigate(String fxmlFileName, int width, int height)
    {
        application.navigate(fxmlFileName, width, height);
    }

    public void navigate(String fxmlFileName, int width, int height)
    {
        application.navigate(fxmlFileName, width, height);
    }

}
