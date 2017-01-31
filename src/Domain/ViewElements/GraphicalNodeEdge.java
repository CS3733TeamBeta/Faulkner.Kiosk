package Domain.ViewElements;

import Domain.Map.Destination;
import Domain.Map.NodeEdge;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import jfxtras.labs.util.event.MouseControlUtil;

/**
 * Test class for a drawable Node edge. Probably will change this in the future.
 *
 * @TODO Make this code not janky
 */
public class GraphicalNodeEdge extends NodeEdge implements DrawableMapEntity
{
    Line l;

    boolean displayNodes = false;

    Group edgeWithNodes;

    public GraphicalNodeEdge()
    {
        l = new Line(50, 50, 150, 150);
        l.setStrokeWidth(10);


        Group g = new Group();
        g.getChildren().add(l);

        edgeWithNodes = g;
    }

    @Override
    public Node getDrawableNode()
    {
        return edgeWithNodes;
    }
}
