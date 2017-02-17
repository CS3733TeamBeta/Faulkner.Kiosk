package Controller.Admin;

import Controller.AbstractController;
import Domain.Map.Office;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import Domain.Map.Doctor;
import Domain.Map.Office;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.print.Doc;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

public class AdminDirectoryEditorController  extends AbstractController{

    private HashSet<Doctor> doctors;
    private ObservableList<Doctor> tableData;

    @FXML
    private TableView<Doctor> directoryTable;

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
    private TableColumn<Doctor, String> nameColumn;

    @FXML
    private TableColumn<Doctor, String> phoneNumberColumn;

    @FXML
    private TableColumn<Doctor, String> departmentColumn;

    @FXML
    private TableColumn<Doctor, String> hoursColumn;

    @FXML
    void clickedSave()
    {
        System.out.println("Thing");
    }

    public AdminDirectoryEditorController()
    {
        nameColumn = new TableColumn<Doctor, String>();
        phoneNumberColumn = new TableColumn<Doctor, String>();
        departmentColumn = new TableColumn<Doctor, String>();
        hoursColumn = new TableColumn<Doctor, String>();
        doctors = new HashSet<>();
    }

    public void initialize()
    {

        Office tmpoffice = new Office();

        Doctor drByrne = new Doctor("3A","N/A", tmpoffice,"Jennifer Byrne","RN, CPNP","N/A");
        Doctor drFrangieh = new Doctor("3B","N/A", tmpoffice,"George Frangieh","MD","N/A");
        Doctor drGreenberg = new Doctor("3C","N/A",tmpoffice,"James Adam Greenberg","MD","N/A");
        Doctor drGrossi = new Doctor("3A","N/A",tmpoffice,"Lisa Grossi","RN, MS, CPNP","N/A");

        doctors.add(drByrne);
        doctors.add(drFrangieh);
        doctors.add(drGreenberg);
        doctors.add(drGrossi);

        nameColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("phoneNum"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        hoursColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
    }

    private void addDoctor(Doctor d)
    {
        tableData.add(d);
        directoryTable.setItems(tableData);
    }
}
