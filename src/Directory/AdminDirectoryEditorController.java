package Directory;

import Application.AbstractController;
import Application.SceneSwitcher;
import Map.Entity.Destination;
import Map.Entity.Hospital;
import Map.Entity.Office;
import Application.Database.DatabaseManager;
import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import Entity.Doctor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyValue;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Predicate;


/*
 * Created by jw97 on 2/18/2017
 */

public class AdminDirectoryEditorController extends AbstractController
{
    Boolean deptDirectoryUp = false;

    Hospital hospital;
    ObservableList<Doctor> existingDoctors;
    ObservableList<Office> existingDepts;
    ObservableList<String> existingLoc = FXCollections.observableArrayList();

    @FXML
    private JFXTextField searchBar, firstName, lastName, description;

    @FXML
    private JFXTextField phoneNum1, phoneNum2, phoneNum3;

    @FXML
    private JFXTextField startTime, endTime;

    @FXML
    private TextField searchForLoc;

    @FXML
    private JFXListView<String> locAssigned;

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
    private AnchorPane deptDirectory;

    @FXML
    private JFXTextField searchDeptBar;

    @FXML
    private TableView<Office> deptDataTable;

    @FXML
    private TableColumn<Office, String> deptNameCol;

    @FXML
    private TableColumn<Office, String> deptLocCol;

    @FXML
    private HBox editDeptFields;

    @FXML
    private TextField deptNameField, assignedLocField;

    @FXML
    private Button editorButton;


    public AdminDirectoryEditorController() throws SQLException
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

        existingDoctors = FXCollections.observableArrayList(hospital.getDoctors().values());

        dataTable.setItems(existingDoctors);

        // Creating list of data to be filtered
        FilteredList<Doctor> filteredDoctors = new FilteredList<>(existingDoctors);

