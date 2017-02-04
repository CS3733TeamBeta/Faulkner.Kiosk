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

            openSet.remove(0);
            closedSet.add(currentNode);

            if (currentNode.equals(end)) {
                //do something
            }

            for (NodeEdge aEdge: currentNode.edges) {
                MapNode aNode = aEdge.getOtherNode(currentNode);
                if (!closedSet.contains(aNode)) { //If errors, make this more explicit
                    aNode.setHeuristic((float)findHeuristic(aNode, end));
                    aNode.setG(currentNode.getG() + aEdge.getCost());
                    aNode.setF(aNode.getHeuristic() + aNode.getG());
                }
            }

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


    public Iterator iterator()
    {
        return pathEdges.iterator();
    }
}
