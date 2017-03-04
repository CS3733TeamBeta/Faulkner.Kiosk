package main.Map.Entity;


/**
 * Created by benhylak on 1/28/17.
 */
public class LinkEdge extends NodeEdge
{
    public LinkEdge(MapNode nodeA, MapNode nodeB)
    {
        super(nodeA, nodeB, 0);
    }

    @Override
    public void updateCost()
    {
        this.cost = 0;
    }
}
