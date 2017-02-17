package Domain.Map;


import java.util.Collection;
import java.util.HashSet;

/**
 * Created by IanCJ on 1/29/2017.
 */
public class Hospital {
    HashSet<Building> buildings;

    public Hospital() {
        buildings = new HashSet<Building>();
    }

    public void addBuilding(Building b) {
        buildings.add(b);
    }

    public boolean containsBuilding(Building b)
    {
        return buildings.contains(b);
    }

    public int buildingCount()
    {
        return buildings.size();
    }

    public Collection<Building> getBuildings()
    {
        return buildings;
    }

}
