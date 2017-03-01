package main.Application.Exceptions;

import main.Map.Entity.MapNode;
import main.Map.Entity.NodeEdge;

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

    public String getErrorMessage() {
        return errorMessage;
    }
}
