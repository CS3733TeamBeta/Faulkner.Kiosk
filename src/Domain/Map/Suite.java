package Domain.Map;

import java.util.UUID;

/**
 * Created by Brandon on 2/13/2017.
 */
public class Suite {

    UUID suiteID;
    String name;
    MapNode location;

    public Suite(String name, MapNode location) {
        this.suiteID = UUID.randomUUID();
        this.name = name;
        this.location = location;
    }

    public Suite(UUID suiteID, String name, MapNode location) {
        this.suiteID = suiteID;
        this.name = name;
        this.location = location;
    }

    public UUID getSuiteID() {
        return this.suiteID;
    }

    public String getName() {
        return this.name;
    }

    public MapNode getLocation() {
        return this.location;
    }

}
