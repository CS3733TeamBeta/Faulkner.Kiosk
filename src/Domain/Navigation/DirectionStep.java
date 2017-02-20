package Domain.Navigation;

import Domain.Map.Floor;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;

import javax.xml.soap.Node;
import java.util.LinkedList;

/**
 * Created by Pattop on 2/16/2017.
 */
public class DirectionStep {
    Floor floorOfTheseDirections;
    LinkedList<String> directionsForThisFloor;
    LinkedList<MapNode> nodesForThisFloor;
    LinkedList<NodeEdge> edgesForThisFloor;

    public DirectionStep(Floor floor){
        floorOfTheseDirections = floor;
        directionsForThisFloor = new LinkedList<String>();
    }

    public void setNodesForThisFloor(LinkedList<MapNode> listOfNodes) {
        this.nodesForThisFloor = listOfNodes;
    }

    public void setEdgesForThisFloor(LinkedList<NodeEdge> listOfEdges) {
        this.edgesForThisFloor = listOfEdges;
    }

    public void addNode(MapNode n) {
        this.nodesForThisFloor.add(n);
    }

    public void addEdge(NodeEdge e) {
        this.edgesForThisFloor.add(e);
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

    public LinkedList<String> getDirections(){
        return directionsForThisFloor;
    }

}
