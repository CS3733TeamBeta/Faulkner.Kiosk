package Domain.Map;


/**
 * Created by benhylak on 1/28/17.
 */
public class LinkEdge extends NodeEdge
{
    public LinkEdge(MapNode nodeA, MapNode nodeB)
    {
        super(nodeA, nodeB);

        cost = 0;
    }
}
