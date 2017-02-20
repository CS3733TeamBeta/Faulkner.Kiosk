package Domain.Map;

import java.util.HashSet;

/**
 * Created by Brandon on 2/13/2017.
 */
public class Suite {

    int suiteID;
    String name;
    MapNode location;

    public Suite() {}

    public Suite(int id, String name, MapNode location) {
        this.suiteID = id;
        this.name = name;
        this.location = location;
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

}
