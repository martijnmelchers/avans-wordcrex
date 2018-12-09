package view;

import controller.GameController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Tile;
import model.TileState;


public class BoardView extends View {

    @FXML public GridPane _gridPane;

    @FXML private DockView dockController;

    private GameController _controller;

    @Override
    protected void loadFinished() {
        _controller = this.getController(GameController.class);
        dockController.setParent(this);
        init();
    }



    public void update()
    {
        clearGrid();
        init();
    }

    public void clearGrid()
    {
        _gridPane.getChildren().removeAll(_gridPane.getChildren().filtered(a->a instanceof StackPane));
    }


    public void init()
    {
        Tile[][] tiles = _controller.getTiles();
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                StackPane stackPane = new StackPane();
                Rectangle rect = new Rectangle(30, 30);

                rect.setFill(tiles[x][y].getColor());
                rect.setArcWidth(10);
                rect.setArcHeight(10);

                rect.setOnMousePressed(event-> tileClicked(event,stackPane));

                Text text = new Text();
                text.setMouseTransparent(true);

                var letter = tiles[x][y].getLetterType().getLetter();
                text.setText(letter.equals("") ? tiles[x][y].getType().toString() : tiles[x][y].getLetterType().getLetter());

                GridPane.setRowIndex(stackPane, x);
                GridPane.setColumnIndex(stackPane, y);

                stackPane.getChildren().addAll(rect, text);
                _gridPane.getChildren().add(stackPane);
            }
        }
    }

    @FXML
    private void submitTurn()
    {
        _controller.submitTurn();
    }

    private void tileClicked(MouseEvent e, StackPane tile)
    {
        Tile[][] tiles = _controller.getTiles();
        int row = GridPane.getRowIndex(tile);
        int col = GridPane.getColumnIndex(tile);
        if(tiles[row][col].getState() == TileState.UNLOCKED)
        {
            String character = ((Text)tile.getChildren().get(1)).getText();
            _controller.resetTile(row,col);
            update();
            StackPane sp = dockController.addCharacter(character,e.getSceneX(),e.getSceneY());

            tile.setOnMouseDragged(event-> Event.fireEvent(sp,event));
            tile.setOnMouseReleased(event-> Event.fireEvent(sp,event ));
        }
    }
}
