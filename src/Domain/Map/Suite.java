package Domain.Map;

import java.util.HashSet;

/**
 * Created by Brandon on 2/13/2017.
 */
public class Suite {

    int suiteID;
    String name;
    MapNode location;
    HashSet<String> doctors;
    Floor myFloor;

    public Suite(int id, String name, Floor floor, MapNode location, HashSet<String> doctors) {
        this.suiteID = id;
        this.name = name;
        this.myFloor = floor;
        this.location = location;
        this.doctors = doctors;
    }

    public void addDoctor(String name) {
        doctors.add(name);
    }

    public int getSuiteID() {
        return this.suiteID;
    }

    public String getName() {
        return this.name;
    }

    public Floor getMyFloor() {
        return this.myFloor;
    }

    public MapNode getLocation() {
        return this.location;
    }

    public HashSet<String> getDoctors() {
        return this.doctors;
    }
}
