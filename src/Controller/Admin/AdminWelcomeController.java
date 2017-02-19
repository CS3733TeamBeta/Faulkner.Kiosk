package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    Label welcomeLabel;

    Stage primaryStage;

    AdminProfile adminProfile;

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
        SceneSwitcher.switchToModifyLocationsView(primaryStage);
    }

    @FXML
    protected void initialize() {

    }

    /**
     * Sets the profile to welcome!
     *
     * @param adminProfile
     */
    public void setAdminProfile(AdminProfile adminProfile)
    {
        this.adminProfile = adminProfile;
        welcomeLabel.setText("Welcome " + adminProfile.getUsername() + "!");
    }
}
