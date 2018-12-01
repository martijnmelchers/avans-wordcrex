package view;

import controller.MatchOverviewController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


public class MatchOverview extends View
{
    private MatchOverviewController controller;

    @FXML
    private ScrollPane m_matchScrollPane;

    public MatchOverview()
    {
    }

    public void start()
    {
        controller = this.getController(MatchOverviewController.class);

        VBox root = new VBox();
        root.getChildren().addAll(new Button("test"),
                                    new Button("test2"));


        m_matchScrollPane.setContent(root);
    }
}