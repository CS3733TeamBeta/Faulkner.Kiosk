package Domain.Map;

import java.util.HashSet;

/**
 * Created by benhylak on 1/29/17.
 */
public class Hospital {
    HashSet<Building> buildings;

    public Hospital() {
        buildings = new HashSet<Building>();
    }

    public void addBuilding(Building b) {
        buildings.add(b);
    }

}
