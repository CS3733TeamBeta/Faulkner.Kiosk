package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class EditDoctorController
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

    public EditDoctorController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Admin/EditDoctorView.fxml"));

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
        new AdminLoginController();
    }

    @FXML
    private void backHit(){
        new ChangingDirectoryController();
    }

    @FXML
    private void saveHit(){
        new ChangingDirectoryController();
    }

    @FXML
    private void assignDeptHit(){
        //what to put here...?
    }

}
