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
    PathfindingAlgorithm algorithm;

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
