package Controller.Admin;

import Controller.Main;
import Model.DoctorProfile;
import java.util.HashSet;

import Model.RoomInfo;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProfileToModifyController {
    @FXML
    Button logout;

    @FXML
    Button back;

    @FXML
    TextField searchModDoc;

    @FXML
    TableView<DoctorProfile> filteredProfiles;

    @FXML
    TableColumn roomNumCol;

    @FXML
    TableColumn lastNameCol;

    @FXML
    TableColumn firstNameCol;


    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public ProfileToModifyController(){
    }

    public void initialize() {
        // Set up table view
        // roomNumCol.setCellValueFactory(new PropertyValueFactory<DoctorProfile, RoomInfo>("roomNum"));

        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<DoctorProfile,String>("lastName"));

        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<DoctorProfile,String>("firstName"));


        FilteredList<DoctorProfile> filtered = new FilteredList<>(Main.FaulknerHospitalDirectory, profile -> true);

        searchModDoc.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filtered.setPredicate((Predicate<? super DoctorProfile>) profile -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (profile.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (profile.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // Create sorted list for filtered data list
        SortedList<DoctorProfile> sorted = new SortedList<>(filtered);
        // Bind sorted list to table
        sorted.comparatorProperty().bind(filteredProfiles.comparatorProperty());
        // Set table data
        filteredProfiles.setItems(sorted);

    }

    @FXML
    private void logoutHit() throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../../AdminLoginView.fxml"));

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

        loader = new FXMLLoader(getClass().getResource("../../ChangingDirectoryView.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ChangingDirectoryController controller = loader.getController();
        controller.setStage(primaryStage);
    }
}
