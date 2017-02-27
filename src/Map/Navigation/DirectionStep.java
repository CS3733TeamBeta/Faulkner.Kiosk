package Map.Navigation;

import Map.Entity.Floor;
import Map.Entity.MapNode;

import java.util.LinkedList;

/**
 * Created by Pattop on 2/16/2017.
 */
public class DirectionStep {
    Floor floorOfTheseDirections;
    LinkedList<String> directionsForThisFloor;
    LinkedList<MapNode> nodesForThisFloor;

    public DirectionStep(Floor floor, LinkedList<String> directionsForThisFloor, LinkedList<MapNode> nodesForThisFloor) {
        this.floorOfTheseDirections = floor;
        this.directionsForThisFloor = directionsForThisFloor;
        this.nodesForThisFloor = nodesForThisFloor;
    }

    public DirectionStep(Floor floor){
        floorOfTheseDirections = floor;
        directionsForThisFloor = new LinkedList<String>();
    }

    public void setNodesForThisFloor(LinkedList<MapNode> listOfNodes) {
        this.nodesForThisFloor = listOfNodes;
    }

    public void addNode(MapNode n) {
        this.nodesForThisFloor.add(n);
    }


    public void addDirections(String newDirection){
        this.directionsForThisFloor.add(newDirection);
    }

    public void setDirections(LinkedList<String> directions){
        this.directionsForThisFloor.clear();
        for(String s : directions){
            this.directionsForThisFloor.add(s);
        }
    }

    public void printNodes() {
        if (nodesForThisFloor != null) {
            System.out.println("Printing Nodes:");
            for (MapNode n : nodesForThisFloor) {
                System.out.println("ID is: " + n.getNodeID());
            }
        } else {
            System.out.println("Nodes were null");
        }
    }
    public Floor getFloor()
    {
        return floorOfTheseDirections;
    }

    public LinkedList<String> getDirections(){
        return directionsForThisFloor;
    }

}
