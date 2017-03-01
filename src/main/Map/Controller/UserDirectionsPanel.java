package main.Map.Controller;


import main.Map.Navigation.DirectionFloorStep;
import main.Map.Navigation.DirectionStep;
import main.Map.Navigation.Guidance;
import main.Application.Events.StepChangedEvent;
import main.Application.Events.StepChangedEventHandler;

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

import java.io.IOException;
import java.util.ArrayList;

public class UserDirectionsPanel extends AnchorPane
{

    Guidance guidance;
    int stepIndex = 0;
    ImageView MapImage;
    ArrayList<StepChangedEventHandler> stepChangedEventHandlers;

    public UserDirectionsPanel(ImageView mapImage)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/map/UserDirectionsPanel.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        stepChangedEventHandlers = new ArrayList<>();

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

    public void addOnStepChangedHandler(StepChangedEventHandler h)
    {
        stepChangedEventHandlers.add(h);
    }

    public void onStepChanged(DirectionFloorStep step)
    {
        for(StepChangedEventHandler stepChangedEventHandler: stepChangedEventHandlers)
        {
            stepChangedEventHandler.handle(new StepChangedEvent(step));
        }
    }

    public void fillGuidance(Guidance g)
    {
        this.guidance = g;
        stepIndex = 0;

        fillDirectionsList(stepIndex);
    }

    public void fillDirectionsList(int index)
    {
        fillDirectionsList(guidance.getSteps().get(index));
    }

    public void fillDirectionsList(DirectionFloorStep step)
    {
        directionsListView.getItems().clear();
        for(DirectionFloorStep s : guidance.getSteps()) {
            for (DirectionStep aStep : s.getDirectionSteps()) {
                Label l = new Label(aStep.toString());
                directionsListView.getItems().add(l);
            }
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

        if(stepIndex<guidance.getSteps().size())
        {
            fillDirectionsList(guidance.getSteps().get(stepIndex));
            onStepChanged(guidance.getSteps().get(stepIndex));
        }
    }


    @FXML
    void onPreviousButtonClicked(MouseEvent event)
    {
        stepIndex--;

        if(stepIndex>0)
        {
            fillDirectionsList(guidance.getSteps().get(stepIndex));
            onStepChanged(guidance.getSteps().get(stepIndex));
        }
    }


    //Test comment, please delete

    
    @FXML
    void onSendEmail(ActionEvent event)
    {
        Runnable sendEmail = () -> {
            if(guidance!=null)
            {
                if (emailField.getText().contains("@")) {
                    System.out.println("Sending to an email");
                    guidance.sendEmailGuidance(emailField.getText());
                } else if (emailField.getText().toLowerCase().equals(emailField.getText().toUpperCase())) {
                    System.out.println("sending to a phone");
                    guidance.sendTextGuidance(emailField.getText());
                }
            }
        };

        new Thread(sendEmail).start();
    }
}