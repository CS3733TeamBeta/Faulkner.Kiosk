package Controller.User;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.IOException;

public class UserDirectionsPanel extends AnchorPane
{

    public UserDirectionsPanel()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "../../User/UserDirectionsPanel.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public AnchorPane mainPane;

    @FXML
    private GridPane locationGridPane;

    @FXML
    private JFXListView<?> directionsListView;

    @FXML
    private ImageView previousButton;

    @FXML
    private ImageView nextButton;

    @FXML
    private Label previousLabel;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXButton sendEmailButton;

    @FXML
    private ImageView closeButton;

    public void setCloseHandler(EventHandler<? super MouseEvent> e)
    {
        closeButton.setOnMouseClicked(e);
    }

    @FXML
    void onCloseButtonClicked(MouseEvent event)
    {

    }

    @FXML
    void onNextButtonClicked(MouseEvent event)
    {

    }

    @FXML
    void onPreviousButtonClicked(MouseEvent event)
    {

    }

    @FXML
    void onSendEmail(ActionEvent event)
    {

    }
}