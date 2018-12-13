package view.BoardView;

import controller.GameController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Tile;
import model.TileState;
import model.helper.Log;
import view.DockView.DockView;
import view.View;



public class BoardView extends View {

    @FXML public GridPane _gridPane;

    @FXML private DockView dockController;

    @FXML private Slider _slider;

    @FXML private Text _scoreP1;
    @FXML private Text _scoreP2;
    @FXML private Text _addedScore;

    @FXML private Label labelLoadingScreen;

    @FXML private VBox vboxLoadingScreen;

    private GameController _controller;

    @Override
    protected void loadFinished() {
        try {
            _controller = this.getController(GameController.class);
        } catch (Exception e){
            Log.error(e);
        }

        dockController.setParent(this);
        init();
        updateScore();
    }

    public void update()
    {
        clearGrid();
        init();
    }

    private void updateScore(){
        String[] playerNames = _controller.getPlayerNames();
        int[] scores = _controller.getScore();
        _scoreP1.setText(playerNames[0] + " : " + Integer.toString(scores[0]));
        _scoreP2.setText(playerNames[1] + " : " +Integer.toString(scores[1]));
    }

    public void updateLocalScore(String score){
        _addedScore.setText(score);
    }

    public void clearGrid()
    {
        _gridPane.getChildren().removeAll(_gridPane.getChildren().filtered(a->a instanceof StackPane));
    }

    public void startLoadingScreen()
    {
        startLoadingScreen("");
    }

    public void startLoadingScreen(String message)
    {
        vboxLoadingScreen.setVisible(true);
        labelLoadingScreen.setText(message);
    }

    public void stopLoadingScreen()
    {
        vboxLoadingScreen.setVisible(false);
    }

    public void init()
    {
        Tile[][] tiles = _controller.getTiles();
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                StackPane stackPane = new StackPane();
                Rectangle rect = new Rectangle(30, 30);

                rect.setFill(tiles[y][x].getColor());
                rect.setArcWidth(10);
                rect.setArcHeight(10);

                rect.setOnMousePressed(event-> tileClicked(event,stackPane));

                Text text = new Text();
                text.setMouseTransparent(true);

                var letter = tiles[y][x].getLetterType().getLetter();
                text.setText(letter.equals("") ? tiles[y][x].getType().toString() : tiles[y][x].getLetterType().getLetter());

                GridPane.setRowIndex(stackPane, y);
                GridPane.setColumnIndex(stackPane, x);

                stackPane.getChildren().addAll(rect, text);
                _gridPane.getChildren().add(stackPane);
            }
        }
    }

    @FXML
    private void submitTurn()
    {
        _controller.submitTurn();
        updateScore();
    }

    private void tileClicked(MouseEvent e, StackPane tile)
    {
        Tile[][] tiles = _controller.getTiles();
        int row = GridPane.getRowIndex(tile);
        int col = GridPane.getColumnIndex(tile);
        if(tiles[row][col].getState() == TileState.UNLOCKED)
        {
            String character = ((Text)tile.getChildren().get(1)).getText();
            int letterid = tiles[row][col].getLetterType().getid();
            _controller.resetTile(col,row);
            update();
            StackPane sp = dockController.addCharacter(character,e.getSceneX(),e.getSceneY(),letterid);

            tile.setOnMouseDragged(event-> Event.fireEvent(sp,event));
            tile.setOnMouseReleased(event-> Event.fireEvent(sp,event ));
        }
    }

    public void getTurn(Integer turn){

       //var db = DocumentSession.getDatabase();

    }
}
