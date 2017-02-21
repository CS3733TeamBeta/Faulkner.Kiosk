package Domain.Map;


import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by IanCJ on 1/29/2017.
 */
public class Hospital {
    HashSet<Building> buildings;
    MapNode kioskNode = null;

    /**
     * Creates a new Hospital with an empty HashSet of buildings
     */
    public Hospital() {
        buildings = new HashSet<Building>();
    }

    /**
     * Adds a building to this Hosptial's list of buildings
     *
     * @param b
     */
    public void addBuilding(Building b) {
        buildings.add(b);
    }

    public boolean containsBuilding(Building b) {
        return buildings.contains(b);
    }

    public int buildingCount() {
        return buildings.size();
    }

    public Collection<Building> getBuildings() {
        return buildings;
    }

    public MapNode getKioskNode() {
        return kioskNode;
    }

    /**
     * Sets this floor's KioskNode, which is the node that navigation begins from, to the given MapNode
     *
     * @param kioskNode
     */
    public void setKioskLocation(MapNode kioskNode) {
        int i;
        int j;
        for (i = 0; i < buildings.size(); i++) {
            if (buildings.iterator().hasNext()) {
                Building nextBuilding = this.buildings.iterator().next();
                if (nextBuilding.getFloors().iterator().hasNext()) {
                    for (j = 0; j < nextBuilding.getFloors().size(); i++) {
                        Floor nextFloor = nextBuilding.getFloors().iterator().next();
                        for (MapNode n : nextFloor.floorNodes) {
                            if (kioskNode.equals(n)) {
                                this.kioskNode = kioskNode;
                            }
                        }
                    }
                }
            }
        }
    }
}
