package Domain.Navigation;

import Domain.Map.*;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Path is the path from one Destination to another
 */
public class Path implements Iterable
{
    LinkedList<NodeEdge> pathEdges;

    public Path (Destination start, Destination end) {

        LinkedList<MapNode> openSet = new LinkedList<MapNode>();
        LinkedList<MapNode> closedSet = new LinkedList<MapNode>();

        openSet.add(start);

        while (openSet.size() > 0) {
            MapNode currentNode = openSet.getFirst();

            //openSet.remove(0);
            closedSet.add(currentNode);

            if (currentNode.equals(end)) {
                //do something
            }

            for (NodeEdge aEdge: currentNode.edges) {
                MapNode aNode = aEdge.getOtherNode(currentNode);
                if (!closedSet.contains(aNode)) { //If errors, make this more explicit
                    float tentativeNewHeuristic = (float)findHeuristic(aNode, end);
                    float tentativeNewGValue = currentNode.getG() + aEdge.getCost();
                    if(tentativeNewHeuristic + tentativeNewGValue < aNode.getF()) {
                        aNode.setHeuristic(tentativeNewHeuristic);
                        aNode.setG(tentativeNewGValue);
                        aNode.setF(tentativeNewHeuristic + tentativeNewGValue);
                        aNode.setParent(aEdge);
                    }

                    if(!openSet.contains(aNode)) {
                        openSet.add(aNode);
                    }

                }
            }
            //sort the list


        }


        //@TODO: calculate path with A*

        //When done, set all nodes back to default values
    }

    public static double findHeuristic(MapNode currentNode, MapNode endNode){
        double currentNodeX = (double)currentNode.getPosX();
        double currentNodeY = (double)currentNode.getPosY();
        double endNodeX = (double)endNode.getPosX();
        double endNodeY = (double)endNode.getPosY();
        return Math.sqrt(Math.pow(endNodeY - currentNodeY, 2) + Math.pow(endNodeX - currentNodeX, 2));
    }

    public MapNode popSmallestF(LinkedList<MapNode> openSet) {
        int indexSmallest = 0;
        MapNode smallestNode = new MapNode();
        int indexCurrent;
        for (indexCurrent = 0; indexCurrent < openSet.size(); indexCurrent++) {
            MapNode currentNode = openSet.get(indexCurrent);
            if (currentNode.getF() < smallestNode.getF()) {
                indexSmallest = indexCurrent;
                smallestNode = currentNode;
            }
        }
        openSet.remove(indexCurrent);
        return smallestNode;
    }

    public Iterator iterator()
    {
        return pathEdges.iterator();
    }
}
