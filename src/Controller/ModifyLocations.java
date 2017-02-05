package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

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

    public ModifyLocations(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Admin/ModifyLocations.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void backButton(){
        new AdminWelcomeController();
    }

    @FXML
    private void changeFloorLayoutButton(){
        new EditNodeGraph();
    }

    @FXML
    private void changeRoomAssignments(){
        new EditRoomAttributes();
    }

    @FXML
    private void logoutButton(){
        new AdminLoginController();
    }
}
