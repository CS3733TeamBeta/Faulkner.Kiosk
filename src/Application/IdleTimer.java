package Application;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Stage;

/**
 * Created by benhylak on 2/27/17.
 */
public class IdleTimer
{
    Stage stageUnderMonitor = null;
    EventHandler<Event> inactivityHandler;
    EventType filterType = Event.ANY;

    public IdleTimer()
    {
        inactivityHandler = event ->
        {
            System.out.println("Activity detected");
        };
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
