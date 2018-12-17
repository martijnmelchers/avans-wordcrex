package view;

import controller.App;
import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;

public abstract class View {
    private App application;

    protected Scene scene;

    public void setApp(App app) {
        application = app;
        scene = application.getScene();
        loadFinished();
    }
  
    public  <T extends Controller> T getController(Class<T> cType) throws Exception {
        return application.getController(cType);
    }

    protected abstract void loadFinished();


    protected void ScaleScreen(Region parent)
    {
        final double newWidth = scene.getWidth();
        final double newHeight = scene.getHeight();

        final double ratio = 1;

        double scaleFactor =
                newWidth / newHeight > ratio ? newHeight / 1200 : newWidth / 1600 ;

        if (scaleFactor > 1) {
            Scale scale = new Scale(scaleFactor, scaleFactor);
            scale.setPivotX(0);
            scale.setPivotY(0);
            scene.getRoot().getTransforms().setAll(scale);

            parent.setPrefWidth(newWidth / scaleFactor);
            parent.setPrefHeight(newHeight / scaleFactor);
        } else {
            parent.setPrefWidth(Math.max(1600, newWidth));
            parent.setPrefHeight(Math.max(1200, newHeight));
        }
    }
}
