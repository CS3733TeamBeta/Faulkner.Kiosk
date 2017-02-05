package Domain.Navigation;

import Domain.Map.*;

import java.util.Iterator;
import java.util.LinkedList;
import Exceptions.*;


/**
 * Path is the path from one Destination to another
 */
public class Path implements Iterable {

    LinkedList<NodeEdge> pathEdges;
    LinkedList<MapNode> pathNodes;

    //Set to true for verbose debugging
    private boolean devFlag;

    private static final double FLOOR_HEIGHT_CONSTANT = 1.812;

    public Path() {}

    public Path(MapNode start, MapNode end) throws PathFindingException {
        this(start, end, false);
    }


    public Path(MapNode start, MapNode end, boolean devFlag) throws PathFindingException{

        this.devFlag = devFlag;

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
                    double tentativeNewHeuristic = findHeuristic(aNode, end);
                    double tentativeNewGValue = currentNode.getG() + aEdge.getCost();
                    double tentativeNewFValue = tentativeNewHeuristic + tentativeNewGValue;

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

        //If the loop exited without flagDone, it must have searched all nodes and found nothing
        if (!flagDone) {
            throw new PathFindingNoPathException("No valid path found", this.pathNodes, this.pathEdges);
        }

        //Print the edges and nodes for bugfixing
        if (devFlag) {
            printPathEdges();
            printPathNodes();
        }

        if (!this.isValidPath()) {
            throw new PathFindingInvalidPathException("Path generated is invalid", this.pathNodes, this.pathEdges);
        }


    }

    public Path (LinkedList<NodeEdge> pathEdges, LinkedList<MapNode> pathNodes) {
        this.pathEdges = pathEdges;
        this.pathNodes = pathNodes;
    }

    public LinkedList<NodeEdge> getPathEdges() {
        return pathEdges;
    }
    public LinkedList<MapNode> getPathNodes() { return pathNodes; }

    //Given the start and ending nodes, fill pathNodes from pathEdges
    public void nodesFromEdges(MapNode start, MapNode end) {
        MapNode prev = start;
        for (int i = 0; i < pathEdges.size(); i++){
            pathNodes.add(prev);
            prev = pathEdges.get(i).getOtherNode(prev);

        }
        pathNodes.add(end);

    }

    //Returns true if for every node in pathNodes, it has a path to the next node.
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
        double currentNodeX = currentNode.getPosX();
        double currentNodeY = currentNode.getPosY();
        double currentNodeZ = ( (currentNode.getMyFloor().getFloorNumber()) * FLOOR_HEIGHT_CONSTANT);
        double endNodeX = endNode.getPosX();
        double endNodeY = endNode.getPosY();
        double endNodeZ = ( (endNode.getMyFloor().getFloorNumber()) * FLOOR_HEIGHT_CONSTANT);
        return Math.sqrt(Math.pow(endNodeX - currentNodeX, 2) + Math.pow(endNodeY - currentNodeY, 2) + Math.pow(endNodeZ - currentNodeZ, 2));
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


    //

    public boolean equals(Object obj) {
        if (obj instanceof Path) {
            return this.equals((Path) obj);
        } else {
            return false;
        }
    }

    public boolean equals(Path that) {
        LinkedList<MapNode> thisNodes = this.pathNodes;
        LinkedList<NodeEdge> thisEdges = this.pathEdges;
        LinkedList<MapNode> thatNodes = that.getPathNodes();
        LinkedList<NodeEdge> thatEdges = that.getPathEdges();
        if ((thisNodes.size() != thatNodes.size()) || (thisEdges.size() != thatEdges.size())) {
            return false;
        }
        boolean areEqual = true;
        for (int i = 0; i < thisNodes.size(); i++) {
            if (!(thisNodes.get(i).equals(thatNodes.get(i)))) {
                areEqual = false;
            }
        }
        for (int i = 0; i < thisEdges.size(); i++) {
            if (!(thisEdges.get(i).equals(thatEdges.get(i)))) {
                areEqual = false;
            }
        }
        return areEqual;
    }


    public int hashCode() {
        final int primeNum = 31;
        int totalHash = 1;
        for (MapNode n: pathNodes) {
            totalHash = totalHash * primeNum + n.hashCode();
        }
        return totalHash;
    }

    public Iterator iterator() {return pathEdges.iterator();}
}
