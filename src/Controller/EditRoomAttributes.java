package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

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

    public EditRoomAttributes(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Admin/EditRoomAttributes.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void exitButton(){
        new ModifyLocations();
    }

    @FXML
    private void saveButton(){
        new ModifyLocations();
    }
}
