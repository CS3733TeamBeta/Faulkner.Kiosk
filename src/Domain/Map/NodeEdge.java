package Domain.Map;

import java.util.Map;

/**
 * An edge that connects two nodes and has a cost (edge length)
 */
public class NodeEdge {
/*
    public static class EdgeEndPoint
    {

    }
    /*
    protected EdgeEndPoint a;
    protected EdgeEndPoint b;

    public NodeEdge()
    {

    }

    public NodeEdge(Node nodeA, Node nodeB)
    public NodeEdge(MapNode nodeA, MapNode nodeB)
    {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
*/


    protected double cost;

    MapNode nodeA;
    MapNode nodeB;


    public NodeEdge() {}

    public NodeEdge(MapNode nodeA, MapNode nodeB) {
        this();
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    public NodeEdge(MapNode nodeA, MapNode nodeB, double cost) {
        this(nodeA, nodeB);
        this.cost = cost;
        nodeA.addEdge(this);
        nodeB.addEdge(this);

    }

    public double getCost() {
        return cost;
    }
    public MapNode getNodeA() {
        return nodeA;
    }
    public MapNode getNodeB() {
        return nodeB;
    }

    //Returns the node connected to this edge that isn't passed in
    public MapNode getOtherNode(MapNode n)
    {
        if(nodeA.equals(n))
        {
            return nodeB;
        }
        else return nodeA;
    }
}
