package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class addNewProfileController {
    @FXML
    Button logout, back, save;

    @FXML
    Button add, remove, deselectAll, selectAll;

    @FXML
    TextField firstName, lastName;

    @FXML
    TextField roomNum;

    @FXML
    ListView deptList;

    @FXML
    TextArea deptIn; // Shows the list of department(s) in which the doctor is in


    @FXML
    ImageView profilePic;

    @FXML
    private void logoutHit(){
        Main.thisStage.setScene(Main.adminLogin);
    }

    @FXML
    private void backHit(){
        Main.thisStage.setScene(Main.changingDirectoryView);
    }

    @FXML
    private void saveHit(){
        Main.thisStage.setScene(Main.changingDirectoryView);
    }

    @FXML
    private void assignLocHit(){
        //where does this go...?
    }


}
