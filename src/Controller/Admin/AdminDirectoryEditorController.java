package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Domain.Map.Office;
import Domain.Map.Suite;
import Model.Database.DatabaseManager;
import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import Domain.Map.Doctor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyValue;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import javafx.scene.control.Tooltip;

import static Model.Database.DatabaseManager.Faulkner;

/*
 * Created by jw97 on 2/18/2017
 */

public class AdminDirectoryEditorController  extends AbstractController {
    private ObservableList<String> departments = FXCollections.observableArrayList();
    Boolean deptDirectoryUp = false;
    ObservableList<String> locations = FXCollections.observableArrayList();

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
    private JFXButton save;

    @FXML
    private JFXButton newProfile;

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
    private ImageView addDeptIcon;

    @FXML
    private JFXTextField searchDeptBar;

    @FXML
    private TableView<Office> deptDataTable;

    @FXML
    private TableColumn<Office, String> deptNameCol;

    @FXML
    private TableColumn<Office, String> deptLocCol;


    public AdminDirectoryEditorController() {
    }

    public void updateDoctorTable() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList(Faulkner.getDoctors().values());

        dataTable.setItems(doctors);
    }

    public void updateDeptTable() {
        ObservableList<Office> offices = FXCollections.observableArrayList(Faulkner.getOffices().values());

        deptDataTable.setItems(offices);
    }

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("description"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("phoneNum"));
        hourCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("hours"));

        ObservableList<Doctor> doctors = FXCollections.observableArrayList(Faulkner.getDoctors().values());
        dataTable.setItems(doctors);

        // Creating list of data to be filtered
        FilteredList<Doctor> filtered = new FilteredList<>(doctors);

        // Adding a listener to the search bar, filtering through the data as the user types
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filtered.setPredicate((Predicate<? super Doctor>) profile -> {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
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
        SortedList<Doctor> sorted = new SortedList<>(filtered);
        // Bind the sorted list to table
        sorted.comparatorProperty().bind(dataTable.comparatorProperty());
        // Set table data
        dataTable.setItems(sorted);

        Set s = Faulkner.getSuites().keySet();
        ObservableList<String> existingSuites = FXCollections.observableArrayList(s);

        TextFields.bindAutoCompletion(searchForLoc, existingSuites);

        searchForLoc.setOnKeyPressed((KeyEvent e) -> {
            switch (e.getCode()) {
                case ENTER:
                    if(existingSuites.contains(searchForLoc.getText())) {
                        addToAssignedDept(searchForLoc.getText());
                    } else if (locations.contains(searchForLoc.getText())){
                        System.out.println("This location is assigned to the doctor already.");
                    } else {
                        System.out.println("This suite does not exist.");
                    }

                    break;
                default:
                    break;
            }
        });

        // Would removing also removes the key from
        // all other tables?

    }

    public void initializeDeptDirectory() {
        searchDeptBar.clear();
        searchDeptBar.setStyle("-fx-text-inner-color: white;");
        //Tooltip.install(addDeptIcon, (new Tooltip("Add a department")));

        deptNameCol.setCellValueFactory(new PropertyValueFactory<Office, String>("name"));
        deptLocCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Office, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Office, String> p) {
               return new ReadOnlyObjectWrapper(p.getValue().getSuite().getName());
            }
        });

        ObservableList<Office> offices = FXCollections.observableArrayList(Faulkner.getOffices().values());
        deptDataTable.setItems(offices);

        // Creating list of data to be filtered
        FilteredList<Office> filtered = new FilteredList<>(offices);

        // Adding a listener to the search bar, filtering through the data as the user types
        searchDeptBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filtered.setPredicate((Predicate<? super Office>) o -> {
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
        SortedList<Office> sorted = new SortedList<>(filtered);
        // Bind the sorted list to table
        sorted.comparatorProperty().bind(deptDataTable.comparatorProperty());
        // Set table data
        deptDataTable.setItems(sorted);

    }

    public void addToAssignedDept(String location) {
        locations.add(location);
        locAssigned.setItems(locations);
        searchForLoc.clear();
    }

    public void editTable() {
        deptLocCol.setEditable(true);

    }

    @FXML
    private void showDeptOptions() {
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");


        edit.setOnAction((ActionEvent e) -> {
            Office o = deptDataTable.getSelectionModel().getSelectedItem();

            editTable();
        });

        delete.setOnAction((ActionEvent e) -> {
            System.out.println("Dept Data Table selected.");
            Office o = deptDataTable.getSelectionModel().getSelectedItem();

            System.out.println(o.getName());
            Faulkner.getOffices().remove(o.getName());
            System.out.println("office removed.");
            initializeDeptDirectory();
        });

        ContextMenu options = new ContextMenu();
        options.getItems().addAll(edit, delete);
        deptDataTable.setContextMenu(options);
    }


    @FXML
    private void showDelOption() {
        MenuItem delete = new MenuItem("Delete");

        delete.setOnAction((ActionEvent e) -> {
            if (e.getSource() == locAssigned) {
                String s = locAssigned.getSelectionModel().getSelectedItem();

                locations.remove(s);
            } else {
                Doctor d = dataTable.getSelectionModel().getSelectedItem();

                Faulkner.getDoctors().remove(d.getName());

                reset();
                initialize();
            }
        });

        ContextMenu options = new ContextMenu();
        options.getItems().add(delete);
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

        dataTable.getSelectionModel().clearSelection();

        searchBar.clear();

        locAssigned.getItems().clear();

        searchForLoc.clear();

        startTime.clear();
        endTime.clear();
    }


    @FXML
    private void saveProfile() {
        if (isProcessable()) {
            Doctor toEdit = dataTable.getSelectionModel().getSelectedItem();

            String name = lastName.getText() + ", " + firstName.getText();
            String d = description.getText();
            String phoneNum = phoneNum1.getText() + "-" + phoneNum2.getText() + "-" +
                    phoneNum3.getText();
            String hours = startTime.getText() + " - " + endTime.getText();

            HashSet<Suite> newSuites = new HashSet<>();

            for (String l: locAssigned.getItems()) {
                for (String s: Faulkner.getSuites().keySet()) {
                    if (s.equals(l)) {
                        newSuites.add(Faulkner.getSuites().get(s));
                    }
                }
            }

            if (toEdit != null) {
                int editId = toEdit.getDocID();

                for (String n: Faulkner.getDoctors().keySet()) {
                    if (n.equals(toEdit.getName())) {
                        if (Faulkner.getDoctors().get(n).getDocID() == editId) {
                            Faulkner.getDoctors().remove(n);
                            break;
                        }
                    }
                }

                Doctor newDoc = new Doctor(editId, name, d, hours, newSuites);
                System.out.println(newDoc);
                newDoc.setPhoneNum(phoneNum);
                Faulkner.getDoctors().put(name, newDoc);
            } else {
                addNewProfile(name, d, hours, newSuites, phoneNum);
            }

            // Save to HashMap (ready to be saved to database after shutdown)
            reset();
            initialize();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not all required fields are filled in.");
            alert.setTitle("Action denied.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

    }

    private void addNewProfile(String name, String d, String hrs, HashSet<Suite> suites, String phoneNum) {
        int newId = -5000;

        // Do we have to randomly create a ID?
        Doctor newDoc = new Doctor(newId, name, d, hrs, suites);
        newDoc.setPhoneNum(phoneNum); // If the phoneNum is valid
        Faulkner.getDoctors().put(name, newDoc);
    }

    private Boolean isProcessable() {
        if (firstName.getText() == null || (firstName.getText().isEmpty())){
            return false;
        }

        if (startTime.getText() == null || (startTime.getText().isEmpty())){
            return false;
        }

        if (endTime.getText() == null || (endTime.getText().isEmpty())){
            return false;
        }

        if (locations.isEmpty()) {
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

        firstName.setText(selectedDoc.splitName()[1]);
        lastName.setText(selectedDoc.splitName()[0]);
        description.setText(selectedDoc.getDescription());

        if (!(selectedDoc.splitPhoneNum()[0].equals(selectedDoc.getPhoneNum()))) {
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

        HashSet<Suite> assignedSuites = selectedDoc.getSuites();

        // Assigning to a suite = location
        for (Suite s: assignedSuites) {
                locations.add(s.getName());

                // Should we have a department directory (change the location of depts?)
                // I do not think we are provided with the departments with the doctors are in?
                // IF we do, should we have a office_doc table? Where would that information be saved?

                /*
                // With suite, find offices = department names
                for (Office o: Faulkner.getOffices().values()) {
                    if (o.getSuite() == s) {
                        departments.add(o.getName());
                    }
                }
                */
        }

        locAssigned.setItems(locations);
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
        }

        directorySlide.getKeyFrames().add(keyFrame);
        directorySlide.play();
    }

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
