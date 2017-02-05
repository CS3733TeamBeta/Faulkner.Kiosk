package Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;


public class AdminWelcomeController {
    @FXML
    Button btnBack;
    @FXML
    Button btnModifyDirectory;
    @FXML
    Button btnChangeFloorplan;

    public AdminWelcomeController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Admin/AdminWelcomeView.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void clickedBack()
    {
        new AdminLoginController();
    }
    @FXML
    private void clickedModifyDirectory()
    {
        new ChangingDirectoryController();
    }
    @FXML
    private void clickedChangeFloorplan()
    {
        new ModifyLocations();
    }


}
