package view.DockView;

import controller.GameController;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.helper.Log;
import model.tables.HandLetter;
import view.BoardView.BoardView;

import java.util.List;

public class DockView
{
    private GameController controller;
    private BoardView board;

    @FXML HBox hBoxDock;

    public void setParent(BoardView board)
    {
        this.board = board;

        try{
            controller = board.getController(GameController.class);
        } catch (Exception e){
            Log.error(e);
        }


        updateDock();
    }

    public void updateDock()
    {
        HandLetter[] letters = controller.getDock();
        makeTransparent();
        for (HandLetter letter : letters) {
            if(letter!=null)
            {
                addCharacter(letter.letter.get_symbol()+"",letter.letter.get_letterId());
            }
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
        getTile(tile);
    }

    private void getTile(StackPane stackPane)
    {
        Rectangle tile = (Rectangle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        List<Node> children = board._gridPane.getChildren();

        for (int i = 0; i < 225; i++)
        {
            int row = i/15;
            int column = i%15;
            Node child = children.get(i);
            Bounds b = tile.localToScreen(tile.getBoundsInLocal());
            double x = ((b.getMinX() + b.getMaxX())/2);
            double y = ((b.getMinY() + b.getMaxY())/2);
            Bounds bounds = child.localToScreen(child.getBoundsInLocal());
            
            if(bounds.getMaxY()+1 > y && bounds.getMinY()-1 < y)
            {
                if (bounds.getMaxX() + 1 > x && bounds.getMinX() - 1 < x)
                {
                    if(!controller.tileEmpty(row,column))
                    {
                        continue;
                    }
                    controller.placeTile(row,column ,text.getText(), (int)stackPane.getProperties().get("id"));
                    hBoxDock.getChildren().remove(stackPane);
                    System.out.println("row:" + row);
                    System.out.println("column:" + column);
                    return;
                }
            }
        }
        jumpBack((StackPane) tile.getParent());
    }

    public StackPane addCharacter(String character, double x, double y,int letterId)
    {
        StackPane sp = new StackPane();
        sp.setAlignment(Pos.CENTER);
        sp.setOnMouseDragged(e -> blockMoved(e,sp));
        sp.setOnMouseReleased(e-> blockReleased(e,sp ));
        sp.getProperties().put("id", letterId);
        Rectangle r = new Rectangle();
        r.setFill(Color.WHITE);
        r.setArcHeight(10);
        r.setArcWidth(10);
        r.setWidth(30);
        r.setHeight(30);
        Text t = new Text();
        t.setText(character);
        sp.getChildren().add(r);
        sp.getChildren().add(t);
        hBoxDock.getChildren().add(sp);
        if(x != 0 && y!=0)
        {
            Bounds  b = sp.localToScene(sp.getBoundsInLocal());
            double currentX = ((b.getMinX() + b.getMaxX())/2);
            double currentY = ((b.getMinY() + b.getMaxY())/2);
            double Xdiff = x - currentX;
            double Ydiff = y - currentY;
            double extraOffset = (7-hBoxDock.getChildren().size()) * 30;
            System.out.println( (Xdiff) - 270 +  " " + extraOffset);
            sp.setTranslateX((Xdiff ) - 180 + extraOffset);
            sp.setTranslateY((Ydiff) - 39);
        }
        sp.getParent().getParent().toFront();
        return sp;
    }

    public void addCharacter(String character,int letterId)
    {
        addCharacter(character,0,0,letterId);
    }

    private void jumpBack(StackPane sp)
    {
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), sp);
        tt.setToX(0);
        tt.setToY(0);
        tt.setAutoReverse(true);
        tt.play();
    }
}
