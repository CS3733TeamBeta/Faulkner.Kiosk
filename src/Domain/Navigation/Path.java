package Domain.Navigation;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Exceptions.PathFindingException;
import Exceptions.PathFindingInvalidPathException;
import Exceptions.PathFindingNoPathException;
import javafx.scene.Node;
import sun.awt.image.ImageWatched;

import java.util.Iterator;
import java.util.LinkedList;
//Testing

/**
 * Path is the path from one Destination to another
 */
public class Path{
    public PathfindingAlgorithm algorithm;

    LinkedList<NodeEdge> pathEdges = new LinkedList<>();
    LinkedList<MapNode> pathNodes = new LinkedList<>();

    public boolean vFlag;

    public Path(MapNode start, MapNode end, boolean vFlag, PathfindingAlgorithm algorithm) throws PathFindingException{
        algorithm.setVFlag(vFlag);
        this.vFlag = vFlag;
        algorithm.createPath(start, end);
        pathEdges = algorithm.getPathEdges();
        pathNodes = algorithm.getPathNodes();
    }


    public Path() {
        System.out.println("Path constuctor must be called with fields");
    }


    public Path(MapNode start, MapNode end) throws PathFindingException {
        this(start, end, false, new AStarAlgorithm());
    }

    public Path(MapNode start, MapNode end, boolean vFlag) throws PathFindingException {
        this(start, end, vFlag, new AStarAlgorithm());
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

    public static boolean isValidPath(LinkedList<MapNode> listNodes, LinkedList<NodeEdge> listEdges) {
        boolean isValid = true;
        for(int i = 1; i < listNodes.size() - 0; i++) {
            if ( !(listNodes.get(i-1).hasEdgeTo(listNodes.get(i))) ) {
                isValid = false;
            }
        }
        return isValid;
    }

    public boolean equals(Path p) {
        return ((this.pathEdges.equals(p.getPathEdges()) && (this.pathNodes.equals(p.getPathNodes()))));
    }

    /**
     * Manual constructor for a path
     * @param pathEdges
     * @param pathNodes
     */
    public Path (LinkedList<NodeEdge> pathEdges, LinkedList<MapNode> pathNodes) {
        this.pathEdges = pathEdges;
        this.pathNodes = pathNodes;
    }

    public LinkedList<NodeEdge> getPathEdges(){
        return this.pathEdges;
    }

    public LinkedList<MapNode> getPathNodes(){
        return this.pathNodes;
    }
}
