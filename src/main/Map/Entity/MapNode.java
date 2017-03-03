package main.Map.Entity;

import main.Application.Events.DeleteRequestedEvent;
import main.Application.Events.DeleteRequestedHandler;
import main.Application.popover.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.util.*;

/**
 * Represents a node in a Map, connected to other nodes by NodeEdges
 */

public class MapNode extends Observable
{
    double posX;
    double posY;

    private final String popOverEditFXML = "/Popover/NodeEditPopup.fxml";

    protected ArrayList<DeleteRequestedHandler> deleteEventHandlers = null;

    UUID nodeUID;

    String label = "";
    String destName = "";
    NodeType type;

    public static MapNode nodeFactory(NodeType type, Point2D location)
    {
        MapNode n = null;

        switch(type)
        {
            case Department:
            case Food:
            case Info:
            case Restroom:
            case Store:
            {
                n = new Destination(type.toString()); //needed for lambda
                break;
            }
            case Kiosk:
            {
                n = new Kiosk();

                break;
            }
            case Elevator:
            {
                n= new MapNode();
                n.setIsElevator(true);
            }
            default:
            {
                n= new MapNode(); //needed for lambda

                break;
            }
        }

        n.setType(type);
        n.setPosX(location.getX());
        n.setPosY(location.getY());

        return n;
    }

    public static enum NodeUpdateType
    {
        NameUpdate
    }

    /**
     * G value of this node, used for pathfinding, defaults to 0
     */
    double g = 0;
    /**
     * Heuristic value of this node, used for pathfinding, defaults to max
     */
    double heuristic = Double.MAX_VALUE;
    /**
     * F  value of this node, used for pathfinding, defaults to max
     */
    double f = Double.MAX_VALUE;
    /**
     * Parent of this node, used for pathfinding, defaults to null
     */

    NodeEdge parent = null;

    Image node = null;
    Floor myFloor;

    public HashSet<NodeEdge> edges;

    boolean isElevator = false;
    /**
     * Creates a new MapNode, with no edges, a new UID, and a new Icon
     */
    public MapNode() {
        this(UUID.randomUUID());
    }

    /**
     * Creates a new MapNode with no edges, a new UID, a new Icon, and the given nodeID
     * @param nodeID
     */
    public MapNode(UUID nodeID) {
        this(nodeID,0, 0, 0);
    }

    /**
     * Creates a new MapNode with no edges, a new UID, a new Icon, the given NodeID, posX, and posY. defaults to a toilet.
     * @param //nodeID
     * @param posX
     * @param posY
     */
    public MapNode(double posX, double posY) {
        this(posX, posY, 0);
    }

    /*
    public void setLabel(String name) {
        destName = name;
    }
    */

    public String getLabel()
    {
        return "";
    }

    /**
     * Creates a new MapNode with no edges, a new UID, a new ICON, the given NodeID, posX, posY, and Type
     * @param //nodeID
     * @param posX
     * @param posY
     * @param type
     */
    public MapNode(double posX, double posY, int type)
    {
        this(UUID.randomUUID(), posX, posY, type);
    }

    public MapNode(UUID nodeUID, double posX, double posY, int type)
    {
        this.nodeUID = nodeUID;

        this.posX = posX;
        this.posY = posY;

        this.edges = new HashSet<NodeEdge>();

        this.setType(NodeType.values()[type]);
    }

    public void setPos(double posX, double posY)
    {
        setPosX(posX);
        setPosY(posY);
    }

    /**
     * Sets whether or not this map node is an elevator
     *@param isElevator true if node is elevator, false otherwise
     */
    public void setIsElevator(boolean isElevator)
    {
        this.isElevator =isElevator;

        if(isElevator)
        {
            label = "Elevator";
            this.setType(NodeType.Elevator);
        }
    }

    /**
     *
     * @return whether or not this node is an elevator
     */
    public boolean getIsElevator()
    {
        return this.isElevator;
    }

    /**
     * Sets the X position of this node to be the given position
     * @param posX The desired position, as a double
     */
    public void setPosX(double posX) {

        this.posX = posX;

       // icon.parentToLocal(icon.getParent().localToScene(posX,posY)
    }

    /**
     * Sets the Y position of this node to be the given position
     * @param posY The desired position, as a double
     */
    public void setPosY(double posY) {

        this.posY = posY;
    }

    /**
     * Retrieves the x position of this MapNode as a double
     * @return X Position in Scene Coordinates
     */
    public double getPosX() {
        return posX;
    }

    /**
     * Retrieves the Y position of this MapNode as a double
     * @return Y Position in Scene Coordinates
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Sets the floor of this MapNode to the given Floor
     * @param f the desired floor
     */
    public void setFloor(Floor f) {
        this.myFloor = f;
    }

    /**
     * Returns the edge to this MapNode's parent
     * @return the edge to this MapNode's parent
     */
    public NodeEdge getParent() {
        return parent;
    }

    /**
     * Retrieves the set of edges that connect to this MapNode
     * @return the set of edges as a Collection<NodeEdge></NodeEdge>
     */
    public Collection<NodeEdge> getEdges() {
        return edges;
    }

    public NodeEdge getEdgeTo(MapNode n) {
        for (NodeEdge e: this.edges) {
            if (e.getOtherNode(this).equals(n)) {
                return e;
            }
        }
        return null;
        //@TODO Throw exception
    }


