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
        textDirections = new LinkedList<String>();
        createTextDirections();

        if (flag) {
            printTextDirections();
        }
    }

    public void createTextDirections() {
        for (MapNode n: this.pathNodes) {
            String s = "Go to " + n.getNodeID();
            textDirections.add(s);
        }
    }

    public void printTextDirections() {
        System.out.println("");
        System.out.println("Printing Textual Directions");
        System.out.println("");
        for (String s: textDirections) {
            System.out.println(s);
        }
    }

    public LinkedList<String> getTextDirections()
    {
        return textDirections;
    }
}
