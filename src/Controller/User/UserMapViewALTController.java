package Controller.User;

import Controller.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;


/**
 * Created by jw97 on 2/16/2017.
 */
public class UserMapViewALTController extends AbstractController {
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
    TextField searchBar;

    Stage primaryStage;
    public UserMapViewALTController(){}
    public void setStage(Stage s)
    {
        primaryStage = s;
    }

    // Show search menu
    public void searchMenuUp() {
        Timeline menuSlideUp = new Timeline();

        menuSlideUp.setCycleCount(1);
        menuSlideUp.setAutoReverse(true);

        if (menuUp) {
            KeyValue valUp = new KeyValue(searchMenu.translateYProperty(), (mainPane.getHeight() - 566));
            KeyFrame keyFrame = new KeyFrame(Duration.millis(600), valUp);
            menuSlideUp.getKeyFrames().add(keyFrame);
            menuUp = false;
        } else {
            KeyValue valUp = new KeyValue(searchMenu.translateYProperty(), -mainPane.getHeight());
            KeyFrame keyFrame = new KeyFrame(Duration.millis(600), valUp);
            menuSlideUp.getKeyFrames().add(keyFrame);
            menuUp = true;
        }

        menuSlideUp.play();
    }


    public void doctorSelected() {
        colorAdjust.setSaturation(0.1);
        doctorIcon.setEffect(colorAdjust);

        searchBar.setText("Search for doctors");
    }

    public void bathroomSelected() {

        searchBar.setText("Search for bathrooms");
    }

    public void foodSelected() {

        searchBar.setText("Search for food");

    }

    public void helpSelected() {

        searchBar.setText("Search for help");
    }
}
