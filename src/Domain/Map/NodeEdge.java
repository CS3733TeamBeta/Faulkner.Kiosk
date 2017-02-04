package Domain.Map;

import java.util.Map;

/**
 * An edge that connects two nodes and has a cost (edge length)
 */
public class NodeEdge
{
    public static class EdgeEndPoint
    {

    }

    protected float cost;

    protected MapNode nodeA;
    protected MapNode nodeB;

    protected EdgeEndPoint a;
    protected EdgeEndPoint b;

    public NodeEdge()
    {

    }

    public NodeEdge(MapNode nodeA, MapNode nodeB)
    {
        this();

        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    public void setNodeA(MapNode nodeA)
    {
        this.nodeA = nodeA;
    }

    public void setNodeB(MapNode nodeB)
    {
        this.nodeB = nodeB;
    }

    public MapNode getNodeA()
    {
        return nodeA;
    }

    public MapNode getNodeB()
    {
        return nodeB;
    }

    MapNode getOtherNode(MapNode n)
    {
        if(nodeA.equals(n))
        {
            return nodeB;
        }
        else return nodeA;
    }
}
