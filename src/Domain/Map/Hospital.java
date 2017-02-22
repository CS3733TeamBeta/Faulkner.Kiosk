package Domain.Map;


import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
  * Created by IanCJ on 1/29/2017.
  */
public class Hospital {
    HashSet<Building> buildings;
    HashMap<Floor, MapNode> kioskNodeList = null;
    MapNode kioskNode = null;
    public static ObservableList<Integer> floorsWithKiosk;
    /**
     * Creates a new Hospital with an empty HashSet of buildings
     */

    private HashMap<String, Doctor> doctors;
    private HashMap<UUID, Destination> destinations;
    private HashMap<String, Office> offices;

    public Hospital() {
        buildings = new HashSet<Building>();

        doctors = new HashMap<>();
        destinations = new HashMap<>();
        offices = new HashMap<>();
    }

    public void addBuilding(Building b) {
        buildings.add(b);
    }

    //Alter Doctor HashMap: doctors
    public HashMap<String, Doctor> getDoctors() {
        return doctors;
    }
    public void addDoctors(String s, Doctor doc) {
        doctors.put(s, doc);
    }
    public void setDoctors(HashMap<String, Doctor> doctors) {
        this.doctors = doctors;
    }
    public void removeDoctors(String s){
        doctors.remove(s);
    }


    //Alter Suite HashMap: destinations
    public HashMap<UUID, Destination> getDestinations() {
        return destinations;
    }
    public void addDestinations(UUID uuid, Destination suite) {
        destinations.put(uuid, suite);
    }
    public void setDestinations(HashMap<UUID, Destination> destinations) {
        this.destinations = destinations;
    }
    public void removeSuites(String s){
        destinations.remove(s);
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
                            if (kioskNodeList.get(kioskNode.myFloor).equals(n)) {
                                kioskNodeList.put(kioskNode.myFloor, kioskNode);
                                setFloorsWithKiosk(kioskNode.myFloor);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void setFloorsWithKiosk(Floor floor) {
        Hospital.floorsWithKiosk.add(floor.floorNumber);
    }
}
