
package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Model.AdminLoginModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminLoginController extends AbstractController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnLogin;
    @FXML
    TextField txtUsername;
    @FXML
    PasswordField txtPassword;

    @FXML
    GridPane gridPane;

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
            SceneSwitcher.switchToAdminWelcomeView(primaryStage);
        }
    }

    @FXML
    private void clickedBack() throws IOException
    {
        System.out.println("test");
        SceneSwitcher.goToUserHome(primaryStage);
    }

    @FXML
    protected void initialize() {

    }
}
