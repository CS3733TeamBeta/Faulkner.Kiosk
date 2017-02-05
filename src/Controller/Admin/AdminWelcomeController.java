package Controller.Admin;
import Controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class AdminWelcomeController {
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
    private void clickedBack()
    {
        new AdminLoginController();
    }
    @FXML
    private void clickedModifyDirectory()
    {
        new ChangingDirectoryController();
    }
    @FXML
    private void clickedChangeFloorplan()
    {
        new ModifyLocations();
    }

    @FXML
    protected void initialize() {

    }
}
