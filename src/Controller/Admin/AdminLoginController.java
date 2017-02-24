
package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Model.AdminLoginModel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController extends AbstractController
{
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label loginLabel;

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXButton loginButton;

    AdminLoginModel infoForLoggingIn;

    Stage primaryStage;

    public AdminLoginController(){
        infoForLoggingIn = new AdminLoginModel();
        infoForLoggingIn.admins.devEnabled = true;
    }

    public void setStage(Stage stage)
    {
        primaryStage = stage;
    }

    @FXML
    private void clickedLogin() throws IOException
    {
        System.out.println("Login clicked");

        if(infoForLoggingIn.admins.checkValidity(txtUsername.getText(), txtPassword.getText())) {
            SceneSwitcher.switchToMapEditorView(primaryStage);
        }
    }

    @FXML
    private void clickedBack() throws IOException
    {
        SceneSwitcher.switchToUserMapView(primaryStage);
    }

    @FXML
    protected void initialize()   {

    }
}
