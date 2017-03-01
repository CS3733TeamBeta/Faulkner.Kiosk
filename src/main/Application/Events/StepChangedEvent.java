package main.Application.Events;

import main.Map.Navigation.DirectionFloorStep;

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
