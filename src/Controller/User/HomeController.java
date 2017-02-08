package Controller.User;

import Controller.Admin.AdminLoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController
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
        {
            FXMLLoader loader;
            Parent root;

            loader = new FXMLLoader(getClass().getResource("../../Admin/UserSearchView.fxml"));

            root = loader.load();
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            UserSearchController controller = loader.getController();
            controller.setStage(primaryStage);
        }
    }

    
    @FXML
    public void clickSAB() throws IOException {
        {
            FXMLLoader loader;
            Parent root;

            loader = new FXMLLoader(getClass().getResource("../../Admin/AdminLoginView.fxml"));

            root = loader.load();
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            AdminLoginController controller = loader.getController();
            controller.setStage(primaryStage);
        }
    }
}
