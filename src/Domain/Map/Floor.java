package Domain.Map;

import javafx.scene.Node;

import java.util.LinkedList;

/**
 * Represents a floor in a building. A floor will have nodes, node edges, destinations, more
 */
public class Floor implements Comparable{
    LinkedList<MapNode> floorNodes;
    LinkedList<NodeEdge> floorEdges;
    MapNode kioskNode = null;

    public LinkedList<MapNode> getFloorNodes() {
        return floorNodes;
    }

    public LinkedList<NodeEdge> getFloorEdges() {
        return floorEdges;
    }

    int floorNumber;

    public Floor(int floorNumber) {
        floorNodes = new LinkedList<MapNode>();
        floorEdges = new LinkedList<NodeEdge>();
        this.floorNumber = floorNumber;
    }

    public void addNode(MapNode n) {
        floorNodes.add(n);
        n.setFloor(this);
    }

    public void setKioskLocation(MapNode kioskNode){
        for(MapNode n : this.floorNodes){
            if(kioskNode.equals(n)){
                this.kioskNode = kioskNode;
            }
        }
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
    public void removeNode(MapNode n) {
        floorNodes.remove(n);
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
}
