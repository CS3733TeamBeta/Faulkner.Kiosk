package Domain.Navigation;

import Domain.Exception.PathFindingErrorException;
import Domain.Map.*;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Path is the path from one Destination to another
 */
public class Path implements Iterable
{

    LinkedList<NodeEdge> pathEdges;
    LinkedList<MapNode> pathNodes;

    public Path (LinkedList<NodeEdge> pathEdges, LinkedList<MapNode> pathNodes) {
        this.pathEdges = pathEdges;
        this.pathNodes = pathNodes;
    }

    public Path (MapNode start, MapNode end) throws PathFindingErrorException{

        pathEdges = new LinkedList<NodeEdge>();
        pathNodes = new LinkedList<MapNode>();

        //Have not found the end yet.
        boolean flagDone = false;

        //Initialize sets and globals
        LinkedList<MapNode> openSet = new LinkedList<MapNode>();
        LinkedList<MapNode> closedSet = new LinkedList<MapNode>();
        MapNode currentNode;

        //Start from start
        openSet.add(start);

        //While not everything is checked, and the end has not been found
        while (openSet.size() > 0 && !flagDone) {

            //Get the node with the smallest f-value in openset
            currentNode = popSmallest(openSet);

            //and add it to the closed set.
            closedSet.add(currentNode);

            //If it's the destination
            if (currentNode.equals(end)) {

                //Whoop-de-do, you found the path!

                //return the path from start to end
                MapNode currentInPath = currentNode;

                //While parents are still to be found.
                while(currentInPath.getParent() != null){

                    //Stick the parent at the beginning of the list
                    pathEdges.add(0, currentInPath.getParent());

                    //Then look for the parent's parent.
                    currentInPath = currentInPath.getParent().getOtherNode(currentInPath);
                }
                //Set flag to exit while
                flagDone = true;

            }

            //If you havn't found the end,
            //For every edge in the lowest-f currentNode, look at it's connected edges
            for (NodeEdge aEdge: currentNode.edges) {

                //and those edges' nodes.
                MapNode aNode = aEdge.getOtherNode(currentNode);

                //As long as you arn't currently pathing through that node
                if (!closedSet.contains(aNode)) {

                    //Calculate what the F value would be
                    float tentativeNewHeuristic = (float)findHeuristic(aNode, end);
                    float tentativeNewGValue = currentNode.getG() + aEdge.getCost();
                    float tentativeNewFValue = tentativeNewHeuristic + tentativeNewGValue;

                    //If the new F value is better
                    if(tentativeNewFValue < aNode.getF()) {

                        //set it as the F value.
                        aNode.setHeuristic(tentativeNewHeuristic);
                        aNode.setG(tentativeNewGValue);
                        aNode.setF(tentativeNewHeuristic + tentativeNewGValue);

                        //And set its parent
                        aNode.setParent(aEdge);
                    }

                    //If this node isn't in the open set either
                    if(!openSet.contains(aNode)) {
                        //Put it in the open set
                        openSet.add(aNode);
                    }

                }
            }


        }
        //Set all nodes in the open and closed set back to default values
        for (MapNode n: openSet) {
            n.resetTempValues();
        }
        for (MapNode n: closedSet) {
            n.resetTempValues();
        }

        //Determine pathNodes from pathEdges
        nodesFromEdges(start, end);

        //Print the edges and nodes for bugfixing
        printPathEdges();
        printPathNodes();

        if (!this.isValidPath()) {
            throw new PathFindingErrorException("Path invalid as generated");
        }

    }

    public LinkedList<NodeEdge> getPathEdges() {
        return pathEdges;
    }

    //Given the start and ending nodes, fill pathNodes from pathEdges
    public void nodesFromEdges(MapNode start, MapNode end) {
        MapNode prev = start;
        for (int i = 0; i < pathEdges.size(); i++){
            pathNodes.add(prev);
            prev = pathEdges.get(i).getOtherNode(prev);

        }
        pathNodes.add(end);

    }

    public boolean isValidPath() {
        boolean isValid = true;
        for(int i = 1; i < pathNodes.size() - 0; i++) {
            if ( !(pathNodes.get(i-1).hasEdgeTo(pathNodes.get(i))) ) {
                isValid = false;
            }

        }
        return isValid;
    }

    //Find the heuristic (aprox. distance) from currentNode to endNode
    public static double findHeuristic(MapNode currentNode, MapNode endNode){
        double currentNodeX = (double)currentNode.getPosX();
        double currentNodeY = (double)currentNode.getPosY();
        double endNodeX = (double)endNode.getPosX();
        double endNodeY = (double)endNode.getPosY();
        return Math.sqrt(Math.pow(endNodeY - currentNodeY, 2) + Math.pow(endNodeX - currentNodeX, 2));
    }

    //Given a list of MapNodes, removes and returns the one with the smallest F value
    public static MapNode popSmallest(LinkedList<MapNode> listOfNodes){

        //Seed smallestNode with first value
        MapNode smallestNode = listOfNodes.get(0);

        //For every node
        for(int i = 0; i < listOfNodes.size(); i++){
            //If f value is better than current smallest
            if(listOfNodes.get(i).getF() < smallestNode.getF()){
                //store this one as current smallest
                smallestNode = listOfNodes.get(i);
            }
        }
        //Remove smallest from list, and return it
        listOfNodes.remove(smallestNode);
        return smallestNode;

    }

    //Prints out all edges in this path
    public void printPathEdges(){
        System.out.println("Printing Edges");
        for (NodeEdge e: this.pathEdges) {
            System.out.println("From " + Integer.toString(e.getNodeA().getNodeID()) + "to " + Integer.toString(e.getNodeB().getNodeID()));
        }
    }

    //Prints out all nodes in this path
    public void printPathNodes(){
        System.out.println("Printing Nodes");
        for (MapNode n: this.pathNodes) {
            System.out.println("Node of id " + n.getNodeID());
        }
    }

    public Iterator iterator()
    {
        return pathEdges.iterator();
    }
}
