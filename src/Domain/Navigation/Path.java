package Domain.Navigation;

import Domain.Map.*;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Path is the path from one Destination to another
 */
public class Path implements Iterable
{
    LinkedList<NodeEdge> pathEdges;

    public Path (Destination start, Destination end) {

        LinkedList<MapNode> openSet = new LinkedList<MapNode>();
        LinkedList<MapNode> closedSet = new LinkedList<MapNode>();

        openSet.add(start);


        //@TODO: calculate path with A*

        //When done, set all nodes back to default values
    }

    public Iterator iterator()
    {
        return pathEdges.iterator();
    }
}
