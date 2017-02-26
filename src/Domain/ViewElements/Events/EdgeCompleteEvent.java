package Domain.ViewElements.Events;

import Domain.Map.NodeEdge;
import javafx.scene.shape.Line;

/**
 * Created by benhylak on 2/4/17.
 */
public class EdgeCompleteEvent
{
    NodeEdge edge;

    public EdgeCompleteEvent(NodeEdge edge)
    {
        this.edge =edge;
    }

    public NodeEdge getNodeEdge()
    {
        return edge;
    }
}
