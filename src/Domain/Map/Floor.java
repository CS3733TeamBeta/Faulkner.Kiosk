package Domain.Map;

import java.util.LinkedList;

/**
 * Represents a floor in a building. A floor will have nodes, node edges, destinations, more
 */
public class Floor {
    LinkedList<MapNode> floorNodes;
    LinkedList<NodeEdge> floorEdges;

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

    public int getFloorNumber() {
        return floorNumber;
    }

    public void addEdge(NodeEdge e) {
        floorEdges.add(e);
    }
}
