package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditDoctorController extends AbstractController
{
    @FXML
    Button logout;
    @FXML
    Button backButton;
    @FXML
    Button saveButton;
    @FXML
    ComboBox assignDept; //a list of all the possible depts for the doctor to be assigned to
    //needs listbox choices
    @FXML
    ChoiceBox doctorDept; //a list of all the depts the doctor is currently in
                            //needs listbox choices
    @FXML
    TextField nameFirst;
    @FXML
    TextField nameLast;
    @FXML
    TextField docRoom; //room doctor is located in
    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }


    @FXML
    private void logoutHit() throws IOException{
        SceneSwitcher.switchToLoginView(primaryStage);
    }

    @FXML
    private void backHit() throws IOException{
        SceneSwitcher.switchToModifyLocationsView(primaryStage);
    }

    @FXML
    private void saveHit(){
        //Main.thisStage.setScene(Main.changingDirectoryView);
    }

    @FXML
    private void assignDeptHit(){
        //what to put here...?
    }

}
