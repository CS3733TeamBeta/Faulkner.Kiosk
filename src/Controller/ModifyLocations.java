package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifyLocations
{
    @FXML
    Button logout;
    @FXML
    Button changeFloorLayout;
    @FXML
    Button changeRoomAssignments;
    @FXML
    Button back;

    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public ModifyLocations(){

    }

    @FXML
    private void backButton()throws IOException
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
    private void changeFloorLayoutButton() throws IOException
    {
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../editNodeGraph.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        EditNodeGraph controller = loader.getController();
        controller.setStage(primaryStage);
    }

    @FXML
    private void changeRoomAssignments() throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../editRoomAttributes.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        EditRoomAttributes controller = loader.getController();
        controller.setStage(primaryStage);
    }

    @FXML
    private void logoutButton() throws IOException{
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
}