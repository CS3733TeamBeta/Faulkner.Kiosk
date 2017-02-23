package Controller;

import Controller.Admin.*;
import Controller.User.*;
import Domain.Map.Hospital;
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
    protected static final String ModifyDirectoryViewPath = AdminPackage + "/AdminDirectoryEditor.fxml";
    protected static final String MapEditorViewPath = AdminPackage + "/MapBuilder/MapEditorView.fxml";
    protected static final String UserMapViewerPath = UserPackage + "/UserMapView.fxml";

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


    public static void switchToModifyDirectoryView(Stage primaryStage) throws IOException {
        AdminDirectoryEditorController controller = (AdminDirectoryEditorController) switchToScene(primaryStage, ModifyDirectoryViewPath);
    }

    public static void switchToMapEditorView(Stage primaryStage) throws IOException {
        MapEditorController controller = (MapEditorController) switchToScene(primaryStage, MapEditorViewPath);
    }

    public static void switchToUserMapView(Stage primaryStage) throws IOException
    {
        UserMapViewController controller = (UserMapViewController)switchToScene(primaryStage, UserMapViewerPath);
    }

    public static void switchToAddFloor(Stage primaryStage, Hospital theHospital) throws IOException{
        FileChooser_Controller controller = (FileChooser_Controller)switchToScene(primaryStage, "../Admin/MapBuilder/FileChooser_View.fxml");
        controller.setTheHospital(theHospital);
    }
}

