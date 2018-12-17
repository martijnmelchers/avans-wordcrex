package view.BoardView;

import controller.GameController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.GameSession;
import model.Letter;
import model.Tile;
import model.TileState;
import model.helper.Log;
import view.DockView.DockView;
import view.View;

import java.util.stream.Collectors;

import static javafx.scene.paint.Color.rgb;


public class BoardView extends View {

    @FXML public GridPane _gridPane;

    @FXML private DockView dockController;

    @FXML private Slider _slider;

    @FXML private Text _scoreP1;
    @FXML private Text _scoreP2;
    @FXML private Text _addedScore;
    @FXML private Text _tilesLeft;

    @FXML private Button _submit;
    @FXML private Button _pass;
    @FXML private Button _reset;
    @FXML private Button _shuffle;
    @FXML private Button _surrender;
    @FXML private Button _home;

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
        updateTilesLeft();
        checkRole();
        checkIfTurnPlayed();
        _slider.setDisable(_controller.getCurrentTurn() == 1);
    }

    private void checkRole()
    {
        if (GameSession.getRole().getRole().equals("observer"))
        {
            _controller.getOldDock(_controller.getCurrentTurn());
            dockController.updateDock();
            GridPane block = new GridPane();
            block.setId("block");
            _gridPane.add(block, 0, 0, 15, 17);
            _submit.setVisible(false);
            _pass.setVisible(false);
            _reset.setVisible(false);
            _shuffle.setVisible(false);
            _surrender.setVisible(false);
        }
    }

    public void update(boolean updateDock)
    {
        clearGrid();
        init();
        if (updateDock)
        {
            dockController.updateDock();
            _slider.setDisable(_controller.getCurrentTurn() == 1);
        }
    }

    public void updateScore(){
        String[] playerNames = _controller.getPlayerNames();
        int[] scores = _controller.getScore();
        _scoreP1.setText(playerNames[0] + " : " + Integer.toString(scores[0]));
        _scoreP2.setText(playerNames[1] + " : " +Integer.toString(scores[1]));
    }

    public void updateTilesLeft()
    {
        _tilesLeft.setText("Stenen : " + _controller.getNotUsedTiles());
    }

    private void checkIfTurnPlayed()
    {
        _controller.checkIfTurnPlayed();
    }

    // get older turns
    public void updateTilesLeft(int turn)
    {
        _tilesLeft.setText("Stenen : " + _controller.getNotUsedTiles(turn));
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
                Rectangle rect = new Rectangle(40, 40);

                rect.setFill(tiles[y][x].getColor());
                rect.setArcWidth(10);
                rect.setArcHeight(10);

                rect.setOnMousePressed(event-> tileClicked(event,stackPane));

                Text text = new Text();
                text.setMouseTransparent(true);

                Text p = new Text();
                p.setMouseTransparent(true);
                p.setText(Integer.toString(tiles[y][x].getLetterType().getPoints()));
                p.setFill(rgb(130, 130, 130));
                StackPane.setAlignment(p, Pos.CENTER_LEFT);
                p.setTranslateY(-10);
                p.setTranslateX(2);

                var letter = tiles[y][x].getLetterType().getLetter();
                text.setText(letter.equals("") ? tiles[y][x].getType().toString() : tiles[y][x].getLetterType().getLetter());

                GridPane.setRowIndex(stackPane, y);
                GridPane.setColumnIndex(stackPane, x);

                if(!tiles[y][x].isEmpty()) { stackPane.getChildren().addAll(rect, text, p); }
                else { stackPane.getChildren().addAll(rect, text); }
                _gridPane.getChildren().add(stackPane);
            }
        }
        _slider.setDisable(_controller.getCurrentTurn() == 1);

        _slider.setMin(0);
        _slider.setMax(_controller.getCurrentTurn() - 1);
        _slider.setValue(_controller.getCurrentTurn() - 1);

        _slider.setShowTickLabels(true);
        _slider.setShowTickMarks(true);

        _slider.setMinorTickCount(_controller.getCurrentTurn() - 1);

       _slider.setBlockIncrement(5);
    }

    @FXML
    private void submitTurn()
    {
        _controller.submitTurn();
        updateScore();
        updateTilesLeft();
    }

    @FXML
    private void passTurn()
    {
        _controller.passTurn();
        updateScore();
        updateTilesLeft();
    }

    @FXML
    public void resetTiles()
    {
       for (Tile tile : _controller.resetTiles())
       {
           int letterid = tile.getLetterType().getid();
           dockController.addCharacter(tile.getLetterType().getLetter(), tile.getLetterType());
       }
       update(false);
       updateLocalScore("0p");
    }

    @FXML
    public void shuffleTiles()
    {
        dockController.shuffleDock();
    }

    @FXML
    public void surrender()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Klik op OK om op te geven. \n\nKlik op Cancel om niet op te geven.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                _controller.surrender();
            }
        });
    }

    private void tileClicked(MouseEvent e, StackPane tile)
    {
        Tile[][] tiles = _controller.getTiles();
        int row = GridPane.getRowIndex(tile);
        int col = GridPane.getColumnIndex(tile);
        if(tiles[row][col].getState() == TileState.UNLOCKED)
        {
            String character = ((Text)tile.getChildren().get(1)).getText();
            Letter letter = tiles[row][col].getLetterType();
            _controller.resetTile(col,row);
            update(false);
            StackPane sp = dockController.addCharacter(character,e.getSceneX(),e.getSceneY(), letter);

            tile.setOnMouseDragged(event-> Event.fireEvent(sp,event));
            tile.setOnMouseReleased(event-> Event.fireEvent(sp,event ));
        }
    }

    public void gameDone()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("De game is klaar \n\n " + _controller.getGameWinner() + " is gewonnen met " + _controller.getGameWinnerScore() + " punten");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // TODO navigate to matchoverview
                home();
            }
        });
    }

    @FXML
    private void home(){
        _controller.navigate("MatchOverview");
    }

    @FXML
    public void showTurnOnBoard(){
        if(_controller.getCurrentTurn() == 1)// Return void if there is no history.
            return;

        //TODO update score erbij
        //TODO lock dock
        var snap = Math.round(_slider.getValue());
        _controller.showTurn((int)snap);
        _slider.setValue(snap);
        updateScore();
        updateTilesLeft((int)snap);

        if ((_controller.getCurrentTurn() - 1) != (int)snap || GameSession.getRole().getRole().equals("observer"))
        {
            _controller.getOldDock((int)snap);
            dockController.updateDock();
            GridPane block = new GridPane();
            block.setId("block");
            _gridPane.add(block, 0, 0, 15, 17);
            _submit.setVisible(false);
            _pass.setVisible(false);
            _reset.setVisible(false);
            _shuffle.setVisible(false);
            _surrender.setVisible(false);
        }
        else
        {
            _controller.getOldDock(_controller.getCurrentTurn());
            dockController.updateDock();
           _gridPane.getChildren().removeAll(
                   _gridPane.getChildren().stream()
                           .filter(x -> x.getId() != null && x.getId().equals("block"))
                           .collect(Collectors.toList())
           );
            _submit.setVisible(true);
            _pass.setVisible(true);
            _reset.setVisible(true);
            _shuffle.setVisible(true);
            _surrender.setVisible(true);
        }
    }
}
