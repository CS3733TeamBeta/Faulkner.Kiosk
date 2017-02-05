// Create and edit by Joan Wong

package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AddNewProfileController
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


    @FXML
    private void logoutHit(){
        new AdminLoginController();
    }

    @FXML
    private void backHit(){
        new ChangingDirectoryController();
    }

    @FXML
    private void saveHit(){}

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
}
