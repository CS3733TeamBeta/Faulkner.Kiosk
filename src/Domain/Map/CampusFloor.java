package Domain.Map;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by benhylak on 2/23/17.
 */
public class CampusFloor extends Floor
{
    HashSet<MapNode> campusNodes;

    public CampusFloor()
    {
        super(1); //sorta hacky but should do the trick...
        campusNodes = new HashSet<MapNode>();
    }

    @Override
    public void addNode(MapNode n)
    {
        campusNodes.add(n);
        super.addNode(n);
    }

    public void clearBuildings()
    {
        getFloorNodes().clear();
        getFloorEdges().clear();

        for(MapNode n: campusNodes)
        {
            super.addNode(n);
        }
    }

    public Collection<MapNode> getCampusNodes()
    {
        return campusNodes;
    }

    public void importBuilding(Building b)
    {
        try
        {
            Floor ground = b.getFloor(1);

            for(MapNode n: ground.getFloorNodes())
            {
                super.addNode(n);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