        // Adding a listener to the search bar, filtering through the data as the user types
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredDoctors.setPredicate((Predicate<? super Doctor>) profile -> {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Checks if filter matches
                if (profile.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Filter does not match
                return false;
            });
        });

        // Create a sorted list for the filtered data list
        SortedList<Doctor> sortedDoctors = new SortedList<>(filteredDoctors);
        // Bind the sorted list to table
        sortedDoctors.comparatorProperty().bind(dataTable.comparatorProperty());
        // Set table data
        dataTable.setItems(sortedDoctors);

        existingLoc.clear();

        for (Destination d: hospital.getDestinations().values()) {
            existingLoc.add(d.getName());
        }

        TextFields.bindAutoCompletion(searchForLoc, existingLoc);

        searchForLoc.setOnKeyPressed((KeyEvent e) -> {
            switch (e.getCode()) {
                case ENTER:
                    addToAssignedDept(searchForLoc.getText());
                    break;
                default:
                    break;
            }
        });

        setPhoneNumConstraint(phoneNum1, 3);
        setPhoneNumConstraint(phoneNum2, 3);
        setPhoneNumConstraint(phoneNum3, 4);

        showDeptOptions();
        delAssignedLoc();
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

    public void addToAssignedDept(String location) {
        if (!locAssigned.getItems().contains(location)) {
            locAssigned.getItems().add(location);
        }

        searchForLoc.clear();
    }

    @FXML
    private void showDelOption() {
        MenuItem delete = new MenuItem("Delete");
        ContextMenu options = new ContextMenu();
        options.getItems().add(delete);
        dataTable.setContextMenu(options);

        delete.setOnAction((ActionEvent event) -> {
                Doctor d = dataTable.getSelectionModel().getSelectedItem();

                if (hospital.getDoctors().get(d.getName()) == d) {
                    hospital.getDoctors().remove(d.getName());

                    saveData();
                    reset();
                    initialize();
                }
        });
    }

    @FXML
    private void delAssignedLoc() {
        MenuItem delete = new MenuItem("Delete");
        ContextMenu options = new ContextMenu();
        options.getItems().add(delete);
        locAssigned.setContextMenu(options);

        delete.setOnAction((ActionEvent e) -> {
            String s = locAssigned.getSelectionModel().getSelectedItem();

            locAssigned.getItems().remove(s);
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

        searchForLoc.clear();

        startTime.clear();
        endTime.clear();
    }

    private Boolean isProcessable() {
        if (firstName.getText() == null || (firstName.getText().isEmpty())){
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
        showDelOption();
        searchForLoc.clear();
        locAssigned.getItems().clear();

        Doctor selectedDoc = dataTable.getSelectionModel().getSelectedItem();
        
        if (selectedDoc != null) {
            firstName.setText(selectedDoc.splitName()[1]);
            lastName.setText(selectedDoc.splitName()[0]);
            description.setText(selectedDoc.getDescription());

            if (!selectedDoc.getPhoneNum().equals("N/A")) {
                phoneNum1.setText(selectedDoc.splitPhoneNum()[0]);
                phoneNum2.setText(selectedDoc.splitPhoneNum()[1]);
                phoneNum3.setText(selectedDoc.splitPhoneNum()[2]);
            } else {
                phoneNum1.clear();
                phoneNum2.clear();
                phoneNum3.clear();
            }

            startTime.setText(selectedDoc.splitHours()[0]);
            endTime.setText(selectedDoc.splitHours()[1]);

            // Displaying the locations the doctor is assigned to
            for (Destination d : selectedDoc.getDestinations()) {
                locAssigned.getItems().add(d.getName());
            }

            delAssignedLoc();
        }
    }

    @FXML
    private void saveProfile() {
        if (isProcessable()) {
            Doctor toEdit = dataTable.getSelectionModel().getSelectedItem();

            String name = lastName.getText() + ", " + firstName.getText();
            String d = description.getText();

            String phoneNum = "N/A";

            if (phoneNum1.getText().length() > 0) {
                phoneNum = phoneNum1.getText() + "-" + phoneNum2.getText() + "-" +
                        phoneNum3.getText();
            }

            String hours = startTime.getText() + " - " + endTime.getText();

            HashSet<Destination> newDestinations = new HashSet<>();

            for (String l: locAssigned.getItems()) {
                for (Destination s: hospital.getDestinations().values()) {
                    if (s.getName().equals(l)) {
                        newDestinations.add(s);
                    }
                }
            }


            if (toEdit != null) {
                UUID editId = toEdit.getDocID();

                for (Doctor exist: hospital.getDoctors().values()) {
                    if (exist == toEdit) {
                        hospital.getDoctors().remove(exist.getName());
                        break;
                    }
                }


                Doctor newDoc = new Doctor(editId, name, d, hours, newDestinations);
                newDoc.setPhoneNum(phoneNum);
                hospital.getDoctors().put(name, newDoc);
            } else {
                addNewProfile(name, d, hours, newDestinations, phoneNum);
            }

            saveData();
            reset();
            initialize();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not all required fields are filled in.");
            alert.setTitle("Action denied.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    private void addNewProfile(String name, String d, String hrs, HashSet<Destination> destinations, String phoneNum) {
        Doctor newDoc = new Doctor(name, d, hrs, destinations);
        newDoc.setPhoneNum(phoneNum);
        hospital.getDoctors().put(name, newDoc);
    }

    //---------------------------------------------------------------------- (Dept directory)

    public void initializeDeptDirectory() {
        searchDeptBar.clear();
        searchDeptBar.setStyle("-fx-text-inner-color: white;");
        deptDataTable.getSelectionModel().clearSelection();

        deptNameCol.setCellValueFactory(new PropertyValueFactory<Office, String>("name"));

        deptLocCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Office, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Office, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getDestination().getName());
            }
        });

        existingDepts = FXCollections.observableArrayList(hospital.getOffices().values());

        // Creating list of data to be filtered
        FilteredList<Office> filteredDepts = new FilteredList<>(existingDepts);

        // Adding a listener to the search bar, filtering through the data as the user types
        searchDeptBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredDepts.setPredicate((Predicate<? super Office>) o -> {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                // Checks if filter matches
                if (o.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Filter does not match
                return false;
            });
        });

        // Create a sorted list for the filtered data list
        SortedList<Office> sortedDepts = new SortedList<>(filteredDepts);
        // Bind the sorted list to table
        sortedDepts.comparatorProperty().bind(deptDataTable.comparatorProperty());
        // Set table data
        deptDataTable.setItems(sortedDepts);

        existingLoc.clear();

        for (Destination d: hospital.getDestinations().values()) {
            existingLoc.add(d.getName());
        }

        TextFields.bindAutoCompletion(assignedLocField, existingLoc);

        editDeptFields.setVisible(false);
        editorButton.setText("Add");
        showDeptOptions();
    }

    @FXML
    private void changeDirectory() {
        initializeDeptDirectory();
        Timeline directorySlide = new Timeline();
        KeyFrame keyFrame;
        directorySlide.setCycleCount(1);
        directorySlide.setAutoReverse(true);

        if (deptDirectoryUp) {
            KeyValue keyValue = new KeyValue(deptDirectory.translateYProperty(),
                    (deptDirectory.getHeight() - 700));
            keyFrame = new KeyFrame(Duration.millis(600), keyValue);
            deptDirectoryUp = false;
        } else {
            KeyValue keyValue = new KeyValue(deptDirectory.translateYProperty(),
                    -(deptDirectory.getHeight() - 80));
            keyFrame = new KeyFrame(Duration.millis(600), keyValue);
            deptDirectoryUp = true;
            editDeptFields.setVisible(false);
        }

        directorySlide.getKeyFrames().add(keyFrame);
        directorySlide.play();
    }

    @FXML
    private void showEditor() {
        deptNameField.clear();
        assignedLocField.clear();
        editorButton.setText("Add");
        editDeptFields.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(1500), editDeptFields);

        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.setAutoReverse(true);
        ft.play();
    }

    @FXML
    private void showDeptOptions() {
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");

        edit.setOnAction((ActionEvent e) -> {
            showEditor();
            editorButton.setText("Save");

            Office o = deptDataTable.getSelectionModel().getSelectedItem();

            deptNameField.setText(o.getName());
            assignedLocField.setText(o.getDestination().getName());
        });

        delete.setOnAction((ActionEvent e) -> {
            Office o = deptDataTable.getSelectionModel().getSelectedItem();

            hospital.getOffices().remove(o.getName());

            saveData();
            initializeDeptDirectory();
        });

        ContextMenu options = new ContextMenu();
        options.getItems().addAll(edit, delete);
        deptDataTable.setContextMenu(options);
    }

    @FXML
    private void deptEditOperation() {
        if (editorProcessable()) {
            String deptName = deptNameField.getText();
            Destination assignedDest = new Destination();

            for (Destination d: hospital.getDestinations().values()) {
                if (d.getName().equals(assignedLocField.getText())) {
                    assignedDest = d;
                    break;
                }
            }

            switch (editorButton.getText()) {
                case "Add":
                    Office newOffice = new Office(deptName, assignedDest);
                    hospital.getOffices().put(deptName, newOffice);

                    break;

                case "Save":
                    Office o = deptDataTable.getSelectionModel().getSelectedItem();

                    if (!(o.getName().equals(deptName))) {
                        o.setName(deptName);
                    }

                    if (o.getDestination() != assignedDest) {
                        o.setSuite(assignedDest);
                    }

                    existingDepts.removeAll(existingDepts);
                    break;
                default:
                    break;
            }

            saveData();
            initializeDeptDirectory();
        }
    }

    private boolean editorProcessable() {
        if (deptNameField.getText() == null || (deptNameCol.getText().isEmpty())){
            return false;
        }

        if (assignedLocField.getText() == null || (assignedLocField.getText().isEmpty())){
            return false;
        }

        return true;
    }

    private void saveData() {
        try {
            new DatabaseManager().saveData(hospital);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onMapBuilderSwitch(ActionEvent actionEvent)
    {
        try
        {
            SceneSwitcher.switchToMapEditorView(this.getStage());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
