package Controller.Admin;

import Controller.AbstractController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;

import java.util.List;

public class AdminDirectoryEditorController  extends AbstractController{

    public AdminDirectoryEditorController()
    {
        JFXTreeTableColumn<?, ?> deptColumn = new JFXTreeTableColumn<>("Thing ");

        directoryTable.setShowRoot(false);
        directoryTable.setEditable(true);
        directoryTable.getColumns().add(0, deptColumn);
    }

    @FXML
    private JFXTreeTableView<?> directoryTable;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField phoneNumberOneTextField;

    @FXML
    private JFXTextField phoneNumberTwoTextField;

    @FXML
    private JFXTextField phoneNumberThreeTextField;

    @FXML
    private JFXTreeTableView<?> departmentsTable;

    @FXML
    private JFXTextField officeNumberTextField;

    @FXML
    private JFXTextField hoursStartTimeTextField;

    @FXML
    private JFXTextField hoursEndTimeTextField;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton newButton;

    @FXML
    void clickedSave()
    {
        System.out.println("Thing");
    }
}
