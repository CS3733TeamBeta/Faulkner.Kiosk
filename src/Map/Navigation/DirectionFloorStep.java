package Map.Navigation;

import Map.Entity.*;
import Map.Entity.Floor;

import javax.xml.soap.Node;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Pattop on 2/16/2017.
 */
public class DirectionFloorStep {
    Floor floorOfTheseDirections;
    LinkedList<MapNode> nodesForThisFloor;
    LinkedList<DirectionStep> stepsInThisFloor;

    public DirectionFloorStep(Floor floor, LinkedList<MapNode> nodesForThisFloor, LinkedList<DirectionStep> stepsInThisFloor) {
        this.floorOfTheseDirections = floor;
        this.nodesForThisFloor = nodesForThisFloor;
        this.stepsInThisFloor = stepsInThisFloor;

    }

    public DirectionFloorStep(Floor floor){
        floorOfTheseDirections = floor;
        stepsInThisFloor = new LinkedList<>();
    }

    public void setNodesForThisFloor(LinkedList<MapNode> listOfNodes) {
        this.nodesForThisFloor = listOfNodes;
    }

    public void addNode(MapNode n) {
        this.nodesForThisFloor.add(n);
    }


    public void addDirectionStep(DirectionStep step){
        this.stepsInThisFloor.add(step);
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

    public LinkedList<DirectionStep> getDirectionSteps(){
        return stepsInThisFloor;
    }

}