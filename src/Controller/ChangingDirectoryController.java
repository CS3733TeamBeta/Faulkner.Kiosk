package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangingDirectoryController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnAddNewProfile;
    @FXML
    Button btnModifyExistingProfile;

    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }


    public ChangingDirectoryController(){

    }

    @FXML
    private void clickedBack() throws IOException
    {
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../AdminWelcomeView.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        AdminWelcomeController controller = loader.getController();
        controller.setStage(primaryStage);
    }
    @FXML
    private void clickedAddNewProfile() throws IOException
    {
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../AddNewProfile.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        AddNewProfileController controller = loader.getController();
        controller.setStage(primaryStage);
    }
    @FXML
    private void clickedModifyExistingProfile() throws IOException
    {
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../ChooseProfiletoModify.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ProfileToModifyController controller = loader.getController();
        controller.setStage(primaryStage);

    }
}

