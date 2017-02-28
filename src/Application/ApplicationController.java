package Application;//Testing

//import Entity.Doctor;

import Application.Database.DataCache;
import Directory.Controller.AdminDeptDirectoryEditor;
import Directory.Controller.AdminDocDirectoryEditorController;
import Map.Controller.MapEditorController;
import Map.Entity.Hospital;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationController extends Application
{
    //public static AdminList admins;    // For testing purposes
   /// public static final ObservableList<String> departments = FXCollections.observableArrayList();
   // public static final ObservableList<Doctor> FaulknerHospitalDirectory = FXCollections.observableArrayList();
    MapEditorController mapEditorController;
    AdminLoginController adminLoginController;
    AdminDocDirectoryEditorController adminDocDirectoryEditorController;
    AdminDeptDirectoryEditor adminDeptDirectoryEditor;

    DataCache dataCache;

    protected static final String AdminPackage = "../Admin";
    protected static final String UserPackage = "../User";
    protected static final String AdminLoginViewPath = AdminPackage + "/AdminLoginView.fxml";
    protected static final String ModifyDirectoryViewPath = AdminPackage + "/AdminDirectoryEditor.fxml";
    protected static final String MapEditorViewPath = AdminPackage + "/MapBuilder/MapEditorView.fxml";
    protected static final String UserMapViewerPath = UserPackage + "/UserMapView.fxml";

    Stage primaryStage;

    private static ApplicationController controller;

    public ApplicationController()
    {
        controller = this; //only instantiated once
    }

    public static ApplicationController getController()
    {
        return controller;
    }

    public static Hospital getHospital()
    {
        return controller.dataCache.getHospital();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        dataCache = DataCache.getInstance();
        this.primaryStage = primaryStage;

        switchToUserMapView();

        primaryStage.show();
    }

    public void switchToScene(String pathToView) throws IOException
    {
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(this.getClass().getResource(pathToView));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        loader.getController();
       // controller.setStage(primaryStage);

       // return controller;
    }

    /**
     * Switch to Welcome Scene
     * @TODO Add in User Data
     * @throws IOException
     */
    public void switchToLoginView() throws IOException {
       switchToScene(AdminLoginViewPath);
    }


    public void switchToModifyDirectoryView() throws IOException {
       switchToScene(ModifyDirectoryViewPath);
    }

    public void switchToMapEditorView() throws IOException {
        switchToScene(MapEditorViewPath);
    }

    public void switchToUserMapView() throws IOException
    {
        switchToScene(UserMapViewerPath);
    }

    public void switchToAddFloor() throws IOException{
        // FileChooser_Controller controller = (FileChooser_Controller)switchToScene(primaryStage, "../Admin/MapBuilder/FileChooser_View.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
