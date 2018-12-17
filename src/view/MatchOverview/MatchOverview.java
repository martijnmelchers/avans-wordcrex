package view.MatchOverview;

import controller.MatchOverviewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import model.Board;
import model.GameModel;
import model.GameSession;
import model.MatchOverviewModel;
import model.helper.Log;
import model.tables.Game;
import view.View;

import java.util.List;

public class MatchOverview extends View {

    @FXML
    private ListView gameListview;

    @FXML
    private ListView gameListview1;

    @FXML
    private ListView gameListview2;

    @FXML
    private GridPane _gridParent;

    private ObservableList<Game> gameObservableList;

    private ObservableList<Game> gameObservableList1;

    private ObservableList<Game> gameObservableList2;

    private MatchOverviewController _controller;

    @FXML
    private Button _observerModeButton;

    @FXML
    private TextField _searchBar;


    public MatchOverview(){

    }

    public void loadFinished(){
        try {
            this._controller = this.getController(MatchOverviewController.class);
        } catch (Exception e) {
            Log.error(e);
        }
        this.renderGames();
//        this.ScaleScreen(_gridParent);

        this.disableNotAllowed();
    }


    private void disableNotAllowed(){

        var roles = GameSession.getRoles();
        if(!GameSession.hasRole("observer")){
            this._observerModeButton.setDisable(true);
        }

        if(!GameSession.hasRole("administrator")){

        }
    }

    public void renderGames(){
        this.gameObservableList = FXCollections.observableArrayList();
        this.gameObservableList1 = FXCollections.observableArrayList();
        this.gameObservableList2 = FXCollections.observableArrayList();
        this.gameListview.setItems(this.gameObservableList);
        this.gameListview1.setItems(this.gameObservableList1);
        this.gameListview2.setItems(this.gameObservableList2);
        this.gameObservableList.clear();
        this.gameObservableList1.clear();
        this.gameObservableList2.clear();

        List<Game> games = this._controller.getGames();


        for (var game : games) {
            switch (game.getGameState().getState()) {
                case "request": {
                    if(game.getAnswer().get_type().equals("unknown")){
                        this.gameObservableList.add(game);
                    }
                    break;
                }

                case "playing": {
                    boolean isMyTurn;

                    try {
                        isMyTurn = MatchOverviewModel.isMyTurn(game);
                    }
                    catch (NullPointerException e){
                        isMyTurn = true;
                    }

                    if (isMyTurn) {
                        this.gameObservableList1.add(game);
                    } else {
                        this.gameObservableList2.add(game);
                    }
                    break;
                }

                case "finished": {
                    //TODO: show finished games
                    break;
                }

                case "resigned": {
                    //TODO: show resigned games??
                    break;
                }
            }

            this.gameListview.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                listViewCell.setMatchOverview(this);
                return listViewCell;
            });


            this.gameListview1.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                listViewCell.setMatchOverview(this);
                return listViewCell;
            });

            this.gameListview2.setCellFactory(studentListView -> {
                var listViewCell = new ListViewCell();
                listViewCell.setController(this._controller);
                listViewCell.setMatchOverview(this);
                return listViewCell;
            });

        }
    }

    @FXML
    public void filter(){
        String filter = this._searchBar.getText();
        FilteredList<Game> filteredGames = new FilteredList<>(this.gameObservableList, s -> true);
        FilteredList<Game> filteredGames1 = new FilteredList<>(this.gameObservableList1, s -> true);
        FilteredList<Game> filteredGames2 = new FilteredList<>(this.gameObservableList2, s -> true);
        if(filter == null || filter.length() == 0){
            filteredGames.setPredicate(s -> true);
        }
        else{
            filteredGames.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        if(filter == null || filter.length() == 0){
            filteredGames1.setPredicate(s -> true);
        }
        else{
            filteredGames1.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        if(filter == null || filter.length() == 0){
            filteredGames2.setPredicate(s -> true);
        }
        else{
            filteredGames2.setPredicate(s -> {
                return (s.getPlayer1().getUsername().contains(filter) || s.getPlayer2().getUsername().contains(filter));
            });
        }

        this.gameListview.setItems(filteredGames);
        this.gameListview1.setItems(filteredGames1);
        this.gameListview2.setItems(filteredGames2);
    }
    // Shows all buttons whe have access to.
    private void showAccessibleButtons(){

    }


    @FXML
    private void logOut(){
        this._controller.endSession();
        try{
            this._controller.navigate("LoginView", 350,550);
        }
        catch (Exception e){
            Log.error(e);
        }
    }

    @FXML
    private void navigateObserver(){
        try{
            this._controller.navigate("ObserverOverview",620,770);
        }
        catch(Exception e){
            Log.error(e);
        }
    }

    @FXML
    private void accountInfo() {
        try{
            this._controller.navigate("AccountInformation");
        }
        catch (Exception e){
            Log.error(e);
        }
    }


    @FXML
    public void refresh(){
        this.renderGames();
    }

    @FXML
    private void invitationView(){

        try{
            this._controller.navigate("MatchInvitationView");
        }
        catch(Exception e){

        }
    }
}
