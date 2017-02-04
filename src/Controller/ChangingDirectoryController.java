package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ChangingDirectoryController
{
    @FXML
    Button btnBack;
    @FXML
    Button btnAddNewProfile;
    @FXML
    Button btnModifyExistingProfile;
    @FXML
    private void clickedBack()
    {
        Main.thisStage.setScene(Main.adminWelcome);
    }
    @FXML
    private void clickedAddNewProfile()
    {
        Main.thisStage.setScene(Main.addNewProfile);
    }
    @FXML
    private void clickedModifyExistingProfile()
    {
        Main.thisStage.setScene(Main.chooseProfileToModify);
    }
}

