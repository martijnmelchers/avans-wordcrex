package view;

import controller.App;
import controller.GameController;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Letter;
import model.Tile;

import java.util.List;

public class DockView
{
    private GameController controller;
    private BoardView board;

    @FXML HBox hBoxDock;

    public void setParent(BoardView board)
    {
        this.board = board;
        controller = board.getController(GameController.class);
        updateDock();
    }

    public void updateDock()
    {
        Letter[] letters = controller.getDock();
        makeTransparent();
        List<Node> children = hBoxDock.getChildren();
        for(int i =0;i<letters.length;i++)
        {
            StackPane stackPane = (StackPane) children.get(i);
            stackPane.setOnMouseDragged(e -> blockMoved(e,stackPane));
            stackPane.setOnMouseReleased(e-> blockReleased(e,stackPane ));
            List<Node> elements = stackPane.getChildren();
            Rectangle block = (Rectangle) elements.get(0);
            Text text  = (Text)elements.get(1);
            text.setText(letters[i].getLetter());
            block.setFill(Color.rgb(255, 255, 255));
        }
    }

    private void makeTransparent()
    {
        for (Node child : hBoxDock.getChildren())
        {
            StackPane tile = (StackPane) child;
            List<Node> elements = tile.getChildren();
            Rectangle block = (Rectangle) elements.get(0);
            block.setFill(Color.TRANSPARENT);
        }
    }

    private void blockMoved(MouseEvent e,StackPane tile)
    {
        tile.setTranslateX(e.getX() + tile.getTranslateX()-15);
        tile.setTranslateY(e.getY() + tile.getTranslateY()-49);
        tile.getParent().getParent().toFront();
    }

    private void blockReleased(MouseEvent e, StackPane tile)
    {
        getTile((Rectangle) tile.getChildren().get(0));
    }

    private void getTile(Rectangle tile)
    {
        Tile[][] tiles = controller.getTiles();
        List<Node> children = board._gridPane.getChildren();

        for (int xi = 0; xi <= 15; xi++)
        {
            for (int yi = 0; yi <= 15; yi++)
            {
                int index = xi*14 + yi;
                Node child = children.get(index);
                Bounds b = tile.localToScreen(tile.getBoundsInLocal());
                double x = ((b.getMinX() + b.getMaxX())/2);
                double y = ((b.getMinY() + b.getMaxY())/2);
                Bounds bounds = child.localToScreen(child.getBoundsInLocal());
                if(bounds.getMaxY()+1 > y && bounds.getMinY()-1 < y)
                {
                    if(bounds.getMaxX()+1 > x && bounds.getMinX()-1 < x)
                    {
                        System.out.println("X:" +xi);
                        System.out.println("Y:" +yi);
                        return;
                    }
                }
            }
        }
    }
}
