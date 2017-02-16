package Controller.User;

import Controller.AbstractController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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


    // Show search menu
    public void searchMenuUp() {
        Timeline menuSlideUp = new Timeline();

        menuSlideUp.setCycleCount(1);
        menuSlideUp.setAutoReverse(true);

        if (menuUp) {
            KeyValue valDown = new KeyValue(searchMenu.translateYProperty(), 850);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(600), valDown);
            menuSlideUp.getKeyFrames().add(keyFrame);
            menuUp = false;

        } else {
            KeyValue valUp = new KeyValue(searchMenu.translateYProperty(), -(mainPane.getHeight() - 350));
            KeyFrame keyFrame = new KeyFrame(Duration.millis(600), valUp);
            menuSlideUp.getKeyFrames().add(keyFrame);
            menuUp = true;
        }

        upIcon.setRotate(upIcon.getRotate() + 180);

        menuSlideUp.play();
    }


    public void doctorSelected() {
        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        doctorIcon.setEffect(clicked);
        bathroomIcon.setEffect(original);
        helpIcon.setEffect(original);
        foodIcon.setEffect(original);

        searchBar.setText("Search for doctors");
    }

    public void bathroomSelected() {
        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        bathroomIcon.setEffect(clicked);
        doctorIcon.setEffect(original);
        helpIcon.setEffect(original);
        foodIcon.setEffect(original);

        searchBar.setText("Search for bathrooms");
    }

    public void foodSelected() {
        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        foodIcon.setEffect(clicked);
        bathroomIcon.setEffect(original);
        helpIcon.setEffect(original);
        doctorIcon.setEffect(original);

        searchBar.setText("Search for food");

    }

    public void helpSelected() {
        ColorAdjust clicked = new ColorAdjust();
        clicked.setContrast(-10);

        ColorAdjust original = new ColorAdjust();
        original.setContrast(0);
        helpIcon.setEffect(clicked);
        bathroomIcon.setEffect(original);
        doctorIcon.setEffect(original);
        foodIcon.setEffect(original);

        searchBar.setText("Search for help");
    }
}
