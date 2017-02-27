
package Application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class AdminLoginController
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

    HashMap<String, String> credentials;

    Stage primaryStage;

    public AdminLoginController(){
        //credentials.put("ADMIN", "ADMIN");
    }

    public void setStage(Stage stage)
    {
        primaryStage = stage;
    }

    @FXML
    private void clickedLogin() throws IOException
    {
        //if(credentials.get(txtUsername.getText()) txtUsername.getText(), txtPassword.getText())) {
        ApplicationController.getController().switchToMapEditorView();
       // }
    }

    @FXML
    private void clickedBack() throws IOException
    {
        ApplicationController.getController().switchToUserMapView();
    }

    @FXML
    protected void initialize()   {

    }
}
