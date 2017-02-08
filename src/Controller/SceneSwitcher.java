package Controller;

import Controller.Admin.AdminLoginController;
import Controller.Admin.AdminWelcomeController;
import Controller.Admin.ModifyDirectoryController;
import Controller.Admin.ModifyLocationsController;
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
    protected static final String AdminLoginViewPath = AdminPackage + "/AdminLoginView.fxml";
    protected static final String AdminWelcomeViewPath = AdminPackage + "/AdminWelcomeView.fxml";
    protected static final String AddNewProfileViewPath = AdminPackage + "/AdminLoginView.fxml";
    protected static final String ModifyDirectoryViewPath = AdminPackage + "/ModifyDirectoryView.fxml";
    protected static final String ChooseProfileToModify = AdminPackage + "/ChooseProfileToModify.fxml";
    protected static final String EditDepartmentViewPath = AdminPackage + "/EditDepartmentView.fxml";
    protected static final String EditDoctorViewPath = AdminPackage + "/EditDoctorView.fxml";
    protected static final String EditRoomAttributesViewPath = AdminPackage + "/EditRoomAttributes.fxml";
    protected static final String ModifyLocationsViewPath = AdminPackage + "/ModifyLocations.fxml";

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

    public static void switchToLoginView(Stage primaryStage) throws IOException
    {
        AdminLoginController controller = (AdminLoginController) switchToScene(primaryStage, AdminLoginViewPath);
    }

    /**
     * Switch to Welcome Scene
     * @TODO Add in User Data
     * @param primaryStage
     * @throws IOException
     */
    public static void switchToAdminWelcomeView(Stage primaryStage) throws IOException
    {
        AdminWelcomeController controller = (AdminWelcomeController) switchToScene(primaryStage, AdminWelcomeViewPath);
    }

    public static void switchToModifyDirectoryView(Stage primaryStage) throws IOException
    {
        ModifyDirectoryController controller = (ModifyDirectoryController) switchToScene(primaryStage, ModifyDirectoryViewPath);
    }

    public static void switchToModifyLocationsView(Stage primaryStage) throws IOException
    {
        ModifyLocationsController controller = (ModifyLocationsController) switchToScene(primaryStage, ModifyDirectoryViewPath);
    }


    public static void switchToAddNewProfile(Stage primaryStage)
    {

    }

    public static void switchToChangingDirectoryView(Stage primaryStage)
    {

    }

    public static void switchToEditRoomView(Stage primaryStage)
    {

    }
}

