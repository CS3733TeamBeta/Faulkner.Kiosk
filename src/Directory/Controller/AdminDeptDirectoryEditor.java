package Directory.Controller;

import Directory.Boundary.AdminDeptDirectoryBoundary;
import Map.Entity.Destination;
import Map.Entity.Office;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Created by jw97 on 2/27/2017.
 */
public class AdminDeptDirectoryEditor extends AnchorPane {
    Boolean deptDirectoryUp = false;
    ObservableList<Destination> existingLoc = FXCollections.observableArrayList();

    @FXML
    public AnchorPane mainDirectoryPane;

    @FXML
    private AnchorPane deptDirectory;

    @FXML
    private JFXTextField searchDeptBar;

    @FXML
    private TableView<Office> deptDataTable;

    @FXML
    private TableColumn<Office, String> deptNameCol;

    @FXML
    private TableColumn<Office, String> deptLocCol;

    @FXML
    private HBox editDeptFields;

    @FXML
    private TextField deptNameField;

    @FXML
    private ComboBox<Destination> locAssignedField;

    @FXML
    private Button editorButton;

    private AdminDeptDirectoryBoundary deptBoundary;

    public AdminDeptDirectoryEditor() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "../../Admin/AdminDeptDirectoryEditor.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        searchDeptBar.clear();
        searchDeptBar.setStyle("-fx-text-inner-color: white;");
        deptDataTable.getSelectionModel().clearSelection();

        loadData();
    }

    public void loadData() {
        deptNameCol.setCellValueFactory(new PropertyValueFactory<Office, String>("name"));

        deptLocCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Office, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Office, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getDestination().getName());
            }
        });

        // Adding a listener to the search bar, filtering through the data as the user types
        searchDeptBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            // Create a sorted list for the filtered data list
            SortedList<Office> sortedDepts = new SortedList<>(deptBoundary.setSearchList(newValue));
            // Bind the sorted list to table
            sortedDepts.comparatorProperty().bind(deptDataTable.comparatorProperty());
            // Set table data
            deptDataTable.setItems(sortedDepts);
        });

        locAssignedField.setItems(existingLoc);

        editDeptFields.setVisible(false);
        editorButton.setText("Add");
        showDeptOptions();
    }

    private KeyFrame hideDirectory() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(600), new KeyValue(deptDirectory.translateYProperty(),
                -(deptDirectory.getHeight() - 80)));
        deptDirectoryUp = false;

        return keyFrame;
    }

    private KeyFrame showDirectory() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(600), new KeyValue(deptDirectory.translateYProperty(),
                (deptDirectory.getHeight() - 700)));
        deptDirectoryUp = true;

        return keyFrame;
    }


    @FXML
    private void changeDirectory() {
        Timeline directorySlide = new Timeline();
        KeyFrame keyFrame;
        directorySlide.setCycleCount(1);
        directorySlide.setAutoReverse(true);

        if (deptDirectoryUp) {
            keyFrame = hideDirectory();
        } else {
            keyFrame = showDirectory();
        }

        directorySlide.getKeyFrames().add(keyFrame);
        directorySlide.play();
    }

    @FXML
    private void showEditor() {
        deptNameField.clear();
        locAssignedField.setValue(null);
        editorButton.setText("Add");
        editDeptFields.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(1500), editDeptFields);

        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.setAutoReverse(true);
        ft.play();
    }

    @FXML
    private void showDeptOptions() {
        MenuItem edit = new MenuItem("Edit");
        MenuItem delete = new MenuItem("Delete");

        edit.setOnAction((ActionEvent e) -> {
            showEditor();
            editorButton.setText("Save");

            Office o = deptDataTable.getSelectionModel().getSelectedItem();

            deptNameField.setText(o.getName());
            locAssignedField.setValue(o.getDestination());
        });

        delete.setOnAction((ActionEvent e) -> {
            Office o = deptDataTable.getSelectionModel().getSelectedItem();

            deptBoundary.removeDept(o);
        });

        ContextMenu options = new ContextMenu();
        options.getItems().addAll(edit, delete);
        deptDataTable.setContextMenu(options);
    }

    @FXML
    private void deptEditOperation() {
        if (editorProcessable()) {
            String deptName = deptNameField.getText();
            Destination assignedDest = new Destination();

            switch (editorButton.getText()) {
                case "Add":
                    Office newOffice = new Office(deptName, assignedDest);

                    deptBoundary.addDept(newOffice);

                    break;

                case "Save":
                    Office o = deptDataTable.getSelectionModel().getSelectedItem();

                    deptBoundary.editDept(o, deptName, assignedDest);

                    break;
                default:
                    break;
            }
        }
    }

    private boolean editorProcessable() {
        if (deptNameField.getText() == null || (deptNameCol.getText().isEmpty())){
            return false;
        }

        if (locAssignedField.getValue() == null){
            return false;
        }

        return true;
    }
}
