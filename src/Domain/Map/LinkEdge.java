package Domain.Map;


/**
 * Created by benhylak on 1/28/17.
 */
public class LinkEdge extends NodeEdge
{
    public LinkEdge(MapNode nodeA, MapNode nodeB)
    {
        super(nodeA, nodeB, 0);
        edgeLine.setVisible(false);
    }

    public LinkEdge(NodeEdge e)
    {
        this.setSource(e.getSource());
        this.setTarget(e.getTarget());

        cost = e.getCost();
    }


    @Override
    public void updateCost()
    {
        this.cost = 0;
    }
}
