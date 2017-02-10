package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifyLocationsController extends AbstractController
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

    public ModifyLocationsController(){

    }

    @FXML
    private void backButton()throws IOException
    {
        FXMLLoader loader;
        Parent root;

        loader = new FXMLLoader(getClass().getResource("../../Admin/AdminWelcomeView.fxml"));

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
        SceneSwitcher.switchToEditNodeGraphView(primaryStage);
    }

    @FXML
    private void changeRoomAssignments() throws IOException {
        //SceneSwitcher.switchToEditRoomAssignmentsView(primaryStage);
        SceneSwitcher.switchToEditDoctorView(primaryStage);
    }

    @FXML
    private void logoutButton() throws IOException{
        SceneSwitcher.switchToAdminLoginView(primaryStage);
    }
}
