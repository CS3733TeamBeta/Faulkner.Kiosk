package Controller.User;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import sun.invoke.empty.Empty;

import java.io.IOException;


/**
 * Created by jw97 on 2/16/2017.
 *
 */
public class UserMapViewALTController extends AbstractController {
    Boolean downArrow = true; // By default, the navigation arrow is to minimize the welcome page
    ColorAdjust colorAdjust = new ColorAdjust();
    int numClickDr = 0;
    int numClickFood = 0;
    int numClickBath = 0;
    int numClickHelp = 0;

    @FXML
    AnchorPane mainPane;

    @FXML
    AnchorPane searchMenu;

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
    Text welcomeGreeting;

    @FXML
    TreeTableView deptTable;

    @FXML
    TreeTableView doctorTable;

    Stage primaryStage;

    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    public void defaultProperty() {
        searchMenu.setStyle("-fx-background-color:  #e6e6fa;");

        // Sets the color of the icons to black
        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        doctorIcon.setEffect(original);
        bathroomIcon.setEffect(original);
        foodIcon.setEffect(original);
        helpIcon.setEffect(original);

        // By default, only the departments table is shown
        deptTable.setVisible(true);
        // Set all other tables false
        doctorTable.setVisible(false);
        searchBar.setPromptText("Search for Departments");

        // Title shown
        welcomeGreeting.setVisible(true);
    }

    public void searchMenuUp() {
        Timeline menuSlideDown = new Timeline();
        KeyFrame keyFrame;
        menuSlideDown.setCycleCount(1);
        menuSlideDown.setAutoReverse(true);

        if (downArrow) { // Navigate down icon -> welcome page down (left with search bar)
            KeyValue welcomeDown = new KeyValue(searchMenu.translateYProperty(), 180);
            keyFrame = new KeyFrame(Duration.millis(600), welcomeDown);
            welcomeGreeting.setVisible(false);
            downArrow = false; // Changes to up icon
            searchMenu.setStyle("-fx-background-color: transparent;");
        } else { // Navigate up icon -> show welcome page
            KeyValue welcomeUp = new KeyValue(searchMenu.translateYProperty(), 0);
            keyFrame = new KeyFrame(Duration.millis(600), welcomeUp);

            // Reset to default
            defaultProperty();

            downArrow = true;
            numClickDr = 0;
            numClickFood = 0;
            numClickBath = 0;
            numClickHelp = 0;

            searchBar.clear();
        }

        navigateArrow.setRotate(navigateArrow.getRotate() + 180); // Changes to direction of arrow icon

        menuSlideDown.getKeyFrames().add(keyFrame);
        menuSlideDown.play();
    }

    public void loadMenu() {
        defaultProperty();

        Timeline menuSlideUp = new Timeline();
        menuSlideUp.setCycleCount(1);
        menuSlideUp.setAutoReverse(true);

        KeyValue menuUp = new KeyValue(searchMenu.translateYProperty(), -(mainPane.getHeight() - 350));
        KeyFrame keyFrame = new KeyFrame(Duration.millis(600), menuUp);

        menuSlideUp.getKeyFrames().add(keyFrame);

        menuSlideUp.play();
    }

    public void doctorSelected() {
        loadMenu();
        numClickDr = numClickDr + 1;
        numClickHelp = 0;
        numClickBath = 0;
        numClickFood = 0;

        if (numClickDr == 2) {
            defaultProperty();
            numClickDr = 0;
        } else {
            defaultProperty();

            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);

            doctorIcon.setEffect(clicked);

            searchBar.setPromptText("Search for doctors");

            deptTable.setVisible(false);
            doctorTable.setVisible(true);
        }
    }

    public void bathroomSelected() {
        loadMenu();
        numClickBath = numClickBath + 1;
        numClickHelp = 0;
        numClickDr = 0;
        numClickFood = 0;

        if (numClickBath == 2) {
            defaultProperty();
            numClickBath = 0;
        } else {
            defaultProperty();

            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);

            ColorAdjust original = new ColorAdjust();
            original.setContrast(0);

            bathroomIcon.setEffect(clicked);

            searchBar.setPromptText("Search for bathrooms");
        }
    }

    public void foodSelected() {
        loadMenu();
        numClickFood = numClickFood + 1;
        numClickHelp = 0;
        numClickBath = 0;
        numClickDr = 0;

        if (numClickFood == 2) {
            defaultProperty();
            numClickFood = 0;
        } else {
            defaultProperty();

            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);

            ColorAdjust original = new ColorAdjust();
            original.setContrast(0);
            foodIcon.setEffect(clicked);

            searchBar.setPromptText("Search for food");
        }
    }

    public void helpSelected() {
        loadMenu();
        numClickHelp = numClickHelp + 1;
        numClickDr = 0;
        numClickBath = 0;
        numClickFood = 0;

        if (numClickHelp == 2) {
            defaultProperty();
            numClickHelp = 0;
        } else {
            defaultProperty();
            ColorAdjust clicked = new ColorAdjust();
            clicked.setContrast(-10);

            ColorAdjust original = new ColorAdjust();
            original.setContrast(0);
            helpIcon.setEffect(clicked);

            searchBar.setPromptText("Search for help");
        }
    }

    public void adminLogin() throws IOException {
        SceneSwitcher.switchToLoginView(primaryStage);
    }
}