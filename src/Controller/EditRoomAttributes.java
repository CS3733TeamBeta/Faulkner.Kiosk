package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditRoomAttributes
{
    @FXML
    Button exitButton;
    @FXML
    Button saveButton;
    @FXML
    TextField roomNumber; //lists doctors who are available to add to this department
    @FXML
    TextField addDoctorsToRoom; //lists doctors assigned to this department

    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public EditRoomAttributes(){

    }

    @FXML
    private void exitButton() throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../modifyLocations.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ModifyLocations controller = loader.getController();
        controller.setStage(primaryStage);
    }

    @FXML
    private void saveButton() throws IOException{
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../modifyLocations.fxml"));

        root = loader.load();
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        ModifyLocations controller = loader.getController();
        controller.setStage(primaryStage);
    }
}