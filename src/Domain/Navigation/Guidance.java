package Domain.Navigation;

import java.lang.Math;
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
        createTextDirections(false);
    }

    public void createTextDirections(boolean vFlag) {
        LinkedList<String> empDir = new LinkedList<String>();
        for (int i = 0; i < this.pathNodes.size() - 1; i++) {

            MapNode fromNode = pathNodes.get(i);
            MapNode toNode = pathNodes.get(i+1);
            if (vFlag) {
                System.out.println("fromNode has ID " + fromNode.getNodeID());
                System.out.println("toNode has ID " + toNode.getNodeID());
            }
            double angle;
            angle = Math.toDegrees(Math.atan2(toNode.getPosX() - fromNode.getPosX(), toNode.getPosY() - fromNode.getPosY()));
            if (vFlag) {
                System.out.println("Angle is " + angle);
            }
            String direction = "Error";
            if (fromNode.getMyFloor().getFloorNumber() > toNode.getMyFloor().getFloorNumber()) {
                direction = "Up";
            } else if (fromNode.getMyFloor().getFloorNumber() < toNode.getMyFloor().getFloorNumber()) {
                direction = "Down";
            } else if (angle > -22.5 && angle <= 22.5) {
                direction = "North";
            } else if (angle > 22.5 && angle <= 67.5) {
                direction = "NorthEast";
            } else if (angle > 67.5 && angle <= 112.5) {
                direction = "East";
            } else if (angle > 112.5 && angle <= 157.5) {
                direction = "SouthEast";
            } else if (angle > 157.5 || angle <= -157.5) {
                direction = "South";
            } else if (angle > -157.5 && angle <= -112.5) {
                direction = "SouthWest";
            } else if (angle > -112.5 && angle <= -67.5) {
                direction = "West";
            } else if (angle > -67.5 && angle <= -22.5) {
                direction = "NorthWest";
            }
            if (vFlag) {
                System.out.println("Direction is " + direction);
                System.out.println("");
            }

            empDir.add("Go " + direction + " toward node " + toNode.getNodeID());
        }
        this.textDirections = empDir;
    }
/*
    public LinkedList<String> getEmpDirections() {
        return getEmpDirections(false);
    }

    public LinkedList<String> getEmpDirections(boolean vFlag) {
        LinkedList<String> empDir = new LinkedList<String>();
        for (int i = 0; i < this.pathNodes.size() - 1; i++) {

            MapNode fromNode = pathNodes.get(i);
            MapNode toNode = pathNodes.get(i+1);
            if (vFlag) {
                System.out.println("fromNode has ID " + fromNode.getNodeID());
                System.out.println("toNode has ID " + toNode.getNodeID());
            }
            double angle;
            angle = Math.toDegrees(Math.atan2(toNode.getPosX() - fromNode.getPosX(), toNode.getPosY() - fromNode.getPosY()));
            if (vFlag) {
                System.out.println("Angle is " + angle);
            }
            String direction = "Error";
            if (fromNode.getMyFloor().getFloorNumber() > toNode.getMyFloor().getFloorNumber()) {
                direction = "Up";
            } else if (fromNode.getMyFloor().getFloorNumber() < toNode.getMyFloor().getFloorNumber()) {
                direction = "Down";
            } else if (angle > -22.5 && angle <= 22.5) {
                direction = "North";
            } else if (angle > 22.5 && angle <= 67.5) {
                direction = "NorthEast";
            } else if (angle > 67.5 && angle <= 112.5) {
                direction = "East";
            } else if (angle > 112.5 && angle <= 157.5) {
                direction = "SouthEast";
            } else if (angle > 157.5 || angle <= -157.5) {
                direction = "South";
            } else if (angle > -157.5 && angle <= -112.5) {
                direction = "SouthWest";
            } else if (angle > -112.5 && angle <= -67.5) {
                direction = "West";
            } else if (angle > -67.5 && angle <= -22.5) {
                direction = "NorthWest";
            }
            if (vFlag) {
                System.out.println("Direction is " + direction);
                System.out.println("");
            }

            empDir.add("Go " + direction + " toward node " + toNode.getNodeID());
        }
        return empDir;
    }
*/
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
