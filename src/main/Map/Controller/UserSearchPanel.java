package main.Map.Controller;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import main.Application.ApplicationController;
import main.Directory.Boundary.UserDirectoryBoundary;
import main.Map.Entity.Destination;
import main.Map.Entity.Office;
import main.Directory.Entity.Doctor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Created by jw97 on 2/28/2017.
 */
public class UserSearchPanel extends AnchorPane {
    TranslateTransition translateTransition = new TranslateTransition(Duration.millis(600), this);
    //FadeTransition backgroundFade = new FadeTransition(Duration.millis(400), this.getBackground());
   // FillTransition fade = new FillTransition(Duration.millis(500), this.getBackground().getFills().get(0));

    UserDirectoryBoundary boundary;
    ColorAdjust original = new ColorAdjust();
    Boolean welcome = true;

    int numClickDr;
    int numClickFood;
    int numClickBath;
    int numClickHelp;

    public UserSearchPanel() throws Exception {
        boundary = new UserDirectoryBoundary(ApplicationController.getHospital());

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
    TableColumn<Office, Destination> deptLocCol;

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

    AnchorPane pane;

    boolean transparent = false;

    private void initialize() {
        docNameCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("name"));
        jobTitleCol.setCellValueFactory(new PropertyValueFactory<Doctor, String>("description"));

        deptNameCol.setCellValueFactory(new PropertyValueFactory<Office, String>("name"));
        deptLocCol.setCellValueFactory(new PropertyValueFactory<Office, Destination>("destination"));

        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            loadSearchMenu();
            if (deptTable.isVisible()) {
                deptTable.setItems(boundary.setSearchListForDept(newValue));
            } else {
                doctorTable.setItems(boundary.setSearchListForDoc(newValue));
            }
        });

        if (boundary.getDoctors() != null) {
            doctorTable.setItems(boundary.getDoctors());
        }

        if (boundary.getDepartments() != null) {
            deptTable.setItems(boundary.getDepartments());
        }

        docLocsCol.setCellFactory(col -> {
            ComboBox<Destination> loc = new ComboBox<Destination>();

            TableCell<Doctor, Destination> cell = new TableCell<Doctor, Destination>() {
                @Override
                public void updateItem(Destination item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        loc.setItems(getTableView().getItems().get(getIndex()).getDestinations());
                        loc.setMaxWidth(docLocsCol.getMaxWidth());
                        setGraphic(loc);
                    }
                }
            };

            return cell;
        });

        docNavigateCol.setCellFactory(col -> {
            Button navigateButton = new Button("Go");
            TableCell<Doctor, Doctor> cell = new TableCell<Doctor, Doctor>() {
                @Override
                public void updateItem(Doctor d, boolean empty) {
                    super.updateItem(d, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(navigateButton);
                    }
                }
            };

            return cell;
        });

        pane = this;
    }

    private void selectionMode(ImageView icon) {
        defaultProperty();
        loadSearchMenu();
        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        icon.setEffect(clicked);
    }


    private void defaultProperty() {
        original.setContrast(0);
        this.setStyle("-fx-background-color:  #f2f2f2;");

        // Sets the color of the icons to black
        doctorIcon.setEffect(original);
        bathroomIcon.setEffect(original);
        foodIcon.setEffect(original);
        helpIcon.setEffect(original);

        numClickDr = -1;
        numClickFood = -1;
        numClickBath = -1;
        numClickHelp = -1;

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
                pane.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        animation.play();

        transparent = false;

        welcomeGreeting.setVisible(true);
        defaultProperty();
        initialize();

        welcome = true;

        navigateArrow.setOnMouseClicked(e -> {
            if (welcome) {
                hideWelcomeScreen();
            } else {
                welcomeScreen();
            }

            navigateArrow.setRotate(180);
        });

        translateTransition.setToY(350);
        translateTransition.play();
    }


    private void loadSearchMenu() {

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
                    pane.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            };
            animation.play();

            transparent=false;
        }

        translateTransition.setToY(0);
        translateTransition.play();
    }

    @FXML
    private void hideWelcomeScreen() {
        final Animation animation = new Transition() {

            {
                setCycleDuration(Duration.millis(500));
                setInterpolator(Interpolator.LINEAR);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(1, 1, 1, .8 - (frac-.2));
                pane.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        transparent = true;
        animation.play();

        welcomeGreeting.setVisible(false);
        welcome = false;
        translateTransition.setToY(350 + 175);
        translateTransition.play();
    }


    @FXML
    private void doctorSelected() {
        selectionMode(doctorIcon);
        numClickDr = numClickDr * (-1);
        displayTable();
    }

    @FXML
    private void bathroomSelected()
    {
        selectionMode(bathroomIcon);
        numClickBath = numClickBath * (-1);
        displayTable();
    }

    @FXML
    private void foodSelected()
    {
        selectionMode(foodIcon);
        numClickFood = numClickFood * (-1);
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
            searchBar.setPromptText("Search for doctors");
            deptTable.setVisible(false);
            doctorTable.setVisible(true);
        }
        if (numClickBath == 1)
        {
            searchBar.setPromptText("Search for bathrooms");
        }

        if (numClickFood == 1)
        {
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
