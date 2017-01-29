package Domain.Navigation;


import java.util.LinkedList;
import java.util.List;
import Domain.Map.*;

/**
 * Direction tells you how to get from
 */
public class Guidance
{
    Path p;
    LinkedList<String> textDirections;

    Guidance (Destination start, Destination end) {
        this.p = new Path(start, end);
    }

    public List getTextDirections()
    {
        return textDirections;
    }
}
