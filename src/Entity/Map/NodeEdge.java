package Entity.Map;

import Controller.Map.ViewElements.DragIcon;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

/**
 * An edge that connects two nodes and has a cost (edge length)
 */
public class NodeEdge
{
    protected double cost;

    protected MapNode source = null;
    protected MapNode target = null;

    /**
     * Creates a new nodeEdge with a line having a default strokewidth of 5.
     */
    public NodeEdge() {
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
    }

    /**
     * Set target node and update start point
     * @param target
     */
    public void setTarget(MapNode target) {
        this.target = target;
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
     * Updates the cost of this NodeEdge to be calculated using the distance between the two MapNodes in this NodeEdge
     */
    public void updateCost() {
        this.cost = Math.pow(Math.pow(source.getPosX() - target.getPosX(), 2) + Math.pow(source.getPosY() - target.getPosY(), 2), .5);
    }

}
