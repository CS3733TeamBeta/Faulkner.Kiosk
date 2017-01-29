package Domain.Navigation;

import Domain.Map.Destination;
import Domain.Map.Node;
import Domain.Map.NodeEdge;

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

    }

    @Override
    public Iterator iterator()
    {
        return pathEdges.iterator();
    }
}
