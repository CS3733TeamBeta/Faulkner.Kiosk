package Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.fxml.FXML;

import java.io.IOException;

public class EditRoomController
{
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

    public EditRoomController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Admin/EditRoomView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

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

