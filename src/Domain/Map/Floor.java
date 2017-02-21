package Domain.Map;

import javafx.scene.Node;

import java.util.LinkedList;

/**
 * Represents a floor in a building. A floor will have nodes, node edges, destinations, more
 */
public class Floor implements Comparable{
    LinkedList<MapNode> floorNodes;
    LinkedList<NodeEdge> floorEdges;

    int floorNumber;



    /**
     * Creates a floor with the given floorNumber and empty nodes/edges
     * @param floorNumber the floorNumber desired.
     */
    public Floor(int floorNumber) {
        floorNodes = new LinkedList<MapNode>();
        floorEdges = new LinkedList<NodeEdge>();
        this.floorNumber = floorNumber;
    }

    /**
     * Retrieves the nodes on this floor
     * @return the nodes on this floor as a LinkedList<MapNode>
     */
    public LinkedList<MapNode> getFloorNodes() {
        return floorNodes;
    }

    /**
     * Retrieves the edges on this floor
     * @return the edges on this floor as a LinkedList<NodeEdge>
     */
    public LinkedList<NodeEdge> getFloorEdges() {
        return floorEdges;
    }

    /**
     * Adds the given node to this floor, and sets that node's floor to this
     * @param n The node to be added.
     */
    public void addNode(MapNode n) {
        floorNodes.add(n);
        n.setFloor(this);
    }




    /**
     * Retrieves the floor number of the given floor
     * @return the floor number of this floor as an int
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Adds the given edge to this floor's list of edges
     * @param e the NodeEdge to be added
     */
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

    /**
     * Removes the given edge from this floor's lift of edges.
     * @param edge the NodeEdge to be removed.
     */
    public void removeEdge(NodeEdge edge)
    {
        floorEdges.remove(edge);
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
