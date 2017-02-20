package Domain.Navigation;

import Domain.Map.Floor;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;

import java.util.LinkedList;

/**
 * Created by Pattop on 2/16/2017.
 */
public class DirectionStep {
    Floor floorOfTheseDirections;
    LinkedList<String> directionsForThisFloor;
    LinkedList<MapNode> nodesInStep;
    LinkedList<NodeEdge> edgesInStep;

    public DirectionStep(Floor floor){
        floorOfTheseDirections = floor;
        directionsForThisFloor = new LinkedList<String>();
    }

    public DirectionStep(Floor floor, LinkedList<MapNode> nodesInStep, LinkedList<NodeEdge> edgesInStep) {
        floorOfTheseDirections = floor;
        directionsForThisFloor = new LinkedList<>();
        this.nodesInStep = nodesInStep;
        this.edgesInStep = edgesInStep;
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


    public Floor getFloor()
    {
        return floorOfTheseDirections;
    }

    public void setNodesInStep(LinkedList<MapNode> listOfMapNodes) {
        this.nodesInStep = listOfMapNodes;
    }

    public void setEdgesInStep(LinkedList<NodeEdge> listOfNodeEdges) {
        this.edgesInStep = listOfNodeEdges;
    }


    public void addNode(MapNode n) {
        nodesInStep.addLast(n);
    }

    public void addEdge(NodeEdge e) {
        edgesInStep.addLast(e);
    }

    public LinkedList<String> getDirections(){
        return directionsForThisFloor;
    }

}
