package Domain.ViewElements;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A map node used during the graphical map building process
 */
public class GraphicalMapNode extends MapNode implements DrawableMapEntity
{
    protected DragIcon icon;

    final double NODE_HOVER_OPACITY = .65;
    final double NODE__NORMAL_OPACITY = 1;

    public GraphicalMapNode()
    {
        super();

        icon = new DragIcon();

      //  fxNodetoGraphicalMap = new HashMap<Node, GraphicalMapNode>();
    }

    public void addEdge(GraphicalNodeEdge edge)
    {
       this.edges.add(edge);
    }

    public HashSet<NodeEdge> getGraphicalEdges()
    {
        return edges;
    }

    /**
     * If the node, is being hovered on during map building, slightly change opacity
     * to indicate it can be dropped on
     */
    public void changeToHoverOpacity()
    {
        icon.setOpacity(NODE_HOVER_OPACITY);
    }

    /**
     * On mouse exit, change opacity back to solid
     */
    public void changeToNormalOpacity()
    {
        icon.setOpacity(NODE__NORMAL_OPACITY);
    }

    /**
     *
     * @return Visual (JavaFX) node that represents this object
     */
    @Override
    public Node getNodeToDisplay()
    {
        return icon;
    }



    /**
     * Set the type of the underlying drag icon
     * @param type (Doctor, bathroom, etc.)
     */
    public void setType (DragIconType type) {
        icon.setType(type);
    }

    /**
     * Get the icon type of the underlying DragIcon
     *
     * @return Drag Icon type
     */
    public DragIconType getIconType()
    {
        return icon.getType();
    }
}
