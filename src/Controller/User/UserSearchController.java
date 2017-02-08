package Controller.User;

import Controller.AbstractController;
import Controller.Admin.AdminLoginController;
import Controller.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class UserSearchController extends AbstractController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnGetDirections;
    @FXML
    TextField txtSearchBar;
    @FXML
    ImageView imgMap;

    Stage primaryStage;
    public UserSearchController(){}
    public void setStage(Stage s)
    {
        primaryStage = s;
    }


    @FXML
    private void clickBack() throws IOException{
        SceneSwitcher.switchToHomeView(primaryStage);
    }
    @FXML
    private void clickGetDirections() throws IOException{
        SceneSwitcher.switchToUserMapView(primaryStage);
    }



}
