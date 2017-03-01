package main.Application.Exceptions;

import main.Map.Entity.MapNode;
import main.Map.Entity.NodeEdge;

import java.util.LinkedList;

/**
 * Created by IanCJ on 2/4/2017.
 */
public class PathFindingNoPathException  extends PathFindingException {

    public PathFindingNoPathException(String errorMessage, LinkedList<MapNode> badPathNodes, LinkedList<NodeEdge> badPathEdge) {
        super(errorMessage, badPathNodes, badPathEdge);
    }
}
