package view;

import controller.App;
import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;

public abstract class View {
    protected Scene scene;
    private App application;

    public void setApp(App app) {
        this.application = app;
        this.scene = this.application.getScene();
        this.loadFinished();
    }

    public <T extends Controller> T getController(Class<T> cType) throws Exception {
        return this.application.getController(cType);
    }

    protected abstract void loadFinished();


    protected void ScaleScreen(Region parent) {
        final double newWidth = this.scene.getWidth();
        final double newHeight = this.scene.getHeight();

        final double ratio = 1;

        double scaleFactor =
                newWidth / newHeight > ratio ? newHeight / 1200 : newWidth / 1600 ;

        if (scaleFactor > 1) {
            Scale scale = new Scale(scaleFactor, scaleFactor);
            scale.setPivotX(0);
            scale.setPivotY(0);
            this.scene.getRoot().getTransforms().setAll(scale);

            parent.setPrefWidth(newWidth / scaleFactor);
            parent.setPrefHeight(newHeight / scaleFactor);
        } else {
            parent.setPrefWidth(Math.max(1600, newWidth));
            parent.setPrefHeight(Math.max(1200, newHeight));
        }
    }
}
