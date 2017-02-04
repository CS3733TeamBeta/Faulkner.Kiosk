package Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class AdminWelcomeController {
    @FXML
    Button btnBack;
    @FXML
    Button btnModifyDirectory;
    @FXML
    Button btnChangeFloorplan;
    @FXML
    private void clickedBack()
    {
        Main.thisStage.setScene(Main.adminLogin);
    }
    @FXML
    private void clickedModifyDirectory()
    {
        Main.thisStage.setScene(Main.changingDirectoryView);
    }
    @FXML
    private void clickedChangeFloorplan()
    {
        Main.thisStage.setScene(Main.modifyLocations);
    }


}
