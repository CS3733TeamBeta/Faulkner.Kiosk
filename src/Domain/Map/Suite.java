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

    public Suite(int id, String name, MapNode location, HashSet<String> doctors) {
        this.suiteID = id;
        this.name = name;
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

    public MapNode getLocation() {
        return this.location;
    }

    public HashSet<String> getDoctors() {
        return this.doctors;
    }
}
