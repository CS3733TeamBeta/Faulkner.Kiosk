// Create and edit by Joan Wong

package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class addNewProfileController {
    Boolean noEmptyFields = true; // Checks if the fields are empty; true, for testing purposes
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


    @FXML
    private void logoutHit(){
        Main.thisStage.setScene(Main.adminLogin);
    }

    @FXML
    private void backHit(){
        Main.thisStage.setScene(Main.changingDirectoryView);
    }

    @FXML
    private void saveHit(){
        if (noEmptyFields) {
            doctorProfile newDoctor = new doctorProfile(firstName.getText(), lastName.getText());
            System.out.println("You have added a new profile of name: " + newDoctor.getFirstName() + " "
                    + newDoctor.getLastName());
            newDoctor.assignRoom(roomNum.getText());
            System.out.println("You have assigned him/her to this room:" + newDoctor.getRoomNum());

        } else {
            return; // Exception
        }

        Main.thisStage.setScene(Main.changingDirectoryView);
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
            deptList.remove(deptSelected);
            addedDept.add(deptSelected);
        } else {
            return;
        }

        deptListView.setItems(FXCollections.observableList(deptList));
        deptAddedListView.setItems(FXCollections.observableList(addedDept));
    }

    @FXML
    private void removeClicked() {
        String deptToBeRemoved = deptAddedListView.getSelectionModel().getSelectedItem();

        if (deptToBeRemoved != null) {
            deptAddedListView.getSelectionModel().clearSelection();
            addedDept.remove(deptToBeRemoved);
            deptList.add(deptToBeRemoved);
        } else {
            return;
        }

        deptListView.setItems(FXCollections.observableList(deptList));
        deptAddedListView.setItems(FXCollections.observableList(addedDept));
    }
}
