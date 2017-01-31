package Domain.ViewElements;

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
public class GraphicalNodeEdge extends NodeEdge
{
    Line l;
    Circle p1;
    Circle p2;

    Group edgeWithNodes;

    public GraphicalNodeEdge()
    {
        l = new Line(50, 50, 150, 150);
        l.setStrokeWidth(10);

        p1 = new Circle(50, 50, 10);
        p2 = new Circle(150, 150, 10);

        Group g = new Group();
        g.getChildren().add(l);
        g.getChildren().add(p1);
        g.getChildren().add(p2);

        MouseControlUtil.makeDraggable(g,
                event -> {
                   if(event.getTarget() == p2)
                   {
                       p2.setCenterX(event.getX());
                       p2.setCenterY(event.getY());

                       l.setEndX(event.getX());
                       l.setEndY(event.getY());

                   }
                },

                event ->
                {
                 //   p1.setTranslateX(even);
                    //p1.setCenterY(l.getTranslateY());
                });


        edgeWithNodes = g;
    }

    public Node getViewNode()
    {
        return edgeWithNodes;
    }
}
