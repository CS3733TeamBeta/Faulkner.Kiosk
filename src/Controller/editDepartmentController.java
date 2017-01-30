package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.awt.*;

public class Controller {
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
    ChoiceBox listDepts; //lists departments to choose from for editing??

}
