package main.Map.Entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.*;

/**
 * Represents a floor in a building. A floor will have nodes, node edges, destinations, more
 */
public class Floor extends Observable implements Comparable
{
    HashSet<MapNode> floorNodes;

    ObservableList<MapNode> tableItems;

    UUID floorID;

    MapNode kioskNode = null;
    String imageLocation = "/map/FloorMaps/1_thefirstfloor.png"; //default floor image path

    ProxyImage imageInfo;

    Building building;

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
        imageInfo = new ProxyImage("/map/FloorMaps/" + imageLocation);
    }

    public void initImage(){
        imageInfo = new ProxyImage("/FloorMaps/" + imageLocation);
    }

    public Image getImage(){
        System.out.println("Getting image in floor: " + imageLocation);
        if(imageInfo==null)
        {
            imageInfo = new ProxyImage("/FloorMaps/" + imageLocation);
        }

        return imageInfo.getImage();
    }

    public HashSet<MapNode> getFloorNodes() {
        return floorNodes;
    }


    int floorNumber;

    public Floor(int floorNumber) {
        tableItems= FXCollections.observableList(new ArrayList<>());
        floorNodes = new HashSet<MapNode>();
        this.floorNumber = floorNumber;
        this.floorID = UUID.randomUUID();
    }

    public Floor(UUID id, int floorNumber) {
        this.floorID = id;
        this.floorNumber = floorNumber;
        floorNodes = new HashSet<>();
        tableItems = FXCollections.observableList(new ArrayList<>());
    }

    public void addNode(MapNode n) {
      addNode(n, true);
    }

    public void addNode(MapNode n, boolean setFloor)
    {
        floorNodes.add(n);

        if(!n.getType().equals(NodeType.Connector))
        {
            tableItems.add(n);
        }

        if(setFloor)
        {
            n.setFloor(this);
        }

        n.addObserver((observer, args)->
        {
            int index = tableItems.indexOf(n);

            tableItems.remove(n);
            tableItems.add(index, n);
        });
    }

    public MapNode getKioskNode(){
        return kioskNode;
    }

    /**
     * Retrieves the floor number of the given floor
     * @return the floor number of this floor as an int
     */
    public int getFloorNumber() {
        return floorNumber;
    }


    /**
     * Removes the given node from this floor's list of nodes. If the given node is the kiosknode, set the kiosknode to null and print a warning
     * @param n the MapNode to be removed.
     */
    public void removeNode(MapNode n)
    {
        floorNodes.remove(n);

        if(!n.getType().equals(NodeType.Connector))
        {
            tableItems.remove(n);
        }


        if(n instanceof Kiosk)
        {
            getBuilding().getHospital().getKiosks().remove(n);
        }
    }

    /**
     * Sets this floor's KioskNode, which is the node that navigation begins from, to the given MapNode
     * @param kioskNode
     */
    public void setKioskLocation(MapNode kioskNode){
        for(MapNode n : this.floorNodes){
            if(kioskNode.equals(n)){
                this.kioskNode = kioskNode;
            }
        }
    }

    public Building getBuilding()
    {
        return building;
    }

    public void setBuilding(Building b)
    {
        building = b;
    }

    @Override
    public String toString()
    {
        return "Floor " + floorNumber;
    }

    @Override
    public int compareTo(Object o)
    {
        return Integer.compare(this.floorNumber, ((Floor)o).getFloorNumber());
    }

    public ObservableList<MapNode> getChildren()
    {
        return tableItems;
    }

    public UUID getFloorID() {
        return this.floorID;
    }
}
