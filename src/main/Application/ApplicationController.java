package main.Application;//Testing

//import Entity.Doctor;
import main.Directory.controller.AdminDeptDirectoryEditor;
import main.Directory.controller.AdminDocDirectoryEditorController;
import main.Application.Database.DataCache;
import main.Map.Controller.MapEditorController;
import main.Map.Controller.UserMapViewController;
import main.Map.Entity.Building;
import main.Map.Entity.Hospital;
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

    Building lastBuildingUnderEdit;

    protected static final String AdminLoginViewPath = "/application/AdminLoginView.fxml";
    protected static final String ModifyDirectoryViewPath = "/directory/AdminDocDirectoryEditor.fxml";
    protected static final String MapEditorViewPath = "/map/MapEditorView.fxml";
    protected static final String UserMapViewerPath = "/map/UserMapView.fxml";
    protected static final String View3DPath = "/map/3DMapView.fxml";

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

    public void stashBuildingEdit(Building b)
    {
        lastBuildingUnderEdit = b;
    }

    public Building popBuildingEdit()
    {
        return lastBuildingUnderEdit;
    }

    IdleTimer idle = new IdleTimer();

    long timeout=120;

    FXMLLoader loader;

    public static DataCache getCache()
    {
        return controller.dataCache;

    }
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        idle.initTimer();

        idle.setStageToMonitor(primaryStage);

        dataCache = DataCache.getInstance();
        this.primaryStage = primaryStage;

        switchToUserMapView();

        primaryStage.show();
    }

    public long getTimeout()
    {
        return timeout;
    }

    public void setTimeout(long timeout)
    {
        this.timeout = timeout;
        idle.updateTimeout(); //updates timeout with latest value from application controller
    }

    public Object switchToScene(String pathToView) throws IOException
    {
        Parent root;

        loader = new FXMLLoader(this.getClass().getResource(pathToView));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        return loader.getController();
    }

    /**
     * Switch to Welcome Scene
     * @TODO Add in User Data
     * @throws IOException
     */
    public void switchToLoginView() throws IOException {
       switchToScene(AdminLoginViewPath);
    }

    public void switchToVisualBuildingEditor() throws IOException
    {
        switchToScene(View3DPath);
    }

    public void switchToModifyDirectoryView() throws IOException {
       switchToScene(ModifyDirectoryViewPath);
    }

    public void switchToMapEditorView(Building b) throws IOException
    {
        idle.stop();
        lastBuildingUnderEdit = b;
        MapEditorController controller = (MapEditorController) switchToScene(MapEditorViewPath);
        controller.setBuilding(b);
    }

    public void switchToSavedMapEditorView() throws IOException
    {
        switchToMapEditorView(lastBuildingUnderEdit);
    }

    public void switchToUserMapView() throws IOException
    {
        idle.start();
        switchToScene(UserMapViewerPath);
    }

    public void switchToAddFloor() throws IOException{
        // FileChooser_Controller controller = (FileChooser_Controller)switchToScene(primaryStage, "../Admin/MapBuilder/FileChooser_View.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
