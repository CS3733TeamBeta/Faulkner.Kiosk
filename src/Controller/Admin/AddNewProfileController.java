// Create and edit by Joan Wong

package Controller.Admin;

import Controller.Main;
import Exceptions.AddFoundException;
import Model.Directory;
import Model.DoctorProfile;
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

public class AddNewProfileController
{
    ObservableList<String> addedDept = FXCollections.observableArrayList();
    ObservableList<String> deptList = FXCollections.observableArrayList();
    public static Directory FaulknerHospital;

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
        FaulknerHospital = new Directory();
    }


    @FXML
    private void logoutHit()throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../../Admin/AdminLoginView.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        AdminLoginController controller = loader.getController();
        controller.setStage(primaryStage);
    }

    @FXML
    private void backHit() throws IOException{

        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../../Admin/ChangingDirectoryView.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ChangingDirectoryController controller = loader.getController();
        controller.setStage(primaryStage);

    }

    @FXML
    private void saveHit() throws IOException{
        processInformation();

        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../../Admin/ChangingDirectoryView.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ChangingDirectoryController controller = loader.getController();
        controller.setStage(primaryStage);
    }

    @FXML
    private void showDeptList() {
        deptList = Main.departments;
        deptListView.setItems(FXCollections.observableList(deptList));
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
        DoctorProfile newProfile = new DoctorProfile(firstName.getText(), lastName.getText());
        newProfile.assignRoom(roomNum.getText());
        for (String dept: addedDept) {
            try {
                newProfile.addDepartment(dept);
            } catch (AddFoundException e) {
                System.out.println("This doctor is already assigned to this room.");
            }
        }

        try {
            FaulknerHospital.addToDirectory(newProfile);
        } catch (AddFoundException e) {
            System.out.println("This doctor already exists in our hospital directory.");
        }
    }
}
