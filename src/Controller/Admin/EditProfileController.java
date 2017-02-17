package Controller.Admin;

import Controller.AbstractController;
import Controller.Main;
import Controller.SceneSwitcher;
import Domain.Map.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by jw97 on 2/9/2017.
 */
public class EditProfileController extends AbstractController
{
    Doctor editDoc;
    ObservableList<String> addedDept;
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

    public void initialize() {
        editDoc = ChooseProfileToModifyController.editDoc;

        String name = editDoc.getName();
        String[] lastAndFirst = name.split(", ");

        firstName.setText(lastAndFirst[1]);
        lastName.setText(lastAndFirst[0]);

        for (String office: editDoc.getMyOffice()) {
            roomNum.setText(office);
        }

        description.setText(editDoc.getDescription());
        hours.setText(editDoc.getHours());
        phoneNum.setText(editDoc.getPhoneNum());

        //addedDept = FXCollections.observableArrayList(editDoc);
        deptAddedListView.setItems(addedDept);
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
        SceneSwitcher.switchToChooseProfileToModifyView(primaryStage);
    }

    @FXML
    private void saveHit() throws IOException {
        if (isProcessable()) {
            processInformation();

            SceneSwitcher.switchToChooseProfileToModifyView(primaryStage);
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
        if (firstName.getText() == null || (firstName.getText().isEmpty())){
            return false;
        }

        if (lastName.getText() == null || (lastName.getText().isEmpty())) {
            return false;
        }

        if (roomNum.getText() == null || (roomNum.getText().isEmpty())) {
            return false;
        }

        if (description.getText() == null || (description.getText().isEmpty())) {
            return false;
        }

        if (addedDept.isEmpty()) {
            return false;
        }

        return true;
    }

    private void processInformation() {
        String hour = "N/A";

        if (hours.getText() != null && !(hours.getText().isEmpty())) {
            hour = hours.getText();
        }

        if (phoneNum.getText() != null && !(phoneNum.getText().isEmpty())) {
            this.editDoc.setPhoneNum(phoneNum.getText());
        }

        this.editDoc.setName(lastName.getText() + ", " + firstName.getText());
        this.editDoc.setDescription(description.getText());
        this.editDoc.setHours(hour);

        //this.editDoc.setDepartment(new HashSet<String>());

//        for (String dept: addedDept) {
//            this.editDoc.addDepartment(dept);
//        }

        this.editDoc.setMyOffice(new HashSet<String>());
        this.editDoc.addOffice(roomNum.getText());
    }
}
