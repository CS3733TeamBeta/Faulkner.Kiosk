package Domain.ViewElements;

import Domain.Map.MapNode;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.HashSet;

/**
 * Created by benhylak on 1/30/17.
 */
public class GraphicalMapNode extends DragIcon
{
    MapNode mapNode = null;
    HashSet<GraphicalNodeEdge> edges;

    final double NODE_HOVER_OPACITY = .65;
    final double NODE__NORMAL_OPACITY = 1;

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

    /**
     * If the node, is being hovered on during map building, slightly change opacity
     * to indicate it can be dropped on
     */
    public void changeToHoverOpacity()
    {
        setOpacity(NODE_HOVER_OPACITY);
    }

    /**
     * On mouse exit, change opacity back to solid
     */
    public void changeToNormalOpacity()
    {
        setOpacity(NODE__NORMAL_OPACITY);
    }
}
