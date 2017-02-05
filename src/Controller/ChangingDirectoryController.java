package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

public class ChangingDirectoryController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnAddNewProfile;
    @FXML
    Button btnModifyExistingProfile;

    public ChangingDirectoryController(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Admin/ChangingDirectoryView.fxml"));

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
        new AdminWelcomeController();
    }
    @FXML
    private void clickedAddNewProfile()
    {
        new AddNewProfileController();
    }
    @FXML
    private void clickedModifyExistingProfile()
    {
        new ProfileToModifyController();
    }
}

