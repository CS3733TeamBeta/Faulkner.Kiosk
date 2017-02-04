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

        boolean flagDone = false;

        LinkedList<MapNode> openSet = new LinkedList<MapNode>();
        LinkedList<MapNode> closedSet = new LinkedList<MapNode>();

        openSet.add(start);

        while (openSet.size() > 0 && !flagDone) {
            MapNode currentNode = popSmallest(openSet);

            //openSet.remove(0);
            closedSet.add(currentNode);

            if (currentNode.equals(end)) {
                //return the path from start to end
                LinkedList<NodeEdge> invertedList = new LinkedList<NodeEdge>();
                MapNode currentInPath = currentNode;
                int length = 0;
                while(currentInPath.getParent() != null){
                    invertedList.add(currentInPath.getParent());
                    currentInPath = currentInPath.getParent().getOtherNode(currentInPath);
                    length++;
                }

                //now invert the inverted list to get the path from start to finish:
                for(int i = 0; i < length; i++){
                    pathEdges.add(length - i - 1, invertedList.get(0));
                    invertedList.remove(0);
                }

                flagDone = true;

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
        //TODO: When done, set all nodes back to default values
    }

    public static double findHeuristic(MapNode currentNode, MapNode endNode){
        double currentNodeX = (double)currentNode.getPosX();
        double currentNodeY = (double)currentNode.getPosY();
        double endNodeX = (double)endNode.getPosX();
        double endNodeY = (double)endNode.getPosY();
        return Math.sqrt(Math.pow(endNodeY - currentNodeY, 2) + Math.pow(endNodeX - currentNodeX, 2));
    }

    public static MapNode popSmallest(LinkedList<MapNode> listOfNodes){
        MapNode smallestNode = new MapNode();
        for(int i = 0; i < listOfNodes.size(); i++){
            if(listOfNodes.get(i).getF() < smallestNode.getF()){
                smallestNode = listOfNodes.get(i);
            }
        }
        listOfNodes.remove(smallestNode);
        return smallestNode;

    }

    public Iterator iterator()
    {
        return pathEdges.iterator();
    }
}
