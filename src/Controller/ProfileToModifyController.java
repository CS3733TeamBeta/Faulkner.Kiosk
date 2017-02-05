package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;

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

    @FXML
    private void logoutHit(){
        new AdminLoginController();
    }

    @FXML
    private void backHit(){
        new ChangingDirectoryController();
    }
}
