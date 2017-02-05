package Controller;

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
public class EditNodeGraph
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

    public EditNodeGraph(){

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
    private void saveHit() throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../modifyLocations.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ModifyLocations controller = loader.getController();
        controller.setStage(primaryStage);

        new ModifyLocations();
    }

    @FXML
    private void exitHit() throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../modifyLocations.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ModifyLocations controller = loader.getController();
        controller.setStage(primaryStage);

        new ModifyLocations();
    }

}
