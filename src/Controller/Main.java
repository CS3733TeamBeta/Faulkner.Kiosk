//Testing
package Controller;

import Model.Database.DatabaseManager;
import Controller.Admin.AdminList;
import Model.DoctorProfile;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Main extends Application {

    public static AdminList admins;    // For testing purposes
    public static final ObservableList<String> departments =
            FXCollections.observableArrayList();

    public static final ObservableList<DoctorProfile> FaulknerHospitalDirectory = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        departments.addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        SceneSwitcher.switchToLoginView(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            launch(args);
            DatabaseManager test = new DatabaseManager();

//            test.testDatabase();
//            test.addRow("services", "Admitting/Registration", 1);
//            test.deleteRow("departments", "floor", 4);
//            test.addRow("services", "ATM", 3);
//            test.addRow("services", "Atrium Cafe", 1);
        }
        catch (NullPointerException e){
            System.out.println("We know what we are, but know not what we may be.");
        }
    }
}
