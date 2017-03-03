package main.Map.Controller;


import com.jfoenix.controls.JFXListCell;
import javafx.event.EventType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import main.Application.Exceptions.PathFindingException;
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
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;

public class UserDirectionsPanel extends AnchorPane
{

    Guidance guidance;
    int stepIndex = 0;
    int followIndex = -1;
    ImageView MapImage;
    ArrayList<StepChangedEventHandler> stepChangedEventHandlers;

    DirectionFloorStep currentStep;

    @FXML
    Text startName;

    @FXML
    Text endName;

    @FXML
    Label floorLabel;

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
    private JFXListView<String> directionsListView;

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

    @FXML
    private AnchorPane startIcon;

    @FXML
    private AnchorPane endIcon;

    public void setCloseHandler(EventHandler<? super MouseEvent> e)
    {
        closeButton.setOnMouseClicked(e);
    }


    @FXML
    public void initialize()
    {
        directionsListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                final ListCell cell = new JFXListCell() {
                    private Text text;

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!isEmpty()) {
                            text = new Text(item.toString());
                            text.setWrappingWidth(directionsListView.getPrefWidth());
                            setGraphic(text);
                        }
                    }
                };

                return cell;
            }
        });
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
        startIcon.getChildren().clear();
        endIcon.getChildren().clear();;

        DragIcon start = new DragIcon();
        start.setType(g.getPathNodes().getFirst().getType());

        startName.setText(g.getPathNodes().getFirst().toString());

        DragIcon end = new DragIcon();
        end.setType(g.getPathNodes().getLast().getType());

        endName.setText(g.getPathNodes().getLast().toString());

        startIcon.getChildren().add(start);
        endIcon.getChildren().add(end);

        startIcon.setTranslateY(8);
        startIcon.setTranslateX(8);

        endIcon.setTranslateY(8);
        endIcon.setTranslateX(4);

        this.guidance = g;
        stepIndex = 0;
        followIndex = -1;


        fillDirectionsList(stepIndex);

        currentStep = g.getFloorSteps().getFirst();
    }

    public void fillDirectionsList(int index)
    {
        fillDirectionsList(guidance.getSteps().get(index));
    }

    public void fillDirectionsList(DirectionFloorStep step)
    {
        directionsListView.getItems().clear();

        for (DirectionStep aDirectionStep: step.getDirectionSteps())
        {
            directionsListView.getItems().add(aDirectionStep.toString());
        }
    }

    @FXML
    void onCloseButtonClicked(MouseEvent event)
    {

    }

    @FXML
    public void onNextButtonClicked()
    {
        int nextIndex = guidance.getFloorSteps().indexOf(currentStep) +1;

        if(nextIndex<guidance.getFloorSteps().size())
        {
            DirectionFloorStep nextStep =guidance.getFloorSteps().get(nextIndex);
            setFloorStep(nextStep);
            notifyStepChanged(new StepChangedEvent(nextStep));
        }
    }

    public void notifyStepChanged(StepChangedEvent e)
    {
        for(StepChangedEventHandler h: stepChangedEventHandlers)
        {
            h.handle(e);
        }
    }
    public void setFloorStep(DirectionFloorStep floorStep)
    {
        currentStep = floorStep;
        floorLabel.setText("Floor " + floorStep.getFloor().getFloorNumber());
        fillDirectionsList(currentStep);
    }

    @FXML
    void onPreviousButtonClicked()
    {
        int prevIndex = guidance.getFloorSteps().indexOf(currentStep)-1;

        if(prevIndex>-1)
        {
            DirectionFloorStep prevStep =guidance.getFloorSteps().get(prevIndex);
            setFloorStep(prevStep);
            notifyStepChanged(new StepChangedEvent(prevStep));
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

    public int getFollowIndex () {
        return this.followIndex;
    }

    public ArrayList<StepChangedEventHandler> getStepChangedEventHandlers () {
        return stepChangedEventHandlers;
    }
}