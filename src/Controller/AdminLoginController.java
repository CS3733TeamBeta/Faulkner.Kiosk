package Controller;

import Model.AdminLoginModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminLoginController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnLogin;
    @FXML
    TextField txtUsername;
    @FXML
    PasswordField txtPassword;

    AdminLoginModel infoForLoggingIn;

    public AdminLoginController(){
        Parent newRoot;
        Stage newStage = new Stage();

        try {
            newRoot = FXMLLoader.load(getClass().getResource("../AdminLoginView.fxml"));
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }

        new Scene(newRoot);


        infoForLoggingIn = new AdminLoginModel();
    }

    @FXML
    private void clickedLogin()
    {
        if(infoForLoggingIn.admins.checkValidity(txtUsername.getText(), txtPassword.getText())) {
            new AdminWelcomeController();
        }
    }

    @FXML
    private void clickedBack()
    {

    }
}
