package Exceptions;

import Entity.Map.MapNode;
import Entity.Map.NodeEdge;

import java.util.LinkedList;

/**
 * Created by IanCJ on 2/4/2017.
 */
public class PathFindingNoPathException  extends PathFindingException {

    public PathFindingNoPathException(String errorMessage, LinkedList<MapNode> badPathNodes, LinkedList<NodeEdge> badPathEdge) {
        super(errorMessage, badPathNodes, badPathEdge);
    }
}
