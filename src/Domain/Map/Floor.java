package Domain.Map;

import javafx.scene.Node;

import java.util.LinkedList;

/**
 * Represents a floor in a building. A floor will have nodes, node edges, destinations, more
 */
public class Floor {
    private LinkedList<MapNode> floorNodes;
    private LinkedList<NodeEdge> floorEdges;
    private MapNode kioskNode = null;
    private int floorNumber;


    public LinkedList<MapNode> getFloorNodes() {
        return floorNodes;
    }

    public LinkedList<NodeEdge> getFloorEdges() {
        return floorEdges;
    }


    public Floor(int floorNumber) {
        floorNodes = new LinkedList<MapNode>();
        floorEdges = new LinkedList<NodeEdge>();
        this.floorNumber = floorNumber;
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
