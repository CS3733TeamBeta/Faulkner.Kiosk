package Domain.Map;

/**
 * An edge that connects two nodes and has a cost (edge length)
 */
public class NodeEdge
{
    protected float cost;

    Node nodeA;
    Node nodeB;

    public NodeEdge()
    {

    }

    public NodeEdge(Node nodeA, Node nodeB)
    {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
    }

    Node getOtherNode(Node n)
    {
        if(nodeA.equals(n))
        {
            return nodeB;
        }
        else return nodeA;
    }
}
