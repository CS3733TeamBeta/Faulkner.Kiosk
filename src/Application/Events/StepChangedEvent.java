package Application.Events;

<<<<<<< HEAD:src/Controller/Map/ViewElements/Events/StepChangedEvent.java
import Entity.Navigation.DirectionFloorStep;
=======
import Map.Navigation.DirectionStep;
>>>>>>> ed08e8acc4874f1f2ac5a418bf8ddd41dc2685f1:src/Application/Events/StepChangedEvent.java

/**
 * Created by benjaminhylak on 2/9/17.
 */
public class StepChangedEvent
{
    protected DirectionFloorStep source;

    public StepChangedEvent(DirectionFloorStep source)
    {
        this.source = source;
    }

    public DirectionFloorStep getSource()
    {
        return source;
    }
}
