//Testing
package Controller;

import Controller.Admin.AdminList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Main extends Application {

    public static Parent adminLoginRoot;

    public static Scene testScene;

    public static AdminList admins;    // For testing purposes
    public static final ObservableList<String> departments =
            FXCollections.observableArrayList();

    public static Stage thisStage;
    public static Scene addNewProfile, adminLogin, adminWelcome, changingDirectoryView, chooseProfileToModify, dragWindow, editDepartmentView, editDoctorView;
    public static Scene editNodeGraph, editRoomAttributes, editRoomView, modifyLocations;

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

        Scene addNewProfile, adminLogin, adminWelcome, changingDirectoryView, chooseProfileToModify, dragWindow, editDepartmentView, editDoctorView;
        Scene editNodeGraph, editRoomAttributes, editRoomView, modifyLocations;

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
        launch(args);
    }
}
