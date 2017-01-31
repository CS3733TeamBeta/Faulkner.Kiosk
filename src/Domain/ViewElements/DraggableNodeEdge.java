package Domain.ViewElements;

import javafx.scene.shape.Circle;
import jfxtras.labs.util.event.MouseControlUtil;

/**
 * Created by benhylak on 1/30/17.
 */
public class DraggableNodeEdge extends GraphicalNodeEdge
{
    boolean displayNodes = true;

    public DraggableNodeEdge()
    {
        super();

        Circle p1 = new Circle(50, 50, 10);
        Circle p2 = new Circle(150, 150, 10);

        edgeWithNodes.getChildren().addAll(p1, p2);

        MouseControlUtil.makeDraggable(edgeWithNodes,
                event ->
                {
                    if (event.getTarget() == p2)
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
    }
}
