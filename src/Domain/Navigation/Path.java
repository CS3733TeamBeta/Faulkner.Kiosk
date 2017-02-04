package Domain.Navigation;

import Domain.Map.*;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Path is the path from one Destination to another
 */
public class Path implements Iterable
{
    public LinkedList<NodeEdge> getPathEdges() {
        return pathEdges;
    }

    LinkedList<NodeEdge> pathEdges;

    public Path (MapNode start, MapNode end) {
        boolean flagDone = false;

        LinkedList<MapNode> openSet = new LinkedList<MapNode>();
        LinkedList<MapNode> closedSet = new LinkedList<MapNode>();

        openSet.add(start);

        MapNode currentNode;

        while (openSet.size() > 0 && !flagDone) {
            //System.out.println("Open set is size: " + Integer.toString(openSet.size()));
            currentNode = popSmallest(openSet);

            //System.out.println(currentNode.getNodeID());

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
                /*
                //now invert the inverted list to get the path from start to finish:
                for(int i = 0; i < length; i++){
                    pathEdges.add(length - i - 1, invertedList.get(0));
                    invertedList.remove(0);
                } */
                pathEdges = invertedList;

                flagDone = true;

            }

            for (NodeEdge aEdge: currentNode.edges) {
                //System.out.println("From " + Integer.toString(aEdge.getNodeA().getNodeID()) + "to " + Integer.toString(aEdge.getNodeB().getNodeID()));
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

                    if(!openSet.contains(aNode) && !closedSet.contains(aNode)) {
                        openSet.add(aNode);
                    }

                }
            }
            //sort the list


        }
        //TODO: When done, set all nodes back to default values
        printPath();

    }

    public static double findHeuristic(MapNode currentNode, MapNode endNode){
        double currentNodeX = (double)currentNode.getPosX();
        double currentNodeY = (double)currentNode.getPosY();
        double endNodeX = (double)endNode.getPosX();
        double endNodeY = (double)endNode.getPosY();
        return Math.sqrt(Math.pow(endNodeY - currentNodeY, 2) + Math.pow(endNodeX - currentNodeX, 2));
    }

    public static MapNode popSmallest(LinkedList<MapNode> listOfNodes){
        //System.out.println(listOfNodes.size());
        MapNode smallestNode = listOfNodes.get(0);
        int indexSmallest = 0;
        for(int i = 0; i < listOfNodes.size(); i++){
            if(listOfNodes.get(i).getF() < smallestNode.getF()){
                indexSmallest = i;
                smallestNode = listOfNodes.get(i);
            }
        }
        listOfNodes.remove(smallestNode);
        return smallestNode;

    }

    public void printPath(){
        for (NodeEdge e: this.pathEdges) {
            System.out.println("From " + Integer.toString(e.getNodeA().getNodeID()) + "to " + Integer.toString(e.getNodeB().getNodeID()));
        }
    }

    public Iterator iterator()
    {
        return pathEdges.iterator();
    }
}
