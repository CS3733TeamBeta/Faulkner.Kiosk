//Testing
package Controller;

import Model.Database.DatabaseManager;
import com.sun.javafx.geom.Shape;
import Controller.Admin.AdminList;
import Controller.Admin.AdminLoginController;
import Model.DoctorProfile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Main extends Application {

    public static AdminList admins;    // For testing purposes
    public static final ObservableList<String> departments =
            FXCollections.observableArrayList();

    public static final ObservableList<DoctorProfile> FaulknerHospitalDirectory = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        departments.addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Admin/AdminLoginView.fxml"));

        Parent root = (Parent)loader.load();


        AdminLoginController controller = loader.getController();
        controller.setStage(primaryStage);

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            launch(args);
            DatabaseManager test = new DatabaseManager();
            test.testDatabase();
        }
        catch(SQLException e) {
            System.out.println("Violence is never the answer.");
            System.out.println(e.getMessage());
        }
    }
}
