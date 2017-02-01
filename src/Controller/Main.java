package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    public static Stage thisStage;
    public static Scene adminLogin, adminWelcome, addNewProfile,
            changingDirectoryView,  chooseProfileToModify, dragWindow,
            editDepartmentView, editDoctorView, editNodeGraph,
            editRoomAttributes, editRoomView, modifyLocations;

    @Override
    public void start(Stage primaryStage) throws Exception{
        thisStage = primaryStage;

        Parent addNewProfileRoot = FXMLLoader.load(getClass().getResource("../AddNewProfile.fxml"));
        Parent adminLoginRoot = FXMLLoader.load(getClass().getResource("../AdminLoginView.fxml"));
        Parent adminWelcomeRoot = FXMLLoader.load(getClass().getResource("../AdminWelcomeView.fxml"));
        Parent changingDirectoryViewRoot = FXMLLoader.load(getClass().getResource("../ChangingDirectoryView.fxml"));
        Parent chooseProfileToModifyRoot = FXMLLoader.load(getClass().getResource("../ChooseProfileToModify.fxml"));
        Parent dragWindowRoot = FXMLLoader.load(getClass().getResource("../dragWindow.fxml"));
        Parent editDepartmentViewRoot = FXMLLoader.load(getClass().getResource("../editDepartmentView.fxml"));
        Parent editDoctorViewRoot = FXMLLoader.load(getClass().getResource("../editDoctorView.fxml"));
        Parent editNodeGraphRoot = FXMLLoader.load(getClass().getResource("../editNodeGraph.fxml"));
        Parent editRoomAttributesRoot = FXMLLoader.load(getClass().getResource("../editRoomAttributes.fxml"));
        Parent editRoomViewRoot = FXMLLoader.load(getClass().getResource("../editRoomView.fxml"));
        Parent modifyLocationsRoot = FXMLLoader.load(getClass().getResource("../modifyLocations.fxml"));

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
