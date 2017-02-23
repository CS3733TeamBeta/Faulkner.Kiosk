package Domain.Navigation;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Exceptions.PathFindingException;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Pattop on 2/22/2017.
 */
public abstract class PathfindingAlgorithm {

    LinkedList<NodeEdge> pathEdges = new LinkedList<>();
    LinkedList<MapNode> pathNodes = new LinkedList<>();


    public LinkedList<NodeEdge> getPathEdges() {
        return pathEdges;
    }
    public LinkedList<MapNode> getPathNodes() { return pathNodes; }


    //Set to true for verbose debugging
    public boolean vFlag;


    public void setVFlag(boolean vFlag) {this.vFlag = vFlag;}

    static final double FLOOR_HEIGHT_CONSTANT = 1.812;

    public PathfindingAlgorithm() {
        this(false);
    }


    /**
     * Constructs itself as a path between the two given nodes.
     * If vFlag is true, prints verbose logs as it does so.
     * @param vFlag
     * @throws PathFindingException
     */
    public PathfindingAlgorithm(boolean vFlag) {
        this.vFlag = vFlag;
    }

    public abstract void createPath(MapNode start, MapNode end) throws PathFindingException;

    /**
     * Manual constructor for a path
     * @param pathEdges
     * @param pathNodes
     */
    public PathfindingAlgorithm (LinkedList<NodeEdge> pathEdges, LinkedList<MapNode> pathNodes) {
        this.pathEdges = pathEdges;
        this.pathNodes = pathNodes;
    }

    /**
     * Given an ending node, reconstructs the path taken to that node, given that that path has been populated with parents.
     * @param end The final node.
     */
    void reconstructPath(MapNode end) {
        //currentInPath is the node we are looking at now to reconstruct the path
        MapNode currentInPath = end;

        //While the current node HAS a parent, continue iterating.
        // This works because the only node without a parent in the path is the starting node.
        while (currentInPath.getParent() != null) {

            //Stick the parent at the beginning of the list
            //we order the array like this in order to invert the list of nodes
            //the path we construct via parent is naturally the path from the end to the start.
            pathEdges.add(0, currentInPath.getParent());

            //Then go to the next node in the path
            currentInPath = currentInPath.getParent().getOtherNode(currentInPath);
        }
    }


    /**
     * Given the starting and ending nodes, and knowing that pathEdges is already populated,
     * fill pathNodes from pathEdges
     * @param start
     * @param end
     */
    void nodesFromEdges(MapNode start, MapNode end) {
        MapNode prev = start;
        for (int i = 0; i < pathEdges.size(); i++){
            pathNodes.add(prev);
            prev = pathEdges.get(i).getOtherNode(prev);

        }
        pathNodes.add(end);

    }


    /**
     * Find the heuristic (aprox. distance) from currentNode to endNode using pythag. theorm from xpos and ypos, and
     * zpos figured out from floor and the floor height constant
     * @param currentNode
     * @param endNode
     * @return Approximate direct distance from currentNode to endNode
     */
    static double findHeuristic(MapNode currentNode, MapNode endNode){
        //calculate the distance from one node to another via geometry
        double currentNodeX = currentNode.getPosX();
        double currentNodeY = currentNode.getPosY();
        double currentNodeZ = ( (currentNode.getMyFloor().getFloorNumber()) * FLOOR_HEIGHT_CONSTANT);
        double endNodeX = endNode.getPosX();
        double endNodeY = endNode.getPosY();
        double endNodeZ = ( (endNode.getMyFloor().getFloorNumber()) * FLOOR_HEIGHT_CONSTANT);
        return Math.sqrt(Math.pow(endNodeX - currentNodeX, 2) + Math.pow(endNodeY - currentNodeY, 2) + Math.pow(endNodeZ - currentNodeZ, 2));
    }

    //Given a list of MapNodes, removes and returns the one with the smallest F value
    static MapNode popSmallest(LinkedList<MapNode> listOfNodes){

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

    //Prints out all edges in this path, only used for testing
    public void printPathEdges(){
        System.out.println("Printing Edges");
        for (NodeEdge e: this.pathEdges) {
            System.out.println("From " + Integer.toString(e.getSource().getType()) + "to " + Integer.toString(e.getTarget().getType()));
        }
    }

    //Prints out all nodes in this path, only used for testing
    public void printPathNodes(){
        System.out.println("Printing Nodes");
        for (MapNode n: this.pathNodes) {
            System.out.println("Node of id " + n.getNodeID());
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof Path) {
            return this.equals((Path) obj);
        } else {
            return false;
        }
    }

    public boolean equals(PathfindingAlgorithm that) {
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
