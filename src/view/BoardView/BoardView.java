package view.BoardView;

import controller.BoardController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import model.Tile;
import view.View;

public class BoardView extends View {

    @FXML private GridPane _gridPane;

    private BoardController _controller;

    public BoardView()
    {
        _controller = new BoardController();
    }

    public void initialize()
    {
        System.out.println(_gridPane);

        Tile[][] tiles = _controller.getTiles();

        for (int x = 0; x < tiles.length; x++)
        {
            for (int y = 0; y < tiles[x].length; y++)
            {
                Rectangle rect = new Rectangle(30,30);

                rect.setFill(tiles[x][y].getColor());
                rect.setArcWidth(10);
                rect.setArcHeight(10);

                GridPane.setRowIndex(rect, x);
                GridPane.setColumnIndex(rect, y);

                _gridPane.getChildren().add(rect);

            }
        }
    }

    @Override
    protected void loadFinished() {

    }
}
