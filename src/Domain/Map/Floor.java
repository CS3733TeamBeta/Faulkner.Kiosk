package Domain.Map;

import Domain.ViewElements.DragIconType;
import Model.DataSourceClasses.HierarchyData;
import Model.DataSourceClasses.Treeable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import javax.transaction.TransactionRequiredException;
import java.util.*;

/**
 * Represents a floor in a building. A floor will have nodes, node edges, destinations, more
 */
public class Floor extends Treeable implements Comparable
{

    HashSet<MapNode> floorNodes;
    HashSet<NodeEdge> floorEdges;

    ObservableList<Treeable> treeItems;

    MapNode kioskNode = null;
    String imageLocation = "/FloorMaps/1_thefirstfloor.png"; //default floor image path

    ProxyImage imageInfo;

    Building building;

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
        imageInfo = new ProxyImage("/FloorMaps/" + imageLocation);
    }

    public void initImage(){
        imageInfo = new ProxyImage(imageLocation);
    }

    public Image getImage(){
        return imageInfo.getImage();
    }

    public HashSet<MapNode> getFloorNodes() {
        return floorNodes;
    }

    public HashSet<NodeEdge> getFloorEdges() {
        return floorEdges;
    }

    int floorNumber;

    public Floor(int floorNumber) {
        treeItems= FXCollections.observableList(new ArrayList<>());
        floorNodes = new HashSet<MapNode>();
        floorEdges = new HashSet<NodeEdge>();
        this.floorNumber = floorNumber;
    }

    public void addNode(MapNode n) {
        floorNodes.add(n);

        if(!n.getType().equals(DragIconType.Connector))
        {
            treeItems.add(n);
        }

        n.setFloor(this);
    }

    public MapNode getKioskNode(){
        return kioskNode;
    }

    /**
     * Retrieves the floor number of the given floor
     * @return the floor number of this floor as an int
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    public void addEdge(NodeEdge e) {
        floorEdges.add(e);
    }

    /**
     * Removes the given node from this floor's list of nodes. If the given node is the kiosknode, set the kiosknode to null and print a warning
     * @param n the MapNode to be removed.
     */
    public void removeNode(MapNode n)
    {
        floorNodes.remove(n);

        if(!n.getType().equals(DragIconType.Connector))
        {
            treeItems.remove(n);
        }
    }

    public void removeEdge(NodeEdge edge)
    {
        floorEdges.remove(edge);
    }


    /**
     * Sets this floor's KioskNode, which is the node that navigation begins from, to the given MapNode
     * @param kioskNode
     */
    public void setKioskLocation(MapNode kioskNode){
        for(MapNode n : this.floorNodes){
            if(kioskNode.equals(n)){
                this.kioskNode = kioskNode;
            }
        }
    }

    public Building getBuilding()
    {
        return building;
    }

    public void setBuilding(Building b)
    {
        building = b;
    }

    @Override
    public String toString()
    {
        return "Floor " + floorNumber;
    }

    @Override
    public int compareTo(Object o)
    {
        return Integer.compare(this.floorNumber, ((Floor)o).getFloorNumber());
    }

    public ObservableList<Treeable> getChildren()
    {
        return treeItems;
    }
}
