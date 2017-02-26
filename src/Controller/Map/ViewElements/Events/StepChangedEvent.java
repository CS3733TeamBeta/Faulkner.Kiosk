package Controller.Map.ViewElements.Events;

import Entity.Navigation.DirectionFloorStep;

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
