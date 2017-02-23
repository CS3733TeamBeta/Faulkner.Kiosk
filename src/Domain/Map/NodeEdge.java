package Domain.Map;

import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DrawableMapEntity;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * An edge that connects two nodes and has a cost (edge length)
 */
public class NodeEdge implements DrawableMapEntity
{
    protected double cost;

    protected MapNode source = null;
    protected MapNode target = null;

    Line edgeLine;

    /**
     * Creates a new nodeEdge with a line having a default strokewidth of 5.
     */
    public NodeEdge() {
        edgeLine = new Line();
        edgeLine.setStrokeWidth(5);
    }

    /**
     * Creates a new nodeEdge with a line having a default strokewidth of 5, the given source MapNode and target MapNode, and adds this
     * new edge to the source and target
     * @param source
     * @param target
     */
    public NodeEdge(MapNode source, MapNode target) {
        this();

        setSource(source);
        setTarget(target);

        source.addEdge(this);
        target.addEdge(this);
    }

    /**
     * Creates a new nodeEdge with a line having a default strokewidth of 5, the given source MapNode and target MapNode, and adds this
     * new edge to the source and target. Has a cost of traveling this equal to the given cost.
     * @param source
     * @param nodeB
     * @param cost
     */
    public NodeEdge(MapNode source, MapNode nodeB, double cost) {
        this(source, nodeB);
        this.cost = cost;
    }

    /**
     * Set source node and update start/end point
     * @param source
     */
    public void setSource(MapNode source) {
        this.source = source;

        DragIcon drawableNode = (DragIcon)source.getNodeToDisplay();

        Point2D startPoint = getNodeCenterPoint(drawableNode);

        setStartPoint(drawableNode.localToParent(startPoint));

        if(getEdgeLine().getEndX() == 0 && getEdgeLine().getEndY()==0) //if currently, end point is empty
        {
            setEndPoint(drawableNode.localToParent(startPoint));
        }
    }

    /**
     * Set target node and update start point
     * @param target
     */
    public void setTarget(MapNode target) {
        this.target = target;

        DragIcon drawableNode = (DragIcon) target.getNodeToDisplay();

        Point2D endPoint = getNodeCenterPoint(drawableNode);

        setEndPoint(drawableNode.localToParent(endPoint));
    }

    /**
     * Sets the end point of this nodeEdge to the given Point2D point.
     * @param startPoint
     */
    public void setStartPoint(Point2D startPoint) {
        edgeLine.setStartX(startPoint.getX());
        edgeLine.setStartY(startPoint.getY());
    }

    /**
     * Sets the end point of this nodeEdge to the given Point2D point.
     * @param endPoint
     */
    public void setEndPoint(Point2D endPoint) {
        edgeLine.setEndX(endPoint.getX());
        edgeLine.setEndY(endPoint.getY());
    }

    /**
     * Returns the line in this nodeEdge
     * @return this nodeEdge's line
     */
    public Line getEdgeLine() {
        return edgeLine;
    }

    /**
     * Updates the position of this NodeEdge. This is called if the MapNode that this NodeEdge connects to is moved
     * or deleted.
     * @param node
     */
    public void updatePosViaNode(MapNode node) {
        Node drawableNode = node.getNodeToDisplay();

        //TODO: WE REALLY NEED TO MOVE THESE CONSTANTS SOMEWHERE
        //TODO: These exist because we autoscale all the png files to be 30x30 when displaying them.
        //TODO: We either need to store the scaled pixel measurements somewhere and pull them here or move all instances of 30 to a constant somewhere
        double offsetX = 12.5;
        double offsetY = 12.5;

        Point2D newPoint = new Point2D (drawableNode.getLayoutX() + offsetX ,
                                        drawableNode.getLayoutY() + offsetY );

        if (node == target) {
            setEndPoint(newPoint);
        } else {
            setStartPoint(newPoint);
        }
    }

    /**
     * Returns the line within this NodeEdge.
     * @return this NodeEdge's line
     */
    @Override
    public Node getNodeToDisplay() {
        return edgeLine;
    }

    /**
     * Moves this NodeEdge's line to the back of the pane, behind other  objects.
     */
    public void toBack() {
        edgeLine.toBack();
    }


    /**
     * Moves this NodeEdge's line to the front of the pane, in front of other objects
     */
    public void toFront() {
        edgeLine.toFront();
    }

    /**
     * Returns the cost of this NodeEdge
     * @return this NodeEdge's cost, as a double.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Retreives this NodeEdge's source
     * @return this NodeEdge's source as a MapNode
     */
    public MapNode getSource() {
        return source;
    }

    /**
     * Retrieves this NodeEdge's target
     * @return this NodeEdge's source as a MapNode
     */
    public MapNode getTarget() {
        return target;
    }

    /**
     * Retrieves the MapNode connected to this NodeEdge that isn't the MapNode passed in
     * @param n the MapNode not desired
     * @return the MapNode at the other side of this NodeEdge
     */
    public MapNode getOtherNode(MapNode n)
    {
        if(source.equals(n)) {
            return target;
        } else if (target.equals(n)) {
            return source;
        } else {
            System.out.println("Something's gone wrong. You've called getOtherNode on a NodeEdge with a node that it doesn't connect to.");
            //@TODO Throw an exception here.
            return source;
        }
    }

    /**
     * Calculates and returns the center point of a given DragIcon
     * @param dragIcon
     * @return the center point of the DragIcon as a Point2D
     */
    public Point2D getNodeCenterPoint(DragIcon dragIcon) {
        Bounds boundsInScene = dragIcon.getBoundsInLocal();

        Point2D centerPoint = new Point2D(
                boundsInScene.getMinX() + (boundsInScene.getWidth() / 2),
                boundsInScene.getMinY() + (boundsInScene.getHeight() / 2)
        );

        return centerPoint;
    }

    /**
     * Changes the color of this NodeEdge's line to the given color
     * @param c The desired color
     */
    public void changeColor(Color c) {
        edgeLine.setStroke(c);
    }

    /**
     * Changes the opacity of this NodeEdge's line to the given opacity. Opacity must be between 0 and 1
     * @param opacity
     */
    public void changeOpacity(Double opacity) {
        if(opacity <= 1 && opacity >= 0) {
            edgeLine.setOpacity(opacity);
        } else {
            System.out.println("Opacity not between 0 and 1!");
            //@TODO Throw an exception here.
        }
    }

    /**
     * Updates the cost of this NodeEdge to be calculated using the distance between the two MapNodes in this NodeEdge
     */
    public void updateCost() {
        this.cost = Math.pow(Math.pow(source.getPosX() - target.getPosX(), 2) + Math.pow(source.getPosY() - target.getPosY(), 2), .5);
    }

}
