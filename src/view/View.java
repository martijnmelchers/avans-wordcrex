package view;

import controller.App;
import controller.Controller;

public abstract class View
{
    private App application;

    public void setApp(App app)
    {
        application = app;
    }

    protected  <T extends Controller> T getController(Class<T> cType) throws Exception {
        return application.getController(cType);
    }
}
