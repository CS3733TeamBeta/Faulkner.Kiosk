package Domain.Map;

import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DrawableMapEntity;
import Domain.ViewElements.GraphicalMapNode;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.rmi.server.UID;

/**
 * An edge that connects two nodes and has a cost (edge length)
 */
public class NodeEdge implements DrawableMapEntity
{
    protected double cost;

    protected MapNode source = null;
    protected MapNode target = null;

    Line edgeLine;

    public NodeEdge() {
        edgeLine = new Line();
        edgeLine.setStrokeWidth(5);
    }

    public NodeEdge(MapNode source, MapNode target) {
        this();

        setSource(source);
        setTarget(target);

        source.addEdge(this);
        target.addEdge(this);
    }

    public NodeEdge(MapNode source, MapNode nodeB, double cost) {
        this(source, nodeB);
        this.cost = cost;
    }

    /**
     * Set source node and update start/end point
     * @param source
     */
    public void setSource(MapNode source)
    {
        this.source = source;

        DragIcon drawableNode = (DragIcon)source.getNodeToDisplay();

        Point2D startPoint = getNodeCenterPoint(drawableNode);

        setStartPoint(drawableNode.localToParent(startPoint));
        setEndPoint(drawableNode.localToParent(startPoint));
    }

    /**
     * Set target node and update start point
     * @param target
     */
    public void setTarget(MapNode target)
    {
        this.target = target;

        DragIcon drawableNode = (DragIcon) target.getNodeToDisplay();

        Point2D endPoint = getNodeCenterPoint(drawableNode);

        setEndPoint(drawableNode.localToParent(endPoint));
    }

    /**
     * Set start point
     * @param startPoint
     */
    public void setStartPoint(Point2D startPoint) {

        edgeLine.setStartX(startPoint.getX());
        edgeLine.setStartY(startPoint.getY());
    }

    //Set end point
    public void setEndPoint(Point2D endPoint) {

        edgeLine.setEndX(endPoint.getX());
        edgeLine.setEndY(endPoint.getY());
    }

    public Line getEdgeLine()
    {
        return edgeLine;
    }

    public void updatePosViaNode(MapNode node)
    {
        Node drawableNode = node.getNodeToDisplay();

        Point2D newPoint = new Point2D(drawableNode.getLayoutX() + drawableNode.getBoundsInLocal().getWidth() / 2,
                drawableNode.getLayoutY() + drawableNode.getBoundsInLocal().getHeight() / 2);

        if (node == target) {
            setEndPoint(newPoint);
        }
        else
        {
            setStartPoint(newPoint);
        }
    }

    @Override
    public Node getNodeToDisplay()
    {
        return edgeLine;
    }

    public void toBack()
    {
        edgeLine.toBack();
    }

    public double getCost() {
        return cost;
    }

    public MapNode getSource() {
        return source;
    }
    public MapNode getTarget() {
        return target;
    }

    //Returns the node connected to this edge that isn't passed in
    public MapNode getOtherNode(MapNode n)
    {
        if(source.equals(n))
        {
            return target;
        }
        else return source;
    }

    public Point2D getNodeCenterPoint(DragIcon dragIcon)
    {
        Bounds boundsInScene = dragIcon.getBoundsInLocal();

        Point2D centerPoint = new Point2D(
                boundsInScene.getMinX() + (boundsInScene.getWidth() / 2),
                boundsInScene.getMinY() + (boundsInScene.getHeight() / 2)
        );

        return centerPoint;
    }

    public void changeColor(Color c)
    {
        edgeLine.setStroke(c);
    }

    public void changeOpacity(Double opacity)
    {
        if(opacity <= 1 && opacity >= 0) {
            edgeLine.setOpacity(opacity);
        }
    }

    public void updateCost() {
        this.cost = Math.pow(source.getPosX() - target.getPosX(), 2) + Math.pow(source.getPosY() - target.getPosY(), 2);
    }

}
