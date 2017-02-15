package Controller;

import Controller.Admin.*;
import Controller.User.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created to faciliate scene transitions
 */
public class SceneSwitcher
{
    protected static final String AdminPackage = "../Admin";
    protected static final String UserPackage = "../User";
    protected static final String AdminLoginViewPath = AdminPackage + "/AdminLoginView.fxml";
    protected static final String AdminWelcomeViewPath = AdminPackage + "/AdminWelcomeView.fxml";
    protected static final String AddNewProfileViewPath = AdminPackage + "/AddNewProfileView.fxml";
    protected static final String ModifyDirectoryViewPath = AdminPackage + "/ModifyDirectoryView.fxml";
    protected static final String ChooseProfileToModifyViewPath = AdminPackage + "/ChooseProfileToModifyView.fxml";
    //protected static final String EditDepartmentViewPath = AdminPackage + "/EditDepartmentView.fxml";
    protected static final String EditDoctorViewPath = AdminPackage + "/EditDoctorView.fxml";
    //protected static final String EditRoomAttributesViewPath = AdminPackage + "/EditRoomAttributesView.fxml";
    protected static final String ModifyLocationsViewPath = AdminPackage + "/ModifyLocationsView.fxml";
    protected static final String EditNodeGraphViewPath = AdminPackage + "/EditNodeGraphView.fxml";
    protected static final String HomeViewPath = UserPackage + "/HomeView.fxml";
    protected static final String UserMapViewPath = UserPackage + "/UserMapView.fxml";
    protected static final String UserSearchViewPath = UserPackage + "/UserSearchView.fxml";
    protected static final String EditProfileViewPath = AdminPackage + "/EditProfileView.fxml";

    public static AbstractController switchToScene(Stage primaryStage, String pathToView) throws IOException
    {
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(SceneSwitcher.class.getResource(pathToView));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        AbstractController controller = loader.getController();
        controller.setStage(primaryStage);

        return controller;
    }

    /**
     * Switch to Welcome Scene
     * @TODO Add in User Data
     * @param primaryStage
     * @throws IOException
     */

    public static void switchToLoginView(Stage primaryStage) throws IOException {
        AdminLoginController controller = (AdminLoginController) switchToScene(primaryStage, AdminLoginViewPath);
    }

    public static void switchToAdminWelcomeView(Stage primaryStage, AdminProfile adminProfile) throws IOException {
        AdminWelcomeController controller = (AdminWelcomeController) switchToScene(primaryStage, AdminWelcomeViewPath);
        controller.setAdminProfile(adminProfile);
    }

    public static void switchToModifyDirectoryView(Stage primaryStage) throws IOException {
        ModifyDirectoryController controller = (ModifyDirectoryController) switchToScene(primaryStage, ModifyDirectoryViewPath);
    }

    public static void switchToModifyLocationsView(Stage primaryStage) throws IOException {
        ModifyLocationsController controller = (ModifyLocationsController) switchToScene(primaryStage, ModifyLocationsViewPath);
    }

    public static void switchToChooseProfileToModifyView(Stage primaryStage) throws IOException {
        ChooseProfileToModifyController controller = (ChooseProfileToModifyController) switchToScene(primaryStage, ChooseProfileToModifyViewPath);
                //(ChooseProfileToModifyController) switchToScene(primaryStage, ChooseProfileToModifyViewPath);
    }

    public static void switchToAddNewProfileView(Stage primaryStage) throws IOException {
        AddNewProfileController controller = (AddNewProfileController) switchToScene(primaryStage, AddNewProfileViewPath);
    }

    public static void switchToEditNodeGraphView(Stage primaryStage) throws IOException {
        EditNodeGraphController controller = (EditNodeGraphController) switchToScene(primaryStage, EditNodeGraphViewPath);
    }

    public static void switchToEditDoctorView(Stage primaryStage) throws IOException {
        EditDoctorController controller = (EditDoctorController) switchToScene(primaryStage, EditDoctorViewPath);
    }

    /*
    public static void switchToEditRoomAttributesView(Stage primaryStage) throws IOException
    {
        EditRoomAttributesController controller = (EditRoomAttributesController) switchToScene(primaryStage, EditRoomAttributesViewPath);
    }
    */

    public static void switchToHomeView(Stage primaryStage) throws IOException {
        HomeController controller = (HomeController) switchToScene(primaryStage, HomeViewPath);
    }

    public static void switchToUserMapView(Stage primaryStage) throws IOException {
        UserMapController controller = (UserMapController) switchToScene(primaryStage, UserMapViewPath);
    }

    public static void switchToUserSearchView(Stage primaryStage) throws IOException {
        UserSearchController controller = (UserSearchController) switchToScene(primaryStage, UserSearchViewPath);
    }

    public static void switchToEditProfileView(Stage primaryStage) throws IOException {
        EditProfileController controller = (EditProfileController) switchToScene(primaryStage, EditProfileViewPath);
    }

    public static void goToUserHome(Stage primaryStage) throws IOException{
        MapViewerController controller =(MapViewerController) switchToScene(primaryStage, "../User/MapViewer/MapEditorView.fxml");
    }
}

