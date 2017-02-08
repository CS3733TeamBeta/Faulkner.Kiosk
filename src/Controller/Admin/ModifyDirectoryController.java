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

public class ModifyDirectoryController extends AbstractController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnAddNewProfile;
    @FXML
    Button btnModifyExistingProfile;

    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }


    public ModifyDirectoryController(){

    }

    @FXML
    private void clickedBack() throws IOException {
        SceneSwitcher.switchToAdminWelcomeView(primaryStage);
    }
    @FXML
    private void clickedAddNewProfile() throws IOException {
        SceneSwitcher.switchToAddNewProfileView(primaryStage);
    }
    @FXML
    private void clickedModifyExistingProfile() throws IOException {
        SceneSwitcher.switchToChooseProfileToModifyView(primaryStage);
    }
}

