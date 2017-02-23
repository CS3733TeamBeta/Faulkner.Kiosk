package Domain.Map;


import java.util.*;

/**
  * Created by IanCJ on 1/29/2017.
  */
public class Hospital {
    HashSet<Building> buildings;

    private LinkedList<MapNode> kioskNodes = new LinkedList<>();
    MapNode mainKioskNode = null;
    String directionOfMainMode = null;

    private HashMap<String, Doctor> doctors;
    private HashMap<UUID, Destination> destinations;
    private HashMap<String, Office> offices;

    CampusFloor CampusFloor;

    public Hospital() {
        buildings = new HashSet<Building>();
        doctors = new HashMap<>();
        destinations = new HashMap<>();
        offices = new HashMap<>();

        CampusFloor  = new CampusFloor();
    }

    public void setMainKioskNode(MapNode n){
        this.mainKioskNode = n;
    }

    public MapNode getMainKioskNode(){
        return this.mainKioskNode;
    }

    public void setDirectionOfMainNode(String directionOfMainNode){
        this.directionOfMainMode = directionOfMainNode;
    }

    public void addBuilding(Building b) {
        buildings.add(b);
    }

    public CampusFloor getCampusFloor()
    {
        CampusFloor.clearBuildings();

        for(Building b: getBuildings())
        {
            CampusFloor.importBuilding(b);
        }

        return CampusFloor;
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

    //Alter Office HashMap: offices
    public void addOffices(String s, Office off) {
        offices.put(s, off);
    }
    public HashMap<String, Office> getOffices() {
        return this.offices;
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
