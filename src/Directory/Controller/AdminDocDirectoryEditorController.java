package Directory.Controller;

import Application.ApplicationController;
import Directory.Boundary.AdminDocDirectoryBoundary;
import Directory.Entity.Doctor;
import Map.Entity.Destination;
import Map.Entity.Hospital;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

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
    ObservableList<Destination> existingLoc = FXCollections.observableArrayList(hospital.getDestinations());
    AdminDeptDirectoryEditor deptPane = new AdminDeptDirectoryEditor();

    public AdminDocDirectoryEditorController() throws SQLException
    {

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
            // Create a sorted list for the filtered data list
            SortedList<Doctor> sortedDoctors = new SortedList<>(docBoundary.setSearchList(newValue));
            // Bind the sorted list to table
            sortedDoctors.comparatorProperty().bind(dataTable.comparatorProperty());
            // Set table data
            dataTable.setItems(sortedDoctors);
        });

        searchForLoc.setItems(existingLoc);

        searchForLoc.setOnKeyPressed((KeyEvent e) -> {
            switch (e.getCode()) {
                case ENTER:
                    if (!locAssigned.getItems().contains(searchForLoc.getValue())) {
                        locAssigned.getItems().add(searchForLoc.getValue());
                    }

                    searchForLoc.setValue(null);
                    break;
                default:
                    break;
            }
        });

        setPhoneNumConstraint(phoneNum1, 3);
        setPhoneNumConstraint(phoneNum2, 3);
        setPhoneNumConstraint(phoneNum3, 4);

        deptPane.mainDirectoryPane.setPrefWidth(mainDirectoryPane.getPrefWidth());

        mainDirectoryPane.getChildren().add(deptPane);
        deptPane.toFront();
        deptPane.relocate(0, mainDirectoryPane.getHeight() - 700);
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
        firstName.clear();
        lastName.clear();

        description.clear();

        phoneNum1.clear();
        phoneNum2.clear();
        phoneNum3.clear();

        dataTable.getSelectionModel().clearSelection();

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
        reset();

        Doctor selectedDoc = dataTable.getSelectionModel().getSelectedItem();

        if (selectedDoc != null) {
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

            showDelOption();
        }
    }

    @FXML
    private void saveProfile() {
        if (isProcessable()) {
            Doctor toEdit = dataTable.getSelectionModel().getSelectedItem();

            String name = lastName.getText() + ", " + firstName.getText();
            String d = description.getText();
            String phoneNum = "N/A";
            String hrs = startTime.getText() + " - " + endTime.getText();

            if (phoneNum1.getText() != null || phoneNum1.getText().isEmpty()) {
                phoneNum = phoneNum1.getText() + "-" + phoneNum2.getText() + "-" + phoneNum3.getText();
            }

            if (toEdit == null) {
                Doctor newDoc = new Doctor(name, d, hrs, locAssigned.getItems());
                newDoc.setPhoneNum(phoneNum);
                docBoundary.addDoctor(newDoc);
            } else {
                docBoundary.editDoctor(toEdit, name, d, hrs, locAssigned.getItems(), phoneNum);
            }

            reset();
            initialize();
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
