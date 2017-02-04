package Controller.Admin;

import Controller.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class EditRoomAttributes
{
    @FXML
    Button exitButton;
    @FXML
    Button saveButton;
    @FXML
    TextField roomNumber; //lists doctors who are available to add to this department
    @FXML
    TextField addDoctorsToRoom; //lists doctors assigned to this department

    @FXML
    private void exitButton(){
        Main.thisStage.setScene(Main.modifyLocations);
    }

    @FXML
    private void saveButton(){
        Main.thisStage.setScene(Main.modifyLocations);
    }
}
