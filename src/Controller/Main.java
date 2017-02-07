//Testing
package Controller;

import Model.Database.DatabaseManager;
import com.sun.javafx.geom.Shape;
import Controller.Admin.AdminList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Main extends Application {

    public static Stage thisStage;
    public static Scene adminLogin, adminWelcome, addNewProfile,
            changingDirectoryView,  chooseProfileToModify, dragWindow,
            editDepartmentView, editDoctorView, editNodeGraph,
            editRoomAttributes, editRoomView, modifyLocations;

    public static AdminList admins;    // For testing purposes
    public static final ObservableList<String> departments =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        thisStage = primaryStage;

        Parent addNewProfileRoot = FXMLLoader.load(getClass().getResource("../Admin/AddNewProfile.fxml"));
        Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("../Admin/AdminLoginView.fxml"));
        Parent adminWelcomeRoot = FXMLLoader.load(getClass().getResource("../Admin/AdminWelcomeView.fxml"));
        Parent changingDirectoryViewRoot = FXMLLoader.load(getClass().getResource("../Admin/ChangingDirectoryView.fxml"));
        Parent chooseProfileToModifyRoot = FXMLLoader.load(getClass().getResource("../Admin/ChooseProfileToModify.fxml"));
        Parent dragWindowRoot = FXMLLoader.load(getClass().getResource("../Admin/DragWindow.fxml"));
        Parent editDepartmentViewRoot = FXMLLoader.load(getClass().getResource("../Admin/EditDepartmentView.fxml"));
        Parent editDoctorViewRoot = FXMLLoader.load(getClass().getResource("../Admin/EditDoctorView.fxml"));
        Parent editNodeGraphRoot = FXMLLoader.load(getClass().getResource("../Admin/EditNodeGraph.fxml"));
        Parent editRoomAttributesRoot = FXMLLoader.load(getClass().getResource("../Admin/EditRoomAttributes.fxml"));
        Parent editRoomViewRoot = FXMLLoader.load(getClass().getResource("../Admin/EditRoomView.fxml"));
        Parent modifyLocationsRoot = FXMLLoader.load(getClass().getResource("../Admin/ModifyLocations.fxml"));

        departments.addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        thisStage.setTitle("Hello World");

        addNewProfile = new Scene(addNewProfileRoot);
        adminLogin = new Scene(adminLoginRoot);
        adminWelcome = new Scene(adminWelcomeRoot);
        changingDirectoryView = new Scene(changingDirectoryViewRoot);
        chooseProfileToModify = new Scene(chooseProfileToModifyRoot);
        dragWindow = new Scene(dragWindowRoot);
        editDepartmentView = new Scene(editDepartmentViewRoot);
        editDoctorView = new Scene(editDoctorViewRoot);
        editNodeGraph = new Scene(editNodeGraphRoot);
        editRoomAttributes = new Scene(editRoomAttributesRoot);
        editRoomView = new Scene(editRoomViewRoot);
        modifyLocations = new Scene(modifyLocationsRoot);

        thisStage.setScene(adminLogin);
        thisStage.show();
        admins = new AdminList();

        admins.addAdmin("ADMIN", "ADMIN");
        admins.setDevEnabled(true);

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
        catch(SQLException e) {
            System.out.println("Violence is never the answer.");
            System.out.println(e.getMessage());
        }
        catch (NullPointerException e){
            System.out.println("We know what we are, but know not what we may be.");
        }
    }
}
