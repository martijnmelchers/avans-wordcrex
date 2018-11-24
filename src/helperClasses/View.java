package helperClasses;

public abstract class View
{
    private App application;

    public void setApp(App app)
    {
        application = app;
    }

    protected  <T extends Controller> T getController()
    {
        return application.getController();
    }
}
