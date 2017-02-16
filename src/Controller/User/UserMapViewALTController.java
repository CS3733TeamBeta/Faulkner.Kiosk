package Controller.User;

import Controller.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
 */
public class UserMapViewALTController extends AbstractController{
    Boolean menuUp = false;
    ColorAdjust colorAdjust = new ColorAdjust();

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

    public void defaultProperty() {
        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        doctorIcon.setEffect(original);
        bathroomIcon.setEffect(original);
        foodIcon.setEffect(original);
        helpIcon.setEffect(original);

        searchBar.setPromptText("Search for...");
    }

    // Show search menu
    public void searchMenuUp() {
        Timeline menuSlideUp = new Timeline();

        menuSlideUp.setCycleCount(1);
        menuSlideUp.setAutoReverse(true);

        if (menuUp) {
            KeyValue valDown = new KeyValue(searchMenu.translateYProperty(), (mainPane.getHeight() - 1000));
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
        defaultProperty();

        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        doctorIcon.setEffect(clicked);

        searchBar.setPromptText("Search for doctors");
    }

    public void bathroomSelected() {
        defaultProperty();

        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        bathroomIcon.setEffect(clicked);

        searchBar.setPromptText("Search for bathrooms");
    }

    public void foodSelected() {
        defaultProperty();

        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        foodIcon.setEffect(clicked);

        searchBar.setPromptText("Search for food");

    }

    public void helpSelected() {
        defaultProperty();

        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        helpIcon.setEffect(clicked);

        searchBar.setPromptText("Search for help");
    }
}
