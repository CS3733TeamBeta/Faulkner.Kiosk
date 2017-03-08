package main.Directory.controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import main.Application.ApplicationController;
import main.Directory.Boundary.AdminDocDirectoryBoundary;
import main.Directory.Entity.Doctor;
import main.Map.Entity.Destination;
import main.Map.Entity.Hospital;

import java.io.IOException;
import java.sql.SQLException;

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
    ObservableList<Destination> existingLoc;
    AdminDeptDirectoryEditor deptPane;

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
        deptPane = new AdminDeptDirectoryEditor();
        nameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("description"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("phoneNum"));
        hourCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("hours"));

        // Adding a listener to the search bar, filtering through the data as the user types
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            dataTable.getSelectionModel().clearSelection();
            dataTable.setItems(docBoundary.setSearchList(newValue));
        });

        if (docBoundary.getDoctors() != null) {
            dataTable.setItems(docBoundary.getDoctors());
        }

        searchForLoc.getItems().clear();

        existingLoc = FXCollections.observableArrayList(ApplicationController.getHospital().getDestinations());

        searchForLoc.getItems().addAll(existingLoc);

        searchForLoc.setOnAction(e -> {
            Destination d = searchForLoc.getSelectionModel().getSelectedItem();
            if (d != null) {
                if (!(locAssigned.getItems().contains(d))) {
                    locAssigned.getItems().add(d);
                }
            }
        });


        setPhoneNumConstraint(phoneNum1, 3);
        setPhoneNumConstraint(phoneNum2, 3);
        setPhoneNumConstraint(phoneNum3, 4);

        mainDirectoryPane.getChildren().add(deptPane);
        deptPane.prefWidthProperty().bind(mainDirectoryPane.widthProperty());
        deptPane.toFront();
        deptPane.relocate(mainDirectoryPane.getLayoutX(), mainDirectoryPane.getHeight() + 620);

        showDelOption();
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

        delete.setOnAction((ActionEvent event) -> {
            Doctor d = dataTable.getSelectionModel().getSelectedItem();

            if (d != null) {
                docBoundary.removeDoctor(d);
                reset();
                searchBar.clear();
            }

            Destination dest = locAssigned.getSelectionModel().getSelectedItem();
            locAssigned.getItems().remove(dest);
        });

        dataTable.setContextMenu(options);
        locAssigned.setContextMenu(options);
    }

    @FXML
    private void reset() {
        firstName.clear();
        lastName.clear();

        description.clear();

        phoneNum1.clear();
        phoneNum2.clear();
        phoneNum3.clear();

        locAssigned.getItems().clear();

        searchForLoc.setValue(null);

        startTime.clear();
        endTime.clear();
    }

    private Boolean isProcessable() {
        if (firstName.getText() == null || (firstName.getText().isEmpty())){
            return false;
        }

        if (lastName.getText() == null || (lastName.getText().isEmpty())){
            return false;
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
        reset();

        Doctor selectedDoc = dataTable.getSelectionModel().getSelectedItem();

        if (selectedDoc != null) {
            firstName.setText(selectedDoc.splitName()[1]);
            lastName.setText(selectedDoc.splitName()[0]);
            description.setText(selectedDoc.getDescription());

            if (!selectedDoc.getPhoneNum().equals("N/A") && !selectedDoc.getPhoneNum().equals("")) {
                phoneNum1.setText(selectedDoc.splitPhoneNum()[0]);
                phoneNum2.setText(selectedDoc.splitPhoneNum()[1]);
                phoneNum3.setText(selectedDoc.splitPhoneNum()[2]);
            }

            startTime.setText(selectedDoc.splitHours()[0]);
            endTime.setText(selectedDoc.splitHours()[1]);

            for (Destination d: selectedDoc.getDestinations()) {
                locAssigned.getItems().add(d);
            }
        }
    }

    private boolean phoneNumValidation() {
        if ((phoneNum1.getText() == null || phoneNum1.getText().isEmpty()) &&
                (phoneNum2.getText() == null || phoneNum2.getText().isEmpty()) &&
                (phoneNum3.getText() == null || phoneNum3.getText().isEmpty())) {
            return true;
        }

        if ((phoneNum1.getText().length() == 3 &&
                phoneNum2.getText().length() == 3 &&
                phoneNum3.getText().length() == 4)) {
            return true;
        }

        return false;
    }

    @FXML
    private void saveProfile() {
        if (isProcessable() && phoneNumValidation()) {
            String name = lastName.getText() + ", " + firstName.getText();
            String d = description.getText();
            String hrs = startTime.getText() + " - " + endTime.getText();

            ObservableList<Destination> destinations = FXCollections.observableArrayList();

            for (Destination dest : locAssigned.getItems()) {
                destinations.add(dest);
            }

            Doctor newDoc = new Doctor(name, d, hrs, destinations);

            newDoc.setDocID(dataTable.getSelectionModel().getSelectedItem().getDocID());

            if (phoneNum1.getText().length() > 0) {
                String phoneNum = phoneNum1.getText() + "-" + phoneNum2.getText() + "-" + phoneNum3.getText();
                newDoc.setPhoneNum(phoneNum);
            }

            if (dataTable.getSelectionModel().getSelectedItem() != null) {
                docBoundary.removeDoctor(dataTable.getSelectionModel().getSelectedItem());

                //dataTable.getSelectionModel().clearSelection();
            }

            docBoundary.addDoc(newDoc);

            searchBar.clear();

            reset();

            dataTable.requestFocus();
            dataTable.getSelectionModel().select(newDoc);
            int i = dataTable.getSelectionModel().getSelectedIndex();
            dataTable.getFocusModel().focus(i);
            dataTable.scrollTo(i);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not all required fields are filled in.");
            alert.setTitle("Action denied.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    public void onBack(ActionEvent actionEvent) throws IOException {
        ApplicationController.getController().switchToVisualBuildingEditor();
    }
}
