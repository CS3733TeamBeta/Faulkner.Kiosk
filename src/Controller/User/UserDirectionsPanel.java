package Controller.User;

import Domain.Navigation.DirectionStep;
import Domain.Navigation.Guidance;
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

    Guidance guidance;
    int stepIndex =0;
    ImageView MapImage;


    public UserDirectionsPanel(ImageView mapImage)
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

        this.MapImage = mapImage;
    }

    @FXML
    public AnchorPane mainPane;

    @FXML
    private GridPane locationGridPane;

    @FXML
    private JFXListView<Label> directionsListView;

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

    public void setGuidance(Guidance g)
    {
        stepIndex = 0;
        guidance = g; //@TODO Make GUIDANCE ITERABLE
    }

    public void fillDirectionsList(DirectionStep step)
    {
        directionsListView.getItems().clear();

        for(String s: step.getDirections())
        {
            Label l = new Label(s);
            directionsListView.getItems().add(l);
        }
    }

    @FXML
    void onCloseButtonClicked(MouseEvent event)
    {

    }

    @FXML
    void onNextButtonClicked(MouseEvent event)
    {
        stepIndex++;

        if(stepIndex<guidance.getSteps().size()-1)
        {
            fillDirectionsList(guidance.getSteps().get(stepIndex));
        }
    }

    @FXML
    void onPreviousButtonClicked(MouseEvent event)
    {
        stepIndex--;

        if(stepIndex>0)
        {
            fillDirectionsList(guidance.getSteps().get(stepIndex));
        }

        this.MapImage.setImage(guidance.getSteps().get(stepIndex).getFloor().getImageInfo().getFXImage());
    }

    @FXML
    void onSendEmail(ActionEvent event)
    {

    }
}