package Domain.Map;

import java.util.LinkedList;

/**
 * Represents a floor in a building. A floor will have nodes, node edges, destinations, more
 */
public class Floor {
    LinkedList<MapNode> floorNodes;
    LinkedList<NodeEdge> floorEdges;

    public Floor() {
        floorNodes = new LinkedList<MapNode>();
        floorEdges = new LinkedList<NodeEdge>();
    }

    public void addNode(MapNode n) {
        floorNodes.add(n);
    }

    public void addEdge(NodeEdge e) {
        floorEdges.add(e);
    }
}
