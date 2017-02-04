package Domain.Exception;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;

import java.util.LinkedList;

/**
 * Created by IanCJ on 2/4/2017.
 */
public class PathFindingInvalidPathException extends PathFindingException {

    public PathFindingInvalidPathException(String errorMessage, LinkedList<MapNode> badPathNodes, LinkedList<NodeEdge> badPathEdge) {
        super(errorMessage, badPathNodes, badPathEdge);
    }
}
