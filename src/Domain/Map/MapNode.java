package Domain.Map;

import Controller.Admin.PopUp.AbstractPopupController;
import Controller.Admin.PopUp.DestinationEditController;
import Controller.Admin.PopUp.NodeEditController;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DragIconType;
import Domain.ViewElements.DrawableMapEntity;
import Domain.ViewElements.Events.DeleteRequestedEvent;
import Domain.ViewElements.Events.DeleteRequestedHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Represents a node in a Map, connected to other nodes by NodeEdges
 */

public class MapNode implements DrawableMapEntity
{
    double posX;
    double posY;

    protected DragIcon icon;

    final double NODE_HOVER_OPACITY = .65;
    final double NODE__NORMAL_OPACITY = 1;

    private final String popOverEditFXML = "/Admin/Popup/NodeEditPopup.fxml";

    protected ArrayList<DeleteRequestedHandler> deleteEventHandlers = null;

    int nodeID; //Used for a human-identifiable

    UID nodeUID;

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

    public MapNode() {
        this.edges = new HashSet<NodeEdge>();
        this.nodeUID = new UID();
        icon = new DragIcon();
        icon.setPrefSize(25, 25);
    }

    public MapNode(int nodeID) {
        this();
        this.nodeID = nodeID;
    }

    public MapNode(int nodeID, int posX, int posY) {
        this(nodeID);

        this.posX = posX;
        this.posY = posY;

        this.setType(DragIconType.values()[0]);
    }

    public MapNode(int nodeID, int posX, int posY, int type) {
        this(nodeID);

        this.posX = posX;
        this.posY = posY;

        this.setType(DragIconType.values()[type]);
    }

    /**
     *
     * @param posX
     */
    public void setPosX(double posX)
    {
        this.posX = posX;
    }

    public void setPosY(double posY)
    {
        this.posY = posY;
    }

    /**
     *
     * @return X Position in Scene Coordinates
     */
    public double getPosX() {
        return posX;
    }

    /**
     *
     * @return Y Position in Scene Coordinates
     */
    public double getPosY() {
        return posY;
    }


    public void setFloor(Floor f) {this.myFloor = f;}


    public NodeEdge getParent() {
        return parent;
    }
    public Collection<NodeEdge> getEdges() {return edges;}

    /**
     * Determines if this node has a node to another node.
     *
     * @param n
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

    public void setParent(NodeEdge parent) {
        this.parent = parent;
    }

    public Floor getMyFloor() {
        return myFloor;
    }

    public int getNodeID(){ return this.nodeID; }

    public UID getNodeUID() {
        return this.nodeUID;
    }

    /**
     * Add an edge to this mapnode
     * @param e edge to add
     */
    public void addEdge(NodeEdge e) {
        this.edges.add(e);
    }

    public boolean equals(Object obj) {
        if (obj instanceof MapNode) {
            return this.equals((MapNode) obj);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (nodeUID.hashCode());
    }

    public boolean equals(MapNode aNode) {
        return (this.nodeUID.equals(aNode.getNodeUID()));
    }

    /*****************************GRAPHICAL FUNCTIONS**********************/
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


    @Override
    public Node getNodeToDisplay()
    {
        return icon;
    }

    /**
     * Sends underlying icon to back
     */
    public void toBack()
    {
        icon.toBack();
    }

    /**
     * Sends underlying icon to front
     */
    public void toFront()
    {
        icon.toFront();
    }

    /**
     * When handlers susbscribe, notifies them that this mapnode should be deleted
     */
    public void deleteFromMap()
    {
        raiseDeleteRequested();
    }

    protected void raiseDeleteRequested()
    {
        if(deleteEventHandlers!=null)
        {
            for (DeleteRequestedHandler handler : deleteEventHandlers)
            {
                handler.handle(new DeleteRequestedEvent(this));
            }
        }
        else {
            System.out.println("You've requested to delete a node, but the node isn't set up to do so. (Hint: Add a DeleteRequestedHandler)");
        }
    }

    public void setOnDeleteRequested(DeleteRequestedHandler handler)
    {
        if(deleteEventHandlers==null)
        {
            deleteEventHandlers = new ArrayList<>();
        }

        deleteEventHandlers.add(handler);
    }

    /**Returns a pop over window to edit this node**/
    public PopOver getEditPopover()
    {
        NodeEditController controller = new NodeEditController(this);

        return getPopOver(controller, popOverEditFXML);
    }

    protected final PopOver getPopOver(AbstractPopupController controller, String fxmlPath)
    {
        PopOver popOver = new PopOver();

        FXMLLoader loader = new FXMLLoader(Destination.class.getResource(fxmlPath));
        controller.setPopOver(popOver); //sets the popover used by the controller

        loader.setController(controller);

        try
        {
            popOver.setContentNode(loader.load());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return popOver;
    }

    /*******************************A STAR FUNCTIONS **********************************/

    public void setG(double g) {
        this.g = g;
    }
    public double getG() {
        return g;
    }
    public double getF() {
        return f;
    }
    public void setF(double f) {
        this.f = f;
    }
    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }
    public double getHeuristic() {
        return heuristic;
    }

    /**
     * Resets the temp values used for pathfinding to the default values.
     */
    public void resetTempValues() {
        this.g = 0;
        this.heuristic = Double.MAX_VALUE;
        this.f = Double.MAX_VALUE;
        this.parent = null;
    }

}
