package Domain.ViewElements;

import Domain.Map.MapNode;
import javafx.scene.Node;

/**
 * Created by benhylak on 1/30/17.
 */
public class GraphicalMapNode extends MapNode implements DrawableMapEntity
{
    public GraphicalMapNode(MapNode n)
    {

    }

    @Override
    public Node getDrawableNode()
    {
        return null;
    }
}
