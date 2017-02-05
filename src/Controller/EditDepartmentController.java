package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.awt.*;
import java.io.IOException;

public class EditDepartmentController
{
    @FXML
    Button logout;
    @FXML
    Button backButton;
    @FXML
    Button saveButton;
    @FXML
    ComboBox addToDept; //lists doctors who are available to add to this department
    @FXML
    ChoiceBox doctorsAssigned; //lists doctors assigned to this department
    @FXML
    ComboBox listDepts;

    public EditDepartmentController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Admin/EditDepartmentView.fxml"));

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

    @FXML
    private void addToDptHit(){

    }

}
