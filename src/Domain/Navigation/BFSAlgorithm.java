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
public class BFSAlgorithm extends PathfindingAlgorithm {
    /**
     * Fills this Path's fields with the path generated from start to end using breadth-first search, printing out stuff if vFlag is true.
     * @param start
     * @param end
     * @throws PathFindingException
     */
    public void createPath(MapNode start, MapNode end) throws PathFindingException {

        pathEdges = new LinkedList<NodeEdge>();
        pathNodes = new LinkedList<MapNode>();

        LinkedList<MapNode> openSet = new LinkedList<>();
        openSet.addFirst(start);
        boolean flagDone = false;
        LinkedList<MapNode> visitedNodes = new LinkedList<>();

        while(openSet.size() > 0 && !flagDone) {
            MapNode newNode = openSet.pop();
            visitedNodes.add(newNode);

            for (NodeEdge e : newNode.getEdges()) {
                MapNode neighbor = e.getOtherNode(newNode);
                if (!openSet.contains(neighbor) && !visitedNodes.contains(neighbor)) {
                    openSet.add(neighbor);
                    neighbor.setParent(e);
                }
            }

            if (newNode.equals(end)) {
                reconstructPath(end);
                //Set the flag to exit the algorithm
                flagDone = true;

            }
        }
        //Set all nodes in the open and closed set back to default values so we can iterate through the list again
        //and find another path
        for (MapNode n: visitedNodes) {
            n.resetTempValues();
        }
        for (MapNode n: openSet) {
            n.resetTempValues();
        }

        //Print the edges and nodes for bugfixing
        if (vFlag) {
            printPathEdges();
            printPathNodes();
        }

        //Determine pathNodes from pathEdges
        nodesFromEdges(start, end);
        //If the loop exited without flagDone, it must have searched all nodes and found nothing
        if (!flagDone) {
            throw new PathFindingNoPathException("No valid path found", this.pathNodes, this.pathEdges);
        }

        if (!Path.isValidPath(this.getPathNodes(), this.getPathEdges())) {
            throw new PathFindingInvalidPathException("Path generated is invalid", this.pathNodes, this.pathEdges);
        }
    }
}
