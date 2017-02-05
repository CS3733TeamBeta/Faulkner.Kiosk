package Controller.Admin;

import Controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileToModifyController
{
    @FXML
    Button logout;

    @FXML
    Button back;

    @FXML
    TextField searchModDoc;

    @FXML
    ScrollPane filteredProfiles;

    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public ProfileToModifyController(){

    }

    @FXML
    private void logoutHit() throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../AdminLoginView.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        AdminLoginController controller = loader.getController();
        controller.setStage(primaryStage);
    }

    @FXML
    private void backHit() throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../ChangingDirectoryView.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ChangingDirectoryController controller = loader.getController();
        controller.setStage(primaryStage);
    }
}
