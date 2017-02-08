package Controller.User;

import Controller.AbstractController;
import Controller.Admin.AdminLoginController;
import Controller.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController extends AbstractController
{
    @FXML
    Button btnEnter;
    @FXML
    Button btnSAB; // Secret Admin Button
    Stage primaryStage;
    public HomeController(){}
    public void setStage(Stage s)
    {
        primaryStage = s;
    }



    @FXML
    public void clickEnter() throws IOException {
        SceneSwitcher.switchToUserSearchView(primaryStage);
    }

    
    @FXML
    public void clickSAB() throws IOException {
        SceneSwitcher.switchToLoginView(primaryStage);
    }
}
