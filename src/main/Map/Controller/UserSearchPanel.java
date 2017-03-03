package main.Map.Controller;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import main.Application.ApplicationController;
import main.Application.Exceptions.PathFindingException;
import main.Directory.Boundary.UserDirectoryBoundary;
import main.Directory.Entity.Doctor;
import main.Map.Entity.Destination;
import main.Map.Entity.Office;

import java.io.IOException;

/**
 * Created by jw97 on 2/28/2017.
 */
public class UserSearchPanel extends AnchorPane {
    TranslateTransition translateTransition = new TranslateTransition(Duration.millis(600), this);
    //FadeTransition backgroundFade = new FadeTransition(Duration.millis(400), this.getBackground());
   // FillTransition fade = new FillTransition(Duration.millis(500), this.getBackground().getFills().get(0));

    @FXML
    TextArea helpText;

    UserDirectoryBoundary boundary;
    ColorAdjust original = new ColorAdjust();
    ColorAdjust clicked = new ColorAdjust();
    Boolean welcome = true;

    int numClickDr = -1;
    int numClickFood = -1;
    int numClickBath = -1;
    int numClickHelp = -1;

    UserMapViewController c;

    public UserSearchPanel(UserMapViewController c) throws Exception {
        boundary = new UserDirectoryBoundary(ApplicationController.getHospital());
        this.c = c;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/directory/UserSearchPanel.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    Text welcomeGreeting;

    @FXML
    ImageView doctorIcon;

    @FXML
    ImageView bathroomIcon;

    @FXML
    ImageView foodIcon;

    @FXML
    ImageView helpIcon;

    @FXML
    ImageView navigateArrow;

    @FXML
    TextField searchBar;

    @FXML
    TableView<Office> deptTable;

    @FXML
    TableColumn<Office, String> deptNameCol;

    @FXML
    TableColumn<Office, String> deptLocCol;

    @FXML
    TableColumn<Office, Office> deptNavigateCol;

    @FXML
    TableView<Doctor> doctorTable;

    @FXML
    TableColumn<Doctor, String> docNameCol;

    @FXML
    TableColumn<Doctor, String> jobTitleCol;

    @FXML
    TableColumn<Doctor, Destination> docLocsCol;

    @FXML
    TableColumn<Doctor, Doctor> docNavigateCol;

    Destination selectedDest;

    int index;

    boolean transparent = false;

    private void initialize() {

        helpText.setVisible(false);

        docNameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        jobTitleCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("description"));

        deptNameCol.setCellValueFactory(new PropertyValueFactory<Office, String>("name"));
        deptLocCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Office, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Office, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getDestination().getName());
            }
        });

        if (boundary.getDoctors() != null) {
            doctorTable.setItems(boundary.getDoctors());
        }

        if (boundary.getDepartments() != null) {
            deptTable.setItems(boundary.getDepartments());
        }

        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            loadSearchMenu();
            if (deptTable.isVisible()) {
                deptTable.setItems(boundary.setSearchListForDept(newValue));
            } else {
                doctorTable.setItems(boundary.setSearchListForDoc(newValue));
            }
        });

        docLocsCol.setCellFactory(col -> {
            ComboBox<Destination> loc = new ComboBox<Destination>();

            TableCell<Doctor, Destination> cell = new TableCell<Doctor, Destination>() {
                @Override
                public void updateItem(Destination item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        loc.setItems(getTableView().getItems().get(this.getIndex()).getDestinations());
                        loc.setMaxWidth(docLocsCol.getMaxWidth());
                        loc.setPromptText("Select a location");
                        setGraphic(loc);
                    }
                }
            };

            loc.valueProperty().addListener(new ChangeListener<Destination>() {
                @Override public void changed(ObservableValue ov, Destination d, Destination d1) {
                    Destination candidate = d1;
                    int index = cell.getIndex();
                    generateButtonCells(index, candidate);
                }
            });

            return cell;
        });

        deptTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Destination candidate = newSelection.getDestination();

                deptNavigateCol.setCellFactory(col -> {
                    Button navigateButton = new Button("Go");
                    TableCell<Office, Office> cell = new TableCell<Office, Office>() {
                        @Override
                        public void updateItem(Office o, boolean empty) {
                            super.updateItem(o, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                if (this.getIndex() == deptTable.getSelectionModel().getSelectedIndex()) {
                                    setGraphic(navigateButton);
                                }
                            }
                        }
                    };

                    navigateButton.setOnAction(e -> {
                        selectedDest = candidate;
                        try {
                            c.setDestination(selectedDest);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    });

                    return cell;
                });
            }
        });
    }

    private void generateButtonCells(int index, Destination candidate) {
        docNavigateCol.setCellFactory(col -> {
            Button navigateButton = new Button("Go");
            TableCell<Doctor, Doctor> cell = new TableCell<Doctor, Doctor>() {
                @Override
                public void updateItem(Doctor d, boolean empty) {
                    super.updateItem(d, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        if (this.getIndex() == index) {
                            setGraphic(navigateButton);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };

            navigateButton.setOnAction(e -> {
                selectedDest = candidate;
                try {
                    c.setDestination(selectedDest);
                } catch (PathFindingException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });

            return cell;
        });
    }

    private void selectionMode(ImageView icon) {
        defaultProperty();
        loadSearchMenu();
        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        icon.setEffect(clicked);
    }

    @FXML
    private void defaultProperty() {
        original.setContrast(0);
        this.setStyle("-fx-background-color:  #f2f2f2;");

        helpText.setVisible(false);
        // Sets the color of the icons to black
        doctorIcon.setEffect(original);
        bathroomIcon.setEffect(original);
        foodIcon.setEffect(original);
        helpIcon.setEffect(original);

        deptTable.setVisible(true);

        doctorTable.setVisible(false);

        searchBar.clear();
        searchBar.setPromptText("Search for Departments");
    }

    public void welcomeScreen() {

        final Animation animation = new Transition() {

            {
                setCycleDuration(Duration.millis(500));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(1, 1, 1, frac);
                setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };

        animation.play();

        transparent = false;

        welcomeGreeting.setVisible(true);
        defaultProperty();
        initialize();

        welcome = true;
        navigateArrow.setRotate(0);

        //translateTransition.setToY(350);
        translateTransition.setToY(0);
        translateTransition.play();
    }


    private void loadSearchMenu() {
        this.toFront();
        navigateArrow.setRotate(0);
        welcomeGreeting.setVisible(true);
        this.setStyle("-fx-background-color:  #f2f2f2;");

        if(transparent)
        {
            final Animation animation = new Transition() {

                {
                    setCycleDuration(Duration.millis(500));
                    setInterpolator(Interpolator.LINEAR);
                }

                @Override
                protected void interpolate(double frac) {
                    Color vColor = new Color(1, 1, 1, frac);
                    setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            };
            animation.play();

            transparent=false;
        }


        translateTransition.setToY(-350);
        translateTransition.play();
    }

    public void hideWelcomeScreen() {
        navigateArrow.setRotate(180);
        this.setStyle("-fx-background-color:  transparent;");

        if(!transparent)
        {
            final Animation animation = new Transition() {

                {
                    setCycleDuration(Duration.millis(500));
                    setInterpolator(Interpolator.LINEAR);
                }

                @Override
                protected void interpolate(double frac) {
                    Color vColor = new Color(1, 1, 1, .8 - (frac-.2));
                    setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            };

            animation.play();
            transparent = true;
        }

        welcomeGreeting.setVisible(false);
        welcome = false;
        //translateTransition.setToY(350 + 175);
        translateTransition.setToY(175);
        translateTransition.play();
    }

    public void addNavigation() {
        navigateArrow.setOnMouseClicked(e -> {
            if (welcome) {
                hideWelcomeScreen();
            } else {
                welcomeScreen();
            }
        });
    }

    private void isDeSelected(int n, ImageView icon) {
        if (n < 0) {
            defaultProperty();
        } else {
            selectionMode(icon);
        }
    }

    @FXML
    private void doctorSelected() {
        numClickDr = numClickDr * (-1);
        isDeSelected(numClickDr, doctorIcon);
        displayTable();
    }

    @FXML
    private void bathroomSelected()
    {
        numClickBath = numClickBath * (-1);
        isDeSelected(numClickBath, bathroomIcon);
        displayTable();
    }

    @FXML
    private void foodSelected()
    {
        numClickFood = numClickFood * (-1);
        isDeSelected(numClickFood, foodIcon);
        displayTable();
    }

    @FXML
    private void helpSelected()
    {
        selectionMode(helpIcon);
        numClickHelp = numClickHelp * (-1);

        displayTable();
    }

    private void displayTable()
    {
        if (numClickDr == 1)
        {
            helpText.setVisible(false);
            searchBar.setPromptText("Search for doctors");
            deptTable.setVisible(false);
            doctorTable.setVisible(true);
        }
        if (numClickBath == 1)
        {
            helpText.setVisible(false);
            searchBar.setPromptText("Search for bathrooms");
        }

        if (numClickFood == 1)
        {
            helpText.setVisible(false);
            searchBar.setPromptText("Search for food");
        }

        if (numClickHelp == 1)
        {
            searchBar.setPromptText("Search for help");
        }

        if ((numClickDr == -1) && (numClickBath == -1) && (numClickFood == -1) && (numClickHelp == -1))
        {
            defaultProperty();
        }
    }
}
