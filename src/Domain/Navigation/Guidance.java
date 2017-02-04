package Domain.Navigation;


import java.util.LinkedList;

import Domain.Map.*;
import Exceptions.*;

/**
 * Direction tells you how to get from
 */
public class Guidance extends Path {

    LinkedList<String> textDirections;

    public Guidance (Destination start, Destination end) throws PathFindingException {
            super(start, end);
    }

    public LinkedList<String> getTextDirections()
    {
        return textDirections;
    }
}
