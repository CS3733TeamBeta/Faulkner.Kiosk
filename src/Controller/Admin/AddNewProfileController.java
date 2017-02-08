// Create and edit by Joan Wong

package Controller.Admin;

import Controller.AbstractController;
import Controller.Main;
import Controller.SceneSwitcher;
import Exceptions.AddFoundException;
import Model.DoctorProfile;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
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
    ListView<String> deptListView;

    @FXML
    ListView<String> deptAddedListView;

    @FXML
    ImageView profilePic;

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
    private void saveHit() throws IOException
    {
        SceneSwitcher.switchToModifyDirectoryView(primaryStage);
    }

    @FXML
    private void addClicked() {
        String deptSelected = deptListView.getSelectionModel().getSelectedItem();

        if (deptSelected != null) {
            deptListView.getSelectionModel().clearSelection();
            makeChanges(deptSelected, deptList, addedDept);
        } else {
            return;
        }

        updateLists();
    }

    @FXML
    private void removeClicked() {
        String deptToBeRemoved = deptAddedListView.getSelectionModel().getSelectedItem();

        if (deptToBeRemoved != null) {
            deptAddedListView.getSelectionModel().clearSelection();
            makeChanges(deptToBeRemoved, addedDept, deptList);
        } else {
            return;
        }

        updateLists();
    }

    private void makeChanges(String department, ObservableList<String> removedFrom, ObservableList<String> addedTo) {
        removedFrom.remove(department);
        addedTo.add(department);
    }

    private void updateLists() {
        deptListView.setItems(FXCollections.observableList(deptList));
        deptAddedListView.setItems(FXCollections.observableList(addedDept));
    }

    private Boolean isProcessable() {
        return true;
    }

    private void processInformation() {
        DoctorProfile newProfile = new DoctorProfile(firstName.getText(), lastName.getText(), roomNum.getText());
        for (String dept: addedDept) {
            try {
                newProfile.addDepartment(new SimpleStringProperty(dept));
            } catch (AddFoundException e) {
                System.out.println("This doctor is already assigned to this department(s).");
            }
        }

        Main.FaulknerHospitalDirectory.add(newProfile);
    }
}
