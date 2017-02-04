package Controller;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.fxml.FXML;

public class editRoomController {
    @FXML
    TextField roomNumber;
    @FXML
    Button logoutButton;
    @FXML
    Button backButton;
    @FXML
    Button saveButton;


    @FXML
    ComboBox chooseDocBox; //this box is where all the doctors that can go in the room are listed
    //needs listbox choices
    @FXML
    ChoiceBox doctorsAssigned; //this box lists all the doctors that are currently assigned to the room
    //needs listbox choices

    @FXML
    private void logoutHit(){

    }

    @FXML
    private void backHit(){

    }

    @FXML
    private void saveHit(){

    }
}

