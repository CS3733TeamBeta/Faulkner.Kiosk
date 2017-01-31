package Domain.Map;

import javafx.scene.image.Image;

import java.util.HashSet;

/**
 * Represents a node in a Map, connected to other nodes by NodeEdges
 */

public class MapNode
{
    int posX;
    int posY;

    Image node = null;

    Floor myFloor;
    HashSet<NodeEdge> edges;
}
