package Domain.Navigation;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Exceptions.PathFindingException;
import Exceptions.PathFindingInvalidPathException;
import Exceptions.PathFindingNoPathException;

import java.util.LinkedList;

/**
 * Created by Pattop on 2/22/2017.
 */
public class AStarAlgorithm extends PathfindingAlgorithm {


    /**
     * Fills this path's field with the path genereated from start to end using Astar, printing out lots of stuff if vFlag is true
     * @param start
     * @param end
     * @throws PathFindingException
     */
    public void createPath(MapNode start, MapNode end) throws PathFindingException {
        pathEdges = new LinkedList<NodeEdge>();
        pathNodes = new LinkedList<MapNode>();

        //Have not found the end yet.
        boolean flagDone = false;

        //Initialize sets and globals
        //openset is the list of nodes to be visited
        LinkedList<MapNode> openSet = new LinkedList<MapNode>();
        //closedset is the list of nodes already visited
        LinkedList<MapNode> closedSet = new LinkedList<MapNode>();
        //currentnode is the node we are inspecting now
        MapNode currentNode;

        //first add the starting node to the openset
        openSet.add(start);

        //Iterate through the list until either we run out of nodes to explore or we get to the exit
        while (openSet.size() > 0 && !flagDone) {

            //Get the node with the smallest best combined heuristic in the openset
            currentNode = popSmallest(openSet);

            //Add the node into the closed set to avoid errors from self-references
            closedSet.add(currentNode);

            //First check if the current node is the goal we are looking for
            if (currentNode.equals(end)) {
                reconstructPath(end);
                //Set the flag to exit the algorithm
                flagDone = true;
            }

            //If you haven't found the end,
            //For every edge in the currentNode, look at it's connected edges and add them to the openSet if they
            //ae not in either the openSet or the closedSet
            for (NodeEdge aEdge: currentNode.edges) {

                //and those edges' nodes.
                MapNode aNode = aEdge.getOtherNode(currentNode);

                //As long as you have not already visited the node, recalculate its heuristic values
                if (!closedSet.contains(aNode)) {

                    //Calculate what the F value would be
                    //the heuristic is the geometric distance from the next node to the end point
                    double tentativeNewHeuristic = findHeuristic(aNode, end);
                    //the G value is the distance we have traveled on this current path
                    double tentativeNewGValue = currentNode.getG() + aEdge.getCost();
                    //the combined heuristic is the sum of the projected distance and the travelled distance
                    //to the end node
                    double tentativeNewFValue = tentativeNewHeuristic + tentativeNewGValue;

                    //If the new F value is better than the one we have for the node, we have found a better path
                    //to that node. replace that node's values with this better parent and heuristics
                    if(tentativeNewFValue < aNode.getF()) {

                        //set the heuristic values.
                        aNode.setHeuristic(tentativeNewHeuristic);
                        aNode.setG(tentativeNewGValue);
                        aNode.setF(tentativeNewHeuristic + tentativeNewGValue);

                        //And set its parent to this node
                        aNode.setParent(aEdge);
                    }

                    //If this node isn't in the open set either, add it to the openset so we visit it later
                    if(!openSet.contains(aNode)) {
                        openSet.add(aNode);
                    }

                }
            }

        }

        //Set all nodes in the open and closed set back to default values so we can iterate through the list again
        //and find another path
        for (MapNode n: openSet) {
            n.resetTempValues();
        }
        for (MapNode n: closedSet) {
            n.resetTempValues();
        }



        //Determine pathNodes from pathEdges
        nodesFromEdges(start, end);

        //Print the edges and nodes for bugfixing
        if (vFlag) {
            printPathEdges();
            printPathNodes();
        }

        //If the loop exited without flagDone, it must have searched all nodes and found nothing
        if (!flagDone) {
            throw new PathFindingNoPathException("No valid path found", this.pathNodes, this.pathEdges);
        }

        if (!Path.isValidPath(this.getPathNodes(), this.getPathEdges())) {
            throw new PathFindingInvalidPathException("Path generated is invalid", this.pathNodes, this.pathEdges);
        }

    }
}
