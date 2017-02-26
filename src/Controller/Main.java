//Testing
package Controller;

import Controller.Map.Admin.AdminList;
import Entity.Doctor;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class Main extends Application
{
    public static AdminList admins;    // For testing purposes
    public static final ObservableList<String> departments = FXCollections.observableArrayList();
    public static final ObservableList<Doctor> FaulknerHospitalDirectory = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        SceneSwitcher.switchToUserMapView(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
