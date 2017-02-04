package Controller.Admin;

import Controller.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
    private void saveHit(){
        Main.thisStage.setScene(Main.modifyLocations);
    }

    @FXML
    private void exitHit(){
        Main.thisStage.setScene(Main.modifyLocations);
    }

}
