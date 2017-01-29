package Domain.Navigation;


import java.util.LinkedList;
import java.util.List;

/**
 * Direction tells you how to get from
 */
public class Guidance
{
    Path p;
    LinkedList<String> textDirections;

    public List getTextDirections()
    {
        return textDirections;
    }
}
