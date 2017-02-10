// Create and edit by Joan Wong

package Controller.Admin;

import Controller.AbstractController;
import Controller.Main;
import Controller.SceneSwitcher;
import Domain.Map.Doctor;
import Exceptions.AddFoundException;
import Model.DoctorProfile;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;

public class AddNewProfileController extends AbstractController
{
    ObservableList<String> addedDept = FXCollections.observableArrayList();
    ObservableList<String> deptList = FXCollections.observableArrayList();
    @FXML
    Button logout, back, save;

    @FXML
    Button add;

    @FXML
    Button remove;

    @FXML
    Button showDeptList;

    @FXML
    TextField firstName;

    @FXML
    TextField lastName;

    @FXML
    TextField roomNum;

    @FXML
    TextField description;

    @FXML
    TextField hours;

    @FXML
    TextField phoneNum;

    @FXML
    ListView<String> deptListView;

    @FXML
    ListView<String> deptAddedListView;


    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public AddNewProfileController(){
        deptList = Main.departments;
    }

    public void initialize() {
        deptList = Main.departments;
        deptListView.setItems(FXCollections.observableList(deptList));
    }


    @FXML
    private void logoutHit()throws IOException
    {
        SceneSwitcher.switchToLoginView(primaryStage);
    }

    /**
     * Switches to modify directory view
     * @throws IOException
     */
    @FXML
    private void backHit() throws IOException
    {
        SceneSwitcher.switchToModifyLocationsView(primaryStage);
    }

    @FXML
    private void saveHit() throws IOException {
        if (isProcessable()) {
            processInformation();

            SceneSwitcher.switchToModifyDirectoryView(primaryStage);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not all required fields are filled in.");
            alert.setTitle("Action denied.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    private void addClicked() {
        String deptSelected = deptListView.getSelectionModel().getSelectedItem();

        if (deptSelected != null && !(addedDept.contains(deptSelected))) {
            deptListView.getSelectionModel().clearSelection();
            addedDept.add(deptSelected);
        } else {
            return;
        }

        deptAddedListView.setItems(FXCollections.observableList(addedDept));
    }

    @FXML
    private void removeClicked() {
        String deptToBeRemoved = deptAddedListView.getSelectionModel().getSelectedItem();

        if (deptToBeRemoved != null) {
            deptAddedListView.getSelectionModel().clearSelection();
            addedDept.remove(deptToBeRemoved);
        } else {
            return;
        }

        deptAddedListView.setItems(FXCollections.observableList(addedDept));
    }

    private Boolean isProcessable() {
        if (firstName.getText() != null && !(firstName.getText().isEmpty())){
            return false;
        }

        if (lastName.getText() != null && !(lastName.getText().isEmpty())) {
            return false;
        }

        if (roomNum.getText() != null && !(roomNum.getText().isEmpty())) {
            return false;
        }

        if (description.getText() != null && !(description.getText().isEmpty())) {
            return false;
        }

        if (addedDept.isEmpty()) {
            return false;
        }

        return true;
    }

    private void processInformation() {
        String name = lastName.getText() + ", " + firstName.getText();
        String hour = "N/A";

        if (hours.getText() != null && !(hours.getText().isEmpty())) {
            hour = hours.getText();
        }

        Doctor newProfile = new Doctor(name, description.getText(), hour);

        if (phoneNum.getText() != null && !(phoneNum.getText().isEmpty())) {
            newProfile.setPhoneNum(phoneNum.getText());
        }

        newProfile.addOffice(roomNum.getText());

        for (String dept: addedDept) {
            newProfile.addDepartment(dept);
        }

        Main.FaulknerHospitalDirectory.add(newProfile);
    }
}
