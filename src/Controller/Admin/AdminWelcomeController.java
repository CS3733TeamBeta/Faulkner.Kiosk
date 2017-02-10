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
        SceneSwitcher.switchToAdminLoginView(primaryStage);
    }

    @FXML
    private void clickedModifyDirectory() throws IOException
    {
        SceneSwitcher.switchToModifyDirectoryView(primaryStage);
    }
    @FXML
    private void clickedChangeFloorplan() throws IOException
    {
        SceneSwitcher.switchToModifyLocationsView(primaryStage);
    }

    @FXML
    protected void initialize() {

    }
}
