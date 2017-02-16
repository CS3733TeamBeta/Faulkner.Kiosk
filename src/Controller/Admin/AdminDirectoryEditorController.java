package Controller.Admin;

import Controller.AbstractController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;
import javafx.stage.Stage;


public class AdminDirectoryEditorController extends AbstractController {

    @FXML
    private JFXTreeTableView<?> directoryTable;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField phoneNumberTextField;

    @FXML
    private JFXTreeTableView<?> departmentsTable;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton newButton;

    public AdminDirectoryEditorController(){

    }

    @FXML
    void clickedSave() {
        System.out.print("nameTextField: ");
        System.out.print(nameTextField.getText());
    }

}
