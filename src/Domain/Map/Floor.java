package Domain.Map;

import javafx.scene.Node;

import java.util.LinkedList;

/**
 * Represents a floor in a building. A floor will have nodes, node edges, destinations, more
 */
public class Floor {
    LinkedList<MapNode> floorNodes;
    LinkedList<NodeEdge> floorEdges;
    MapNode kioskNode = null;

    public LinkedList<MapNode> getFloorNodes() {
        return floorNodes;
    }

    public LinkedList<NodeEdge> getFloorEdges() {
        return floorEdges;
    }

    int floorNumber;
    String floorName;
    int building;

    public Floor(String floorID, int buildID, int floorNumber) {
        floorNodes = new LinkedList<MapNode>();
        floorEdges = new LinkedList<NodeEdge>();
        this.floorName = floorID;
        this.floorNumber = floorNumber;
        this.building = buildID;
    }

    public void addNode(MapNode n) {
        floorNodes.add(n);
        n.setFloor(this);
    }

    public void setKioskLocation(MapNode kioskNode){
        for(MapNode n : this.floorNodes){
            if(kioskNode.equals(n)){
                this.kioskNode = kioskNode;
            }
        }
    }

    public String getFloorName() {
        return this.floorName;
    }

    public int getBuilding() {
        return this.building;
    }

    public MapNode getKioskNode(){
        return kioskNode;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void addEdge(NodeEdge e) {
        floorEdges.add(e);
    }

    public void removeNode(MapNode n)
    {
        floorNodes.remove(n);
        if(n.equals(kioskNode)){
            System.out.println("DANGER: KIOSK DELETED");
            this.kioskNode = null;
        }
    }

    public void removeEdge(NodeEdge edge)
    {
        floorEdges.remove(edge);
    }
}
