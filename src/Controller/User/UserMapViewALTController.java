package Controller.User;

import Controller.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;


/**
 * Created by jw97 on 2/16/2017.
 *
 */
public class UserMapViewALTController extends AbstractController {
    Boolean menuUp = false;
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
    ImageView upIcon;

    @FXML
    TextField searchBar;

    @FXML
    Text welcomeGreeting;

    @FXML
    TreeTableView deptTable;

    @FXML
    TreeTableView doctorTable;

    public void defaultProperty() {
        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        doctorIcon.setEffect(original);
        bathroomIcon.setEffect(original);
        foodIcon.setEffect(original);
        helpIcon.setEffect(original);


        deptTable.setVisible(true);
        // Set all other tables false
        doctorTable.setVisible(false);

        searchBar.setPromptText("Search for...");
    }

    // Show search menu
    public void searchMenuUp() {
        Timeline menuSlideUp = new Timeline();

        menuSlideUp.setCycleCount(1);
        menuSlideUp.setAutoReverse(true);

        if (menuUp) {
            KeyValue valDown = new KeyValue(searchMenu.translateYProperty(), (mainPane.getHeight() - 700));
            KeyFrame keyFrame = new KeyFrame(Duration.millis(600), valDown);
            menuSlideUp.getKeyFrames().add(keyFrame);
            menuUp = false;
            welcomeGreeting.setVisible(false);

        } else {
            welcomeGreeting.setVisible(true);
            defaultProperty();
            KeyValue valUp = new KeyValue(searchMenu.translateYProperty(), -(mainPane.getHeight() - (mainPane.getHeight() - 530)));
            KeyFrame keyFrame = new KeyFrame(Duration.millis(600), valUp);
            menuSlideUp.getKeyFrames().add(keyFrame);
            menuUp = true;
        }

        upIcon.setRotate(upIcon.getRotate() + 180);

        menuSlideUp.play();
    }


    public void doctorSelected() {
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
}