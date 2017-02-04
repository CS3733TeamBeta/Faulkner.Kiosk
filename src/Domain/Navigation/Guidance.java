package Domain.Navigation;


import java.util.LinkedList;

import Domain.Map.*;
import Exceptions.*;

/**
 * Direction tells you how to get from
 */
public class Guidance extends Path {

    LinkedList<String> textDirections;

    public Guidance (MapNode start, MapNode end) throws PathFindingException {
            this(start, end, false);
    }

    public Guidance (MapNode start, MapNode end, boolean flag) throws PathFindingException {
        super(start, end, flag);
        createTextualDirections();
    }

    public void createTextualDirections() {
        System.out.println("");
        System.out.println("Creating Textual Directions");
        System.out.println("");
        for (MapNode n: this.pathNodes) {
            System.out.println("Go to " + n.getNodeID());
        }
    }



    public LinkedList<String> getTextDirections()
    {
        return textDirections;
    }
}
