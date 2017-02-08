package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class AdminWelcomeController extends AbstractController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnModifyDirectory;
    @FXML
    Button btnChangeFloorplan;

    Stage primaryStage;

    public AdminWelcomeController(){

    }

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    @FXML
    private void clickedBack() throws IOException
    {
        SceneSwitcher.switchToLoginView(primaryStage);
    }

    @FXML
    private void clickedModifyDirectory() throws IOException
    {
        SceneSwitcher.switchToModifyDirectoryView(primaryStage);
    }
    @FXML
    private void clickedChangeFloorplan() throws IOException
    {
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../../Admin/ModifyLocationsController.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ModifyLocationsController controller = loader.getController();
        controller.setStage(primaryStage);
    }

    @FXML
    protected void initialize() {

    }
}
