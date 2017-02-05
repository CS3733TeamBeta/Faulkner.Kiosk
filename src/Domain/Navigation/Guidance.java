package Domain.Navigation;


import java.util.LinkedList;
import java.util.List;

import Domain.Map.*;
import Exceptions.*;

/**
 * Direction tells you how to get from
 */
<<<<<<< HEAD
public class Guidance
{
    Path p;
    LinkedList<String> textDirections;

    Guidance (Destination start, Destination end) {
        this.p = new Path(start, end);
=======
public class Guidance extends Path {

    LinkedList<String> textDirections;

    public Guidance (Destination start, Destination end) throws PathFindingException {
            super(start, end);
>>>>>>> f849918ed5c6c50d1ecb03b1e8e5b899259c5ae9
    }

    public LinkedList<String> getTextDirections()
    {
        return textDirections;
    }
}
