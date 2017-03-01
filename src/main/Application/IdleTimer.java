package main.Application;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by benhylak on 2/27/17.
 */
public class IdleTimer
{
    Stage stageUnderMonitor = null;
    EventHandler<Event> inactivityHandler;
    EventType filterType = Event.ANY;
    Timeline inactivityWait;
    Timeline userResponseWait;
    Alert userPrompt;

    TimerState state;

    enum TimerState
    {
        active,
        pending_response,
        inactive,
        stopped
    }

    public IdleTimer()
    {
        inactivityHandler = event ->
        {
            if(state != TimerState.stopped)
            {
                state = TimerState.active;
                resetInactivity();
            }
        };

        userResponseWait = new Timeline(new KeyFrame(Duration.seconds(10), e ->{
            try
            {
                userPrompt.close();
                ApplicationController.getController().switchToUserMapView();
                System.out.println("User response time out.");
                state = TimerState.inactive;
            } catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }));
    }

    public void initTimer()
    {
        state = TimerState.active;

        inactivityWait  = new Timeline(new KeyFrame(Duration.seconds(ApplicationController.getController()
                .getTimeout()), event ->
        {
            try
            {
                if(state == TimerState.active)
                {
                    Platform.runLater(() ->
                    {
                        System.out.println("User inactivity detected... Prompt deployed");
                        userPrompt = new Alert(Alert.AlertType.CONFIRMATION);
                        userPrompt.setTitle("Are you still there?");
                        userPrompt.setHeaderText("Your session is about to expire.");

                        userPrompt.setContentText("Would you like to end your session? Please respond within a minute.");

                        userResponseWait.playFromStart();

                        Optional<ButtonType> result = userPrompt.showAndWait();

                        state = TimerState.pending_response;

                        if (result.get() == ButtonType.CANCEL)
                        {
                            resetInactivity();
                            userResponseWait.stop();
                        }
                        else
                        {
                            userResponseWait.playFrom(Duration.seconds(10));
                        }
                    });
                }
                else if(state == TimerState.pending_response)
                {

                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }));

    }
    public void updateTimeout()
    {
        /*timer.schedule(ApplicationController.getController().getTimeout(),
                );   */// notify applicaiton controller
    }

    public void start()
    {
        state = TimerState.active;
        inactivityWait.playFromStart();
    }

    public void stop()
    {
        state = TimerState.stopped;
        inactivityWait.stop();
    }

    public void resetInactivity()
    {
        inactivityWait.playFromStart();
    }

    public void setStageToMonitor(Stage stage)
    {
        if(stageUnderMonitor!=null)
        {
            stageUnderMonitor.removeEventFilter(Event.ANY, inactivityHandler);
        }

        stageUnderMonitor = stage;

        stage.addEventFilter(Event.ANY,
                inactivityHandler);
    }
}
