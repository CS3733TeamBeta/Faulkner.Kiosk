package Entity;


import Entity.Map.Building;
import Entity.Map.CampusFloor;
import Entity.Map.Destination;
import Entity.Map.Office;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
  * Created by IanCJ on 1/29/2017.
  */
public class Hospital {
    HashSet<Building> buildings;

    private HashMap<String, Doctor> doctors;
    private HashMap<UUID, Destination> destinations;
    private HashMap<String, Office> offices;

    Entity.Map.CampusFloor CampusFloor;

    public Hospital() {
        buildings = new HashSet<Building>();
        destinations = new HashMap<>();
        offices = new HashMap<>();
        doctors = new HashMap<>();

        CampusFloor  = new CampusFloor();
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

    //Alter Suite HashMap: destinations
    public HashMap<UUID, Destination> getDestinations() {
        return destinations;
    }
    public void addDestinations(UUID uuid, Destination suite) {
        destinations.put(uuid, suite);
    }

    //Alter Office HashMap: offices
    public void addOffices(String s, Office off) {
        offices.put(s, off);
    }
    public HashMap<String, Office> getOffices() {
        return this.offices;
    }

    public Collection<Building> getBuildings()
    {
        return buildings;
    }

}
