package Controller.User;

import Controller.Admin.AdminLoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class UserSearchController
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
        {
            FXMLLoader loader;
            Parent root;

            loader = new FXMLLoader(getClass().getResource("../../Admin/HomeView.fxml"));

            root = loader.load();
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            HomeController controller = loader.getController();
            controller.setStage(primaryStage);
        }
    }
    @FXML
    private void clickGetDirections() throws IOException{
        {
            FXMLLoader loader;
            Parent root;

            loader = new FXMLLoader(getClass().getResource("../../Admin/UserMapView.fxml"));

            root = loader.load();
            //create a new scene with root and set the stage
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            UserMapController controller = loader.getController();
            controller.setStage(primaryStage);
        }
    }



}
