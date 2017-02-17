package Controller.Admin;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Domain.Map.Office;
import com.jfoenix.controls.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import Domain.Map.Doctor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;

public class AdminDirectoryEditorController  extends AbstractController {

    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private ObservableList<Doctor> tableData;
    private ObservableList<String> departments = FXCollections.observableArrayList();

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
    private JFXListView<String> departmentsList;

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
    private TableColumn<Doctor, String> officeNumberColumn;

    @FXML
    private TableColumn<Doctor, String> hoursColumn;

    public AdminDirectoryEditorController() {
    }

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("phoneNum"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("depts"));

        officeNumberColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Doctor, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Doctor, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getOffice().getOfficeNum());
            }
        });

        hoursColumn.setCellValueFactory(new PropertyValueFactory<Doctor, String>("hours"));

        Office tmpoffice = new Office("N/A");

        Doctor drByrne = new Doctor("3A, 3C", "N/A", tmpoffice, "Jennifer Byrne", "RN, CPNP", "N/A");
        Doctor drFrangieh = new Doctor("3B", "N/A", tmpoffice, "George Frangieh", "MD", "N/A");
        Doctor drGreenberg = new Doctor("3C", "N/A", tmpoffice, "James Adam Greenberg", "MD", "N/A");
        Doctor drGrossi = new Doctor("3A", "N/A", tmpoffice, "Lisa Grossi", "RN, MS, CPNP", "N/A");

        doctors.addAll(drByrne, drFrangieh, drGreenberg, drGrossi);
        directoryTable.setItems(doctors);

        // All the departments on the 3rd floor

        departments.addAll("3A", "3B", "3C");

        departmentsList.setItems(departments);
        departmentsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /*
    private void addDoctor(Doctor d)
    {
        tableData.add(d);
        directoryTable.setItems(tableData);
    }
    */

    private void newProfileSetUp() {
        nameTextField.clear();
        phoneNumberOneTextField.clear();
        phoneNumberTwoTextField.clear();
        phoneNumberThreeTextField.clear();

        departmentsList.getSelectionModel().clearSelection();

        officeNumberTextField.clear();
        hoursStartTimeTextField.clear();
        hoursEndTimeTextField.clear();
    }

    @FXML
    private void reset() {
        newProfileSetUp();
        directoryTable.getSelectionModel().clearSelection();
    }


    @FXML
    private void saveProfile() {
        if (isProcessable()) {
            String name = nameTextField.getText();
            String phoneNum = phoneNumberOneTextField.getText() + "-" + phoneNumberTwoTextField.getText() + "-" +
                    phoneNumberThreeTextField.getText();

            ObservableList<String> dept = departmentsList.getSelectionModel().getSelectedItems();

            String deptSelected = dept.get(0);

            int i = 0;
            for (String d : dept) {
                if (!(deptSelected.contains(d))) {
                    deptSelected = deptSelected + ", " + d;
                }
            }

            Office office = new Office(officeNumberTextField.getText());
            String hours = hoursStartTimeTextField.getText() + "-" + hoursEndTimeTextField.getText();
            String description = "N/A";


            Doctor toEdit = directoryTable.getSelectionModel().getSelectedItem();
            if (toEdit != null) {
                doctors.remove(toEdit);
            }

            doctors.add(new Doctor(deptSelected, phoneNum, office, name, description, hours));

            directoryTable.setItems(doctors);
            reset();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not all required fields are filled in.");
            alert.setTitle("Action denied.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    private Boolean isProcessable() {
        if (nameTextField.getText() == null || (nameTextField.getText().isEmpty())){
            return false;
        }

        if (departmentsList.getSelectionModel().getSelectedItems() == null) {
            return false;
        }

        if (officeNumberTextField.getText() == null || (officeNumberTextField.getText().isEmpty())) {
            return false;
        }

        if (phoneNumberOneTextField.getText() == null || (phoneNumberOneTextField.getText().isEmpty())) {
            return false;
        }

        if (phoneNumberTwoTextField.getText() == null || (phoneNumberTwoTextField.getText().isEmpty())) {
            return false;
        }

        if (phoneNumberThreeTextField.getText() == null || (phoneNumberThreeTextField.getText().isEmpty())) {
            return false;
        }

        if (hoursStartTimeTextField.getText() == null || (hoursStartTimeTextField.getText().isEmpty())) {
            return false;
        }

        if (hoursEndTimeTextField.getText() == null || (hoursEndTimeTextField.getText().isEmpty())) {
            return false;
        }

        return true;
    }

    @FXML
    private void displaySelectedDocInfo() {
        newProfileSetUp();
        Doctor selectedDoc = directoryTable.getSelectionModel().getSelectedItem();

        nameTextField.setText(selectedDoc.getName());

        String phoneNum = selectedDoc.getPhoneNum();

        if (!phoneNum.equals("N/A")) {
            String[] phoneNumber = phoneNum.split("-");

            phoneNumberOneTextField.setText(phoneNumber[0]);
            phoneNumberTwoTextField.setText(phoneNumber[1]);
            phoneNumberThreeTextField.setText(phoneNumber[2]);
        }

        if (!(selectedDoc.getOffice().getOfficeNum().equals("N/A"))) {
            officeNumberTextField.setText(selectedDoc.getOffice().getOfficeNum());
        }

        String dept = selectedDoc.getDepts();
        String[] depts = dept.split(", ");

        for (String d: depts) {
            int i = 0;
            for (String dpt: departments) {
                if (dpt.equals(d)) {
                    departmentsList.getSelectionModel().selectIndices(i);
                }

                i += 1;
            }
        }

        String hour = selectedDoc.getHours();

        if (!(hour.equals("N/A"))) {
            String[] hours = hour.split("-");

            hoursStartTimeTextField.setText(hours[0]);
            hoursEndTimeTextField.setText(hours[1]);
        }

    }

    public void onMapBuilderSwitch(ActionEvent actionEvent)
    {
        try
        {
            SceneSwitcher.switchToMapEditorView(this.getStage());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
