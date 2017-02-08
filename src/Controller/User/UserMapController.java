package Controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class UserMapController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnSwitchFloors;
    @FXML
    TextArea txtTextualDirections;
    @FXML
    ImageView imgMap;

    Stage primaryStage;
    public UserMapController(){}
    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    @FXML
    private void clickBack() throws IOException{
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
    private void clickSwitchFloors(){}
}
