package Controller;

import Controller.Admin.AdminList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage thisStage;
    public static Scene adminLogin, adminWelcome, addNewProfile,
            changingDirectoryView,  chooseProfileToModify, dragWindow,
            editDepartmentView, editDoctorView, editNodeGraph,
            editRoomAttributes, editRoomView, modifyLocations;

    public static AdminList admins;

    @Override
    public void start(Stage primaryStage) throws Exception{
        thisStage = primaryStage;

        Parent addNewProfileRoot = FXMLLoader.load(getClass().getResource("../Admin/AddNewProfile.fxml"));
        Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("../Admin/AdminLoginView.fxml"));
        Parent adminWelcomeRoot = FXMLLoader.load(getClass().getResource("../Admin/AdminWelcomeView.fxml"));
        Parent changingDirectoryViewRoot = FXMLLoader.load(getClass().getResource("../Admin/ChangingDirectoryView.fxml"));
        Parent chooseProfileToModifyRoot = FXMLLoader.load(getClass().getResource("../Admin/ChooseProfileToModify.fxml"));
        Parent dragWindowRoot = FXMLLoader.load(getClass().getResource("../Admin/dragWindow.fxml"));
        Parent editDepartmentViewRoot = FXMLLoader.load(getClass().getResource("../Admin/editDepartmentView.fxml"));
        Parent editDoctorViewRoot = FXMLLoader.load(getClass().getResource("../Admin/editDoctorView.fxml"));
        Parent editNodeGraphRoot = FXMLLoader.load(getClass().getResource("../Admin/editNodeGraph.fxml"));
        Parent editRoomAttributesRoot = FXMLLoader.load(getClass().getResource("../Admin/editRoomAttributes.fxml"));
        Parent editRoomViewRoot = FXMLLoader.load(getClass().getResource("../Admin/editRoomView.fxml"));
        Parent modifyLocationsRoot = FXMLLoader.load(getClass().getResource("../Admin/modifyLocations.fxml"));

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
        launch(args);
    }
}
