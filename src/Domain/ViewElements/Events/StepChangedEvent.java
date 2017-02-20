package Domain.ViewElements.Events;

import Domain.Map.MapNode;
import Domain.Navigation.DirectionStep;

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
