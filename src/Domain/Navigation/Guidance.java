package Domain.Navigation;

import java.lang.Math;
import java.util.LinkedList;

import Domain.Map.*;
import Exceptions.*;

/**
 * Direction tells you how to get from
 */
public class Guidance extends Path {

    //This is the direction that the user of the kiosk starts off facing.
    private static final int KIOSK_DIRECTION = 1;

    LinkedList<String> textDirections;

    public Guidance (MapNode start, MapNode end) throws PathFindingException {
            this(start, end, false);
    }

    public Guidance (MapNode start, MapNode end, boolean flag) throws PathFindingException {
        //Make the path part
        super(start, end, flag);

        //Declare and initialize directions
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
        LinkedList<String> tempTextDirections = new LinkedList<String>();
        int prevDirection = KIOSK_DIRECTION;
        for (int i = 0; i < this.pathNodes.size() - 1; i++) {

            MapNode fromNode = pathNodes.get(i);
            MapNode toNode = pathNodes.get(i+1);
            if (vFlag) {
                System.out.println("fromNode has ID " + fromNode.getNodeID());
                System.out.println("toNode has ID " + toNode.getNodeID());
            }

            int currentDirection = Guidance.nodesToDirection(fromNode, toNode);

            if (vFlag) {
                System.out.println("Current direction is " + currentDirection);
                System.out.println("");
            }
            int changeInDirection = prevDirection - currentDirection;
            tempTextDirections.add("Go " + Guidance.numToDirection(currentDirection) + " toward node " + toNode.getNodeID());

            prevDirection = currentDirection;
        }
        this.textDirections = tempTextDirections;
    }

    public void printTextDirections() {
        System.out.println("");
        System.out.println("Printing Textual Directions");
        System.out.println("");
        for (String s: textDirections) {
            System.out.println(s);
        }
    }

    public static int nodesToDirection(MapNode fromNode, MapNode toNode) {
        return Guidance.nodesToDirection(fromNode, toNode, false);
    }

    //Takes two nodes, and returns an int representing the angle made by the edge between them
    public static int nodesToDirection(MapNode fromNode, MapNode toNode, boolean vFlag) {

        //Initialize and set the angle between the two nodes, regardless of floors
        double angle;
        angle = Math.toDegrees(Math.atan2(toNode.getPosX() - fromNode.getPosX(), toNode.getPosY() - fromNode.getPosY()));

        if (vFlag) {
            System.out.println("Angle is " + angle);
        }

        //N, NE, E, SE, S, SW, W, NW, Up, down, are equal to
        //1,  2, 3,  4, 5,  6, 7,  8,  9,   10,
        int direction = 0;
        if (fromNode.getMyFloor().getFloorNumber() > toNode.getMyFloor().getFloorNumber()) {
            direction = 9;
        } else if (fromNode.getMyFloor().getFloorNumber() < toNode.getMyFloor().getFloorNumber()) {
            direction = 10;
        } else if (angle > -22.5 && angle <= 22.5) {
            direction = 1;
        } else if (angle > 22.5 && angle <= 67.5) {
            direction = 2;
        } else if (angle > 67.5 && angle <= 112.5) {
            direction = 3;
        } else if (angle > 112.5 && angle <= 157.5) {
            direction = 4;
        } else if (angle > 157.5 || angle <= -157.5) {
            direction = 5;
        } else if (angle > -157.5 && angle <= -112.5) {
            direction = 6;
        } else if (angle > -112.5 && angle <= -67.5) {
            direction = 7;
        } else if (angle > -67.5 && angle <= -22.5) {
            direction = 8;
        }

        return direction;
    }

    public static String numToDirection(int num) {
        String textDirection;
        switch (num) {
            case 0:
                textDirection = "Error";
                break;
            case 1:
                textDirection = "North";
                break;
            case 2:
                textDirection = "NorthEast";
                break;
            case 3:
                textDirection = "East";
                break;
            case 4:
                textDirection = "SouthEast";
                break;
            case 5:
                textDirection = "South";
                break;
            case 6:
                textDirection = "SouthWest";
                break;
            case 7:
                textDirection = "West";
                break;
            case 8:
                textDirection = "NorthWest";
                break;
            case 9:
                textDirection = "Up";
                break;
            case 10:
                textDirection = "Down";
                break;
            default:
                textDirection = "Big Error";
                break;
        }
        return textDirection;
    }

    public LinkedList<String> getTextDirections()
    {
        return textDirections;
    }
}
