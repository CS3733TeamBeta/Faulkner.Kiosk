package Controller.Admin;

import Controller.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

public class ModifyLocations
{
    @FXML
    Button logout;
    @FXML
    Button changeFloorLayout;
    @FXML
    Button changeRoomAssignments;
    @FXML
    Button back;

    @FXML
    private void backButton(){
        Main.thisStage.setScene(Main.adminWelcome);
    }

    @FXML
    private void changeFloorLayoutButton(){
        Main.thisStage.setScene(Main.editNodeGraph);
    }

    @FXML
    private void changeRoomAssignments(){
        Main.thisStage.setScene(Main.editRoomAttributes);
    }

    @FXML
    private void logoutButton(){
        Main.thisStage.setScene(Main.adminLogin);
    }
}
