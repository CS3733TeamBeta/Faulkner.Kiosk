package Domain.ViewElements;

import Domain.Map.MapNode;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.HashSet;

/**
 * Created by benhylak on 1/30/17.
 */
public class GraphicalMapNode extends DragIcon implements DrawableMapEntity
{
    MapNode mapNode = null;
    HashSet<GraphicalNodeEdge> edges;

    public GraphicalMapNode()
    {
        super();
    }

    public GraphicalMapNode(MapNode n)
    {
        super();
        mapNode = n;
    }

    @Override
    public Node getDrawableNode()
    {
        return null;
    }
}
