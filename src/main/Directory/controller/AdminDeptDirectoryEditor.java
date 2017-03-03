package main.Directory.controller;

import main.Application.ApplicationController;
import main.Directory.Boundary.AdminDeptDirectoryBoundary;
import main.Map.Entity.Destination;
import main.Map.Entity.Office;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Created by jw97 on 2/27/2017.
 */
public class AdminDeptDirectoryEditor extends AnchorPane {
    TranslateTransition directorySlide = new TranslateTransition(Duration.millis(600), this);
    Boolean deptDirectoryUp = false;
    private AdminDeptDirectoryBoundary deptBoundary;
    ObservableList<Destination> existingLoc =
            FXCollections.observableArrayList(ApplicationController.getHospital().getDestinations());

    public AdminDeptDirectoryEditor() {
        deptBoundary = new AdminDeptDirectoryBoundary(ApplicationController.getHospital());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/directory/AdminDeptDirectoryEditor.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public AnchorPane mainDirectoryPane;

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

    private void initialize() {
        deptNameCol.setCellValueFactory(new PropertyValueFactory<Office, String>("name"));

        deptLocCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Office, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Office, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getDestination().getName());
            }
        });

        if (deptBoundary.getDepartments() != null) {
            deptDataTable.setItems(deptBoundary.getDepartments());
        }

        // Adding a listener to the search bar, filtering through the data as the user types
        searchDeptBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            deptDataTable.setItems(deptBoundary.setSearchList(newValue));
        });

        locAssignedField.setItems(existingLoc);
        showDeptOptions();
    }

    private void reset() {
        searchDeptBar.clear();
        searchDeptBar.setStyle("-fx-text-inner-color: white;");
        deptDataTable.getSelectionModel().clearSelection();
        editorButton.setText("Add");
        editDeptFields.setVisible(false);
    }

    public void hideDirectory() {
        deptDirectoryUp = false;

        directorySlide.setToY(0);
        directorySlide.play();
    }

    public void showDirectory() {
        deptDirectoryUp = true;

        directorySlide.setToY(-620);
        directorySlide.play();

        initialize();
    }


    @FXML
    private void changeDirectory() {
        if (deptDirectoryUp) {
            hideDirectory();
        } else {
            reset();
            showDirectory();
        }

        deptDataTable.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evt) {
                if (evt.getCode() == KeyCode.ESCAPE) {
                    hideDirectory();
                }
            }
        });
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

            Office newOffice = new Office(deptName, assignedDest);

            deptBoundary.addDept(newOffice);

            deptDataTable.requestFocus();
            deptDataTable.getSelectionModel().select(newOffice);
            int i = deptDataTable.getSelectionModel().getSelectedIndex();
            deptDataTable.getFocusModel().focus(i);
            deptDataTable.scrollTo(i);

            reset();
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