    /**
     * Determines if this node has a node to another node.
     * @param n Other node
     * @return true if this node has a node to another node, false otherwise.
     */
    public boolean hasEdgeTo(MapNode n) {
        for (NodeEdge e: edges) {
            if (e.getOtherNode(this).equals(n)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets this MapNode's parent to the given NodeEdge
     * @param parent The desired parent as a NodeEdge
     */
    public void setParent(NodeEdge parent) {
        this.parent = parent;
    }

    /**
     * Retrieves this MapNode's floor
     * @return this MapNode's floor as a floor
     */
    public Floor getMyFloor() {
        return myFloor;
    }

    /**
     * Retrieves this MapNode's NodeID, which is simply a human-readable identifier. Do not confuse with NodeUID
     * @return this MapNode's NodeID
     */
    /**
     * Retrieves this MapNodes nodeUID, which is a unique ID that identifies this MapNode. Do not confuse with NodeID
     * @return this MapNode's  nodeUID
     */
    public UUID getNodeID() {
        return this.nodeUID;
    }

    /**
     * Add an edge to this mapnode
     * @param e edge to add
     */
    public void addEdge(NodeEdge e) {
        this.edges.add(e);
    }

    /**
     * Returns true if the obj has the same NodeUID as this.
     * @param obj
     * @return true if objects are equal
     */
    @Override
     public boolean equals(Object obj)
    {
        if (obj instanceof MapNode) {
           return this.equals((MapNode) obj);
        } else {
            return false;
        }
    }

    /**
     * Returns true if aNode the same NodeUID as this.
     * @param aNode
     * @return true if objects are equal
     */
    public boolean equals(MapNode aNode) {
        return (this.nodeUID.equals(aNode.getNodeID()));
    }

    /*****************************GRAPHICAL FUNCTIONS**********************/
    /**
     * Set the type of the underlying drag icon
     * @param type (Doctor, bathroom, etc.)
     */
    public void setType (NodeType type) {
        this.type = type;
        label = type.name();
    }

    /**
     * When handlers subscribe, notifies them that this mapnode should be deleted
     */
    public void deleteFromMap() {
        raiseDeleteRequested();
    }

    /**
     * @TODO Make Javadoc for this
     */
    protected void raiseDeleteRequested() {
        if(deleteEventHandlers!=null) {
            for (DeleteRequestedHandler handler : deleteEventHandlers) {
                handler.handle(new DeleteRequestedEvent(this));
            }
        }
        else {
            System.out.println("You've requested to delete a node, but the node isn't set up to do so. (Hint: Add a DeleteRequestedHandler)");
        }
    }

    /**
     * @TODO Make Javadoc for this
     */
    public void setOnDeleteRequested(DeleteRequestedHandler handler) {
        if(deleteEventHandlers==null) {
            deleteEventHandlers = new ArrayList<>();
        }
        deleteEventHandlers.add(handler);
    }

    /**
     * @TODO Make Javadoc for this
     */
    /**Returns a pop over window to edit this node**/
    public PopOver getEditPopover()
    {
        NodeEditController controller = new NodeEditController(this);

        return getPopOver(controller, popOverEditFXML);
    }

    /**
     * @TODO Make Javadoc for this
     */
    protected final PopOver getPopOver(AbstractPopupController controller, String fxmlPath)
    {
        PopOver popOver = new PopOver();

        FXMLLoader loader = new FXMLLoader(Destination.class.getResource(fxmlPath));
        controller.setPopOver(popOver); //sets the popover used by the controller

        loader.setController(controller);

        try {
            popOver.setContentNode(loader.load());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return popOver;
    }

    /*******************************A STAR FUNCTIONS **********************************/

    /**
     * Sets this MapNode's G value, which is the cost from Kiosk to this MapNode
     * @param g
     */
    public void setG(double g) {
        this.g = g;
    }

    /**
     * Retrieves this MapNode's G value, which is the cost from Kiosk to this MapNode
     * @return this MapNode's G value as a double
     */
    public double getG() {
        return g;
    }

    /**
     * Retrieves this MapNode's F value, which is the sum of this MapNode's Heuristic and this MapNode's G value
     * @return this MapNode's F value, as a double
     */
    public double getF() {
        return f;
    }

    /**
     * Sets this MapNode's F value, which is the sum of this MapNode's Heuristic and this MapNode's G value
     * @param f
     */
    public void setF(double f) {
        this.f = f;
    }

    /**
     * Sets this MapNode's Heuristic, which is the estimated optimistic distance to the target
     * @param heuristic
     */
    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * Retrieves this MapNode's Heuristic, which is the estimated optimistic distance to the target
     * @return this MapNode's Heuristic, as a double.
     */
    public double getHeuristic() {
        return heuristic;
    }

    /**
     * Resets the temp values (g, heuristic, f, and parent) used for pathfinding to the default values.
     */
    public void resetTempValues() {
        this.g = 0;
        this.heuristic = Double.MAX_VALUE;
        this.f = Double.MAX_VALUE;
        this.parent = null;
    }

    public NodeType getType()
    {
        return type;
    }


    @Override
    public String toString()
    {
        return label;
    }
}
