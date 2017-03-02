package main.Directory.controller;

import main.Application.ApplicationController;
import main.Directory.Boundary.AdminDocDirectoryBoundary;
import main.Directory.Entity.Doctor;
import main.Map.Entity.Destination;
import main.Map.Entity.Hospital;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by jw97 on 2/27/2017.
 */
public class AdminDocDirectoryEditorController {
    @FXML
    private TableColumn<Doctor, String> nameCol;

    @FXML
    private TableColumn<Doctor, String> descriptionCol;

    @FXML
    private TableColumn<Doctor, String> phoneNumCol;

    @FXML
    private TableColumn<Doctor, String> hourCol;

    @FXML
    private TableView<Doctor> dataTable;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private ComboBox<Destination> searchForLoc;

    @FXML
    private JFXListView<Destination> locAssigned;

    @FXML
    private JFXTextField firstName, lastName, description;

    @FXML
    private JFXTextField phoneNum1, phoneNum2, phoneNum3;

    @FXML
    private JFXTextField startTime, endTime;

    @FXML
    private AnchorPane mainDirectoryPane;

    Hospital hospital;
    AdminDocDirectoryBoundary docBoundary;
    ObservableList<Destination> existingLoc =
            FXCollections.observableArrayList(ApplicationController.getHospital().getDestinations());
    AdminDeptDirectoryEditor deptPane = new AdminDeptDirectoryEditor();
    Boolean editMode = false;

    public AdminDocDirectoryEditorController() throws Exception
    {
        docBoundary = new AdminDocDirectoryBoundary(ApplicationController.getHospital());
    }

    public void setHospital(Hospital h)
    {
        this.hospital = h;
    }

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("description"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("phoneNum"));
        hourCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("hours"));

        // Adding a listener to the search bar, filtering through the data as the user types
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            dataTable.setItems(docBoundary.setSearchList(newValue));
        });

        if (docBoundary.getDoctors() != null) {
            dataTable.setItems(docBoundary.getDoctors());
        }

        searchForLoc.setItems(existingLoc);
        searchForLoc.valueProperty().addListener(new ChangeListener<Destination>() {
            @Override public void changed(ObservableValue ov, Destination t, Destination d1) {
                if (!locAssigned.getItems().contains(d1)) {
                    locAssigned.getItems().add(d1);
                }
            }});


        setPhoneNumConstraint(phoneNum1, 3);
        setPhoneNumConstraint(phoneNum2, 3);
        setPhoneNumConstraint(phoneNum3, 4);

        mainDirectoryPane.getChildren().add(deptPane);
        deptPane.prefWidthProperty().bind(mainDirectoryPane.widthProperty());
        deptPane.toFront();
        deptPane.relocate(mainDirectoryPane.getLayoutX(), mainDirectoryPane.getHeight() + 620);
    }

    public void setPhoneNumConstraint(TextField textField, int length) {
        textField.setOnKeyTyped(e -> {
            if (textField.getText().length() >= length) {
                e.consume();
            }
        });

        textField.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent keyEvent) {
                if (!"0123456789".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }
            }
        });
    }

    @FXML
    private void showDelOption() {
        MenuItem delete = new MenuItem("Delete");
        ContextMenu options = new ContextMenu();
        options.getItems().add(delete);
        dataTable.setContextMenu(options);
        locAssigned.setContextMenu(options);

        delete.setOnAction((ActionEvent event) -> {
            if (event.getSource() == "dataTable") {
                Doctor d = dataTable.getSelectionModel().getSelectedItem();

                docBoundary.removeDoctor(d);
            } else {
                Destination d = locAssigned.getSelectionModel().getSelectedItem();

                locAssigned.getItems().remove(d);
            }

        });
    }

    @FXML
    private void reset() {
        editMode = false;

        dataTable.getSelectionModel().clearSelection();

        firstName.clear();
        lastName.clear();

        description.clear();

        phoneNum1.clear();
        phoneNum2.clear();
        phoneNum3.clear();

        searchBar.clear();

        locAssigned.getItems().clear();

        searchForLoc.setValue(null);

        startTime.clear();
        endTime.clear();
        showDelOption();
    }

    private Boolean isProcessable() {
        if (firstName.getText() == null || (firstName.getText().isEmpty())){
            return false;
        }

        if (lastName.getText() == null || (lastName.getText().isEmpty())){
            return false;
        }

        if (phoneNum1.getText().length() > 0 ||
                phoneNum2.getText().length() > 0 ||
                phoneNum3.getText().length() > 0) {
            if (!((phoneNum1.getText().length() == 3) &&
                    (phoneNum2.getText().length() == 3) &&
                    (phoneNum3.getText().length() == 4))) {
                return false;
            }
        }

        if (startTime.getText() == null || (startTime.getText().isEmpty())){
            return false;
        }

        if (endTime.getText() == null || (endTime.getText().isEmpty())){
            return false;
        }


        if (locAssigned.getItems().isEmpty()) {
            return false;
        }

        return true;
    }


    @FXML
    private void displaySelectedDocInfo() {
        Doctor selectedDoc = dataTable.getSelectionModel().getSelectedItem();

        if (selectedDoc != null) {
            editMode = true;
            firstName.setText(selectedDoc.splitName()[1]);
            lastName.setText(selectedDoc.splitName()[0]);
            description.setText(selectedDoc.getDescription());

            if (!selectedDoc.getPhoneNum().equals("N/A")) {
                phoneNum1.setText(selectedDoc.splitPhoneNum()[0]);
                phoneNum2.setText(selectedDoc.splitPhoneNum()[1]);
                phoneNum3.setText(selectedDoc.splitPhoneNum()[2]);
            }

            startTime.setText(selectedDoc.splitHours()[0]);
            endTime.setText(selectedDoc.splitHours()[1]);

            locAssigned.setItems(docBoundary.getDocLoc(selectedDoc));
        }
    }

    @FXML
    private void saveProfile() {
        if (isProcessable()) {

            String name = lastName.getText() + ", " + firstName.getText();
            String d = description.getText();
            String phoneNum = "N/A";
            String hrs = startTime.getText() + " - " + endTime.getText();
            if (phoneNum1.getText() != null || !phoneNum1.getText().isEmpty()) {
                phoneNum = phoneNum1.getText() + "-" + phoneNum2.getText() + "-" + phoneNum3.getText();
            }

            if (editMode) {
                Doctor toEdit = dataTable.getSelectionModel().getSelectedItem();
                docBoundary.editDoctor(toEdit, name, d, hrs, locAssigned.getItems(), phoneNum);
            } else {
                Doctor newDoc = new Doctor(name, d, hrs, locAssigned.getItems());
                newDoc.setPhoneNum(phoneNum);
                docBoundary.addDoctor(newDoc);

                dataTable.getSelectionModel().select(newDoc);
                dataTable.scrollTo(newDoc);
            }

            reset();

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not all required fields are filled in.");
            alert.setTitle("Action denied.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    public void onMapBuilderSwitch(ActionEvent actionEvent) throws IOException {
        ApplicationController.getController().switchToMapEditorView();
    }
}
