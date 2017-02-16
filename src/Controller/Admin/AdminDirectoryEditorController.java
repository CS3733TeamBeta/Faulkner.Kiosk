package Controller.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;


public class AdminDirectoryEditorController {

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

}
