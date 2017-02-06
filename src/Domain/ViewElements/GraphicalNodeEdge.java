
package Domain.ViewElements;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.UUID;


/**
 * Created by benhylak on 2/3/17.
 */
public class GraphicalNodeEdge extends MapNode implements DrawableMapEntity
{
    Line edgeLine;

    protected GraphicalMapNode source = null;
    protected GraphicalMapNode target = null;

    public GraphicalNodeEdge()
    {
        edgeLine = new Line();
    }

    public GraphicalNodeEdge(GraphicalMapNode source, GraphicalMapNode target)
    {
       this.source = source;
       this.target = target;
       // node_link.setStart(a.getLocation);
    }

    @FXML
    private void initialize() {

    }

    /**
     *
     * @return source of edge
     */
    public GraphicalMapNode getSource()
    {
        return source;
    }

    /**
     *
     * @return target of edge
     */
    public GraphicalMapNode getTarget()
    {
        return target;
    }

    /**
     * Set source node and update start/end point
     * @param source
     */
    public void setSource(GraphicalMapNode source)
    {
        this.source = source;

        DragIcon drawableNode = (DragIcon)source.getNodeToDisplay();

        Bounds boundsInScene = drawableNode.getBoundsInLocal();

        Point2D startPoint = new Point2D(
                boundsInScene.getMinX() + (boundsInScene.getWidth() / 2),
                boundsInScene.getMinY() + (boundsInScene.getHeight() / 2)
        );

        setStart(drawableNode.localToParent(startPoint));
        setEnd(drawableNode.localToParent(startPoint));
    }

    /**
     * Set target node and update start point
     * @param target
     */
    public void setTarget(GraphicalMapNode target)
    {
        this.target = target;

        DragIcon dragIcon = (DragIcon) target.getNodeToDisplay();

        Bounds boundsInScene = dragIcon.getBoundsInLocal();

        Point2D endPoint = new Point2D(
                boundsInScene.getMinX() + (boundsInScene.getWidth() / 2),
                boundsInScene.getMinY() + (boundsInScene.getHeight() / 2)
        );

        setEnd(dragIcon.localToParent(endPoint));
    }

    /**
     * Set start point
     * @param startPoint
     */
    public void setStart(Point2D startPoint) {

        edgeLine.setStartX(startPoint.getX());
        edgeLine.setStartY(startPoint.getY());
    }

    //Set end point
    public void setEnd(Point2D endPoint) {

        edgeLine.setEndX(endPoint.getX());
        edgeLine.setEndY(endPoint.getY());
    }

    public Line getEdgeLine()
    {
        return edgeLine;
    }

    public void updatePosViaNode(GraphicalMapNode node)
    {
        Node drawableNode = node.getNodeToDisplay();

        Point2D newPoint = new Point2D(drawableNode.getLayoutX() + drawableNode.getBoundsInLocal().getWidth() / 2,
                drawableNode.getLayoutY() + drawableNode.getBoundsInLocal().getHeight() / 2);

        if (node == target) {
            setEnd(newPoint);
        }
        else
        {
            setStart(newPoint);
        }

    }

    @Override
    public Node getNodeToDisplay()
    {
        return edgeLine;
    }

    public void toBack()
    {
        edgeLine.toBack();;
    }
}
