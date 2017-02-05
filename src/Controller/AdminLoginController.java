package Controller;

import Model.AdminLoginModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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

    @FXML
    GridPane gridPane;

    AdminLoginModel infoForLoggingIn;

    Stage primaryStage;

    public AdminLoginController(){

        //FXMLLoader fxmlLoader;

        /*
        try {
            root = FXMLLoader.load(getClass().getResource("../AdminLoginView.fxml"));
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }

        Stage stage = (Stage) gridPane.getScene().getWindow();
        stage.setScene(scene);*/

        infoForLoggingIn = new AdminLoginModel();
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
            FXMLLoader loader;
            Parent root;

            loader = new FXMLLoader(getClass().getResource("../AdminWelcomeView.fxml"));

            root = loader.load();
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            AdminWelcomeController controller = loader.getController();
            controller.setStage(primaryStage);
            //primaryStage.show();
        }
    }

    @FXML
    private void clickedBack()
    {

    }

    @FXML
    protected void initialize() {

    }
}
