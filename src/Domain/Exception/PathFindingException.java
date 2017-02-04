package Domain.Exception;

import Domain.Map.*;

import java.util.LinkedList;

/**
 * Created by IanCJ on 2/4/2017.
 */
public class PathFindingException extends Exception {

    String errorMessage;
    LinkedList<MapNode> badPathNodes;
    LinkedList<NodeEdge> badPathEdge;

    public PathFindingException(String errorMessage, LinkedList<MapNode> badPathNodes, LinkedList<NodeEdge> badPathEdge) {
        this.errorMessage = errorMessage;
        this.badPathNodes = badPathNodes;
        this.badPathEdge = badPathEdge;
    }
}
