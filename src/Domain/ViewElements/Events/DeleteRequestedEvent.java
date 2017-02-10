package Domain.ViewElements.Events;

import Domain.Map.MapNode;

/**
 * Created by benjaminhylak on 2/9/17.
 */
public class DeleteRequestedEvent
{
    protected MapNode source;

    public DeleteRequestedEvent(MapNode source)
    {
        this.source = source;
    }

    public MapNode getSource()
    {
        return source;
    }
}
