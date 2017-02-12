package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Exceptions.AddFoundException;
import Model.Directory;
import Model.DoctorProfile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.soap.Text;
import java.io.IOException;

public class ModifyDirectoryController extends AbstractController
{
    @FXML
    Button btnBack;
    @FXML
    TableColumn<?,?> lastNameCol;
    @FXML
    TableColumn<?,?> firstNameCol;
    @FXML
    TableColumn<?,?> phoneCol;
    @FXML
    TableColumn<?, ?> roomCol;
    @FXML
    TableColumn<?,?> departmentCol;
    @FXML
    TextField lnAdd;
    @FXML
    TextField fnAdd;
    @FXML
    TextField phoneAdd;
    @FXML
    TextField roomAdd;
    @FXML
    TextField deptAdd;
    @FXML
    Button addBtn;


    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public void displayCells () {

    }

    public void setFirstNameCol(TableColumn<?, ?> firstNameCol) {
        this.firstNameCol = firstNameCol;

    }

    public void setDepartmentCol(TableColumn<?, ?> departmentCol) {
        this.departmentCol = departmentCol;
    }

    public void setLastNameCol(TableColumn<?, ?> lastNameCol) {
        this.lastNameCol = lastNameCol;
    }

    public void setPhoneCol(TableColumn<?, ?> phoneCol) {
        this.phoneCol = phoneCol;
    }

    public void setRoomCol(TableColumn<?, ?> roomCol) {
        this.roomCol = roomCol;
    }

    public ModifyDirectoryController(){

    }

    @FXML
    private void clickedBack() throws IOException {
        SceneSwitcher.switchToAdminWelcomeView(primaryStage);
    }
    @FXML
    private void clickedAdd() throws IOException, AddFoundException {
        String firstname = fnAdd.getText();
        String lastname = lnAdd.getText();
        String phone = phoneAdd.getText();
        String room = roomAdd.getText();
        String department = deptAdd.getText();


        DoctorProfile doctor = new DoctorProfile(lastname, firstname, room);
        // doctor.addDepartment(StringProperty.stringExpression(department));

        Directory doctors = new Directory();
        doctors.addToDirectory(doctor);

    }


}

