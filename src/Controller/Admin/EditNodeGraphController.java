package Controller.Admin;

import Controller.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Pattop on 1/31/2017.
 */
public class EditNodeGraphController
{
    @FXML
    Button addRoom, addIntersection, addConnection,
            editRoom, editIntersection, editConnection,
            removeRoom, removeIntersection, removeConnection;

    @FXML
    Button saveHit, exitHit;

    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public EditNodeGraphController(){

    }

    @FXML
    private void addRoomHit(){
        //add functionality here
    }

    @FXML
    private void addIntersectionHit(){
        //add functionality here
    }

    @FXML
    private void addConnectionHit(){
        //add functionality here
    }

    @FXML
    private void editRoomHit(){
        //add functionality here
    }

    @FXML
    private void editIntersectionHit(){
        //add functionality here
    }

    @FXML
    private void editConnectionHit(){
        //add functionality here
    }

    @FXML
    private void removeRoomHit(){
        //add functionality here
    }

    @FXML
    private void removeIntersectionHit(){
        //add functionality here
    }

    @FXML
    private void removeConnectionHit(){
        //add functionality here
    }

    @FXML
    private void saveHit() throws IOException {
        SceneSwitcher.switchToModifyLocationsView(primaryStage);
    }

    @FXML
    private void exitHit() throws IOException{
        SceneSwitcher.switchToModifyLocationsView(primaryStage);
    }

}
