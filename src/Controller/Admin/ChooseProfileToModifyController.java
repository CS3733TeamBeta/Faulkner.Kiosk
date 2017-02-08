package Controller.Admin;

import Controller.Main;
import Controller.SceneSwitcher;
import Model.DoctorProfile;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.scene.control.Alert.AlertType;

public class ChooseProfileToModifyController {
    @FXML
    Button logout;

    @FXML
    Button btnBack;

    @FXML
    Button deleteBut;

    @FXML
    Button editBut;

    @FXML
    TextField searchModDoc;

    @FXML
    TableView<DoctorProfile> filteredProfiles;

    @FXML
    TableColumn lastNameCol;

    @FXML
    TableColumn firstNameCol;


    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public ChooseProfileToModifyController(){
    }

    public void initialize() {
        // Setting up the columns of the TableView
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<DoctorProfile,String>("lastName"));

        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<DoctorProfile,String>("firstName"));


        // Creating list of data to be filtered
        FilteredList<DoctorProfile> filtered = new FilteredList<>(Main.FaulknerHospitalDirectory);

        // Adding a listener to the search bar, filtering through the data as the user types
        searchModDoc.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filtered.setPredicate((Predicate<? super DoctorProfile>) profile -> {
                // By default, the entire directory is displayed
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                // Checks if filter matches
                if (profile.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (profile.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Filter does not match
                return false;
            });
        });

        // Create a sorted list for the filtered data list
        SortedList<DoctorProfile> sorted = new SortedList<>(filtered);
        // Bind the sorted list to table
        sorted.comparatorProperty().bind(filteredProfiles.comparatorProperty());
        // Set table data
        filteredProfiles.setItems(sorted);

    }

    @FXML
    private void logoutHit() throws IOException{
        SceneSwitcher.switchToLoginView(primaryStage);
    }

    @FXML
    private void backHit() throws IOException{
        SceneSwitcher.switchToModifyDirectoryView(primaryStage);
    }

    @FXML
    private void delete() {
        DoctorProfile profileSelected = filteredProfiles.getSelectionModel().getSelectedItem();

        // Popup confirmation dialog message window
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this profile from the directory?");
        alert.setTitle("Confirm Action");
        alert.setHeaderText(null);
        ButtonType ok = new ButtonType("OK");
        alert.getButtonTypes().setAll(ok);

        // Save changes only if action has been confirmed.
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ok) {
            // Deleted from database
            Main.FaulknerHospitalDirectory.remove(profileSelected);
            // I need to search for a doctor in database, remove it
            System.out.println("Action confirmed.");
            // Call initiate() again to update table columns, removed from list display.
            initialize();
        }
    }

    @FXML
    private void edit() {
        DoctorProfile profileSelected = filteredProfiles.getSelectionModel().getSelectedItem();

        // Search for the specific doctor, and edit the row of the table in the database system
        // What to edit? Could we edit the ID #?

    }
}
