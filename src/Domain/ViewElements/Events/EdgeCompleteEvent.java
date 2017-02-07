package Domain.ViewElements.Events;

import Domain.ViewElements.GraphicalNodeEdge;

/**
 * Created by benhylak on 2/4/17.
 */
public class EdgeCompleteEvent
{
    GraphicalNodeEdge edge;

    public EdgeCompleteEvent(GraphicalNodeEdge edge)
    {
        this.edge =edge;
    }

    public GraphicalNodeEdge getNodeEdge()
    {
        return edge;
    }
}
