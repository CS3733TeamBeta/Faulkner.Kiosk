package Domain.Map;

import javafx.scene.image.Image;

import java.rmi.server.UID;
import java.util.HashSet;

/**
 * Represents a node in a Map, connected to other nodes by NodeEdges
 */

public class MapNode
{
    int posX;
    int posY;
    int nodeID;
    UID nodeUID;
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

    public HashSet<NodeEdge> getEdges() {
        return edges;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setFloor(Floor f) {
        this.myFloor = f;
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

    public int getNodeID(){ return this.nodeID; }

    public UID getNodeUID() {
        return this.nodeUID;
    }

    public MapNode() {
        this.edges = new HashSet<NodeEdge>();
        this.nodeUID = new UID();
    }


    public MapNode(int nodeID) {
        this();
        this.nodeID = nodeID;
    }

    public MapNode(int nodeID, int posX, int posY) {
        this();
        this.nodeID = nodeID;
        this.posX = posX;
        this.posY = posY;
    }

    public void addEdge(NodeEdge e) {
        this.edges.add(e);
    }

    public void resetTempValues() {
        this.g = 0;
        this.heuristic = Float.MAX_VALUE;
        this.f = Float.MAX_VALUE;
        this.parent = null;
    }

    public boolean equals(Object obj) {
        if (obj instanceof MapNode) {
            return (this.nodeUID.equals(((MapNode) obj).getNodeUID()));
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (nodeUID.hashCode());
    }

    public boolean equals(MapNode aNode) {
        return (this.nodeID == ((MapNode) aNode).nodeID);
    }
}
