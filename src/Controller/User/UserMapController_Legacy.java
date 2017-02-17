package Controller.User;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class UserMapController_Legacy extends AbstractController
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
    public UserMapController_Legacy(){}
    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    @FXML
    private void clickBack() throws IOException{
        SceneSwitcher.switchToUserSearchView(primaryStage);
    }
    @FXML
    private void clickSwitchFloors(){}
}
