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
    float g = 0;
    float heuristic = Float.MAX_VALUE;
    float f = Float.MAX_VALUE;
    NodeEdge parent = null;
    Image node = null;
    Floor myFloor;
    public HashSet<NodeEdge> edges;

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public float getG() {
        return g;
    }

    public float getHeuristic() {
        return heuristic;
    }

    public float getF() {
        return f;
    }

    public NodeEdge getParent() {
        return parent;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setHeuristic(float heuristic) {
        this.heuristic = heuristic;
    }

    public void setF(float f) {
        this.f = f;
    }

    public void setParent(NodeEdge parent) {
        this.parent = parent;
    }

    public Floor getMyFloor() {
        return myFloor;
    }




    public MapNode() {

    }

    public MapNode(int nodeID) {
        this.nodeID = nodeID;
    }

    public boolean equals(Object obj) {
        if (obj instanceof MapNode) {
            return obj.equals(this);
        }
        return false;
    }

    public int hashCode() {
        return (nodeID*11 +posX);
    }

    public boolean equals(MapNode aNode) {
        return (this.nodeID == ((MapNode) aNode).nodeID);
    }
}
