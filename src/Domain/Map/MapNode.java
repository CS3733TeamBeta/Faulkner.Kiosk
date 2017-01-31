package Domain.Map;

import java.util.HashSet;

/**
 * Represents a node in a Map, connected to other nodes by NodeEdges
 */

public class MapNode
{
    Floor myFloor;
    HashSet<NodeEdge> edges;
}
