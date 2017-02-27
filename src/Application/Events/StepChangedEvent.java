package Application.Events;

import Map.Navigation.DirectionStep;

/**
 * Created by benjaminhylak on 2/9/17.
 */
public class StepChangedEvent
{
    protected DirectionStep source;

    public StepChangedEvent(DirectionStep source)
    {
        this.source = source;
    }

    public DirectionStep getSource()
    {
        return source;
    }
}
