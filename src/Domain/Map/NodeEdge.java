package Domain.Map;

/**
 * An edge that connects two nodes and has a cost (edge length)
 */
public class NodeEdge
{
    protected float cost;

    MapNode nodeA;
    MapNode nodeB;

    public NodeEdge()
    {

    }

    public NodeEdge(MapNode nodeA, MapNode nodeB)
    {
        this();

        this.nodeA = nodeA;
        this.nodeB = nodeB;
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
