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
        edges = new HashSet<GraphicalNodeEdge>();
    }

    public GraphicalMapNode(MapNode n)
    {
        super();
        mapNode = n;
    }

    public void addEdge(GraphicalNodeEdge edge)
    {
       this.edges.add(edge);
    }

    public HashSet<GraphicalNodeEdge> getEdges()
    {
        return edges;
    }

    @Override
    public Node getDrawableNode()
    {
        return null;
    }
}
