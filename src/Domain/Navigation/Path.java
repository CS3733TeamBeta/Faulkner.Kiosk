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

    public Path (Destination start, Destination end)
    {
        //@TODO: calculate path with A*
    }

    public Iterator iterator()
    {
        return pathEdges.iterator();
    }
}
