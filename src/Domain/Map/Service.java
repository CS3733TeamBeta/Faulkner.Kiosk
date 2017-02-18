package Domain.Map;

/**
 * Created by Brandon on 2/18/2017.
 */

public class Service {

    int serviceID;
    String name;
    int nodeID;
    String type;
    int floor;

    public Service(int serviceID, String name, int nodeID, String type, int floor) {

        this.serviceID = serviceID;
        this.name = name;
        this.nodeID = nodeID;
        this.type = type;
        this.floor = floor;
    }

    public int getServiceID() {
        return serviceID;
    }

    public String getName() {
        return name;
    }

    public int getNodeID() {
        return nodeID;
    }

    public String getType() {
        return type;
    }

    public int getFloor() {
        return floor;
    }
}
