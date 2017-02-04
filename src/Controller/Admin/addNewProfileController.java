package Controller.Admin;

import Controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class addNewProfileController {
    @FXML
    Button logout, back, save, assignLoc; // assignLoc should bring up a map with nodes (admin could click and assign)

    @FXML
    TextField firstName, lastName;

    @FXML
    TextArea deptIn; // Shows the list of department(s) in which the doctor is in

    @FXML
    ScrollBar scrollDeptIn; // Allows you to scroll through the department(s) added to this doctor

    @FXML
    MenuButton addDeptMenu; // List of departments to add to the doctor's profile

    @FXML
    Label output; //This displays the room in which the doctor is assigned to (show which room has been processed)

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
