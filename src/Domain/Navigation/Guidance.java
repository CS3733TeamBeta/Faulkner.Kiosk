package Domain.Navigation;


import java.util.LinkedList;
import java.util.List;
import Domain.Map.*;
import

/**
 * Direction tells you how to get from
 */
public class Guidance extends Path
{
    Path p;
    LinkedList<String> textDirections;

    public Guidance (Destination start, Destination end) throws PathFindingErrorException {
            super(start, end);
        try {
            this.p = new Path(start, end);
        } catch (Exception e) {}
    }

    public List getTextDirections()
    {
        return textDirections;
    }
}
