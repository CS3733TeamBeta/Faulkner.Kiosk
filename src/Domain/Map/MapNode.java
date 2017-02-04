package Domain.Map;

import javafx.scene.image.Image;

import java.util.HashSet;

/**
 * Represents a node in a Map, connected to other nodes by NodeEdges
 */

public class MapNode
{
    int posX;
    int posY;
    int nodeID;
    int g = 0;
    int heuristic = Integer.MAX_VALUE;
    int f = Integer.MAX_VALUE;
    NodeEdge parent = null;
    Image node = null;

    Floor myFloor;
    public HashSet<NodeEdge> edges;



    public MapNode(int nodeID) {
        this.nodeID = nodeID;
    }

    public boolean equals(Object obj) {
        if (obj instanceof MapNode) {
            return obj.equals(this);
        }
        return false;
    }

    public boolean equals(MapNode aNode) {
        return (this.nodeID == ((MapNode) aNode).nodeID);
    }
}
