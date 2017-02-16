package Domain.Navigation;


import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import Domain.Map.*;
import Exceptions.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

/**
 * Direction tells you how to get from
 */
public class Guidance extends Path {

    //This is the direction that the user of the kiosk starts off facing.
    private static final int KIOSK_DIRECTION = 1;

    LinkedList<String> textDirections;

    public Guidance (MapNode start, MapNode end) throws PathFindingException {
            this(start, end, false);
            printTextDirections();
    }

    public Guidance (MapNode start, MapNode end, boolean vFlag) throws PathFindingException {
        //Make the path part
        super(start, end, vFlag);

        //Declare and initialize directions
        textDirections = new LinkedList<String>();
        createTextDirections(vFlag);

        if (vFlag) {
            printTextDirections();
        }
    }

    public void createTextDirections() {
        createTextDirections(false);
    }

    public void createTextDirections(boolean vFlag) {
        LinkedList<String> tempTextDirections = new LinkedList<String>();
        int prevDirection = KIOSK_DIRECTION;
        MapNode fromNode = new MapNode();
        MapNode toNode = new MapNode();

        int intersectionsPassed = 0;

        //Add the first node to the textual directions
        tempTextDirections.add("Start from " + pathNodes.get(0).getNodeID() + " facing North");

        for (int i = 0; i < this.pathNodes.size() - 1; i++) {

            //For each set of two nodes
            fromNode = pathNodes.get(i);
            toNode = pathNodes.get(i+1);


            if (vFlag) {
                System.out.println("");
                System.out.println("fromNode has ID " + fromNode.getNodeID());
                System.out.println("toNode has ID " + toNode.getNodeID());
            }

            //Figure out the direction that is taken between them.
            int currentDirection = Guidance.nodesToDirection(fromNode, toNode);

            if (vFlag) {
                System.out.println("Current direction is " + currentDirection);
                System.out.println("PrevDirection is " + prevDirection);
            }

            int changeInDirection;
            //If direction is not in an elevator
            if (currentDirection < 9) {
                //change in direction is the difference between directions
                changeInDirection = prevDirection - currentDirection;
                prevDirection = currentDirection;
            } else {
                //If you're on an elevator, your previous direction doesn't matter
                changeInDirection = currentDirection;
                //Presume the elevator passenger faces North
                prevDirection = 1;
            }

            //Change the directionChange into a textual string
            String directionChangeString = Guidance.directionChangeToString(changeInDirection, vFlag);

            if (directionChangeString.equals("Straight")) {
                System.out.println("Passing an possible intersection");
                if (fromNode.getEdges().size() > 3) {
                    System.out.println("Passing a definite intersection");
                    System.out.println("fromNode has size of edges: " + fromNode.getEdges().size());
                    intersectionsPassed++;
                }
                intersectionsPassed++;
            } else if (directionChangeString.equals("up") || directionChangeString.equals("down")) {
                if(intersectionsPassed == 0){
                    tempTextDirections.add("Take an elevator at your next intersection from " + fromNode.getNodeID() + " on floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                }
                else if(intersectionsPassed == 1){
                    tempTextDirections.add("After passing 1 intersection, take an elevator at " + fromNode.getNodeID() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                }
                else {
                    tempTextDirections.add("After passing " + intersectionsPassed + " intersections" + ", take an elevator" + " at " + fromNode.getNodeID() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                }
                intersectionsPassed = 0;
            } else if (!directionChangeString.equals("Straight")) {
                if(intersectionsPassed  == 0) {
                    tempTextDirections.add("Turn " + directionChangeString + " at your next intersection; ID: " + fromNode.getNodeID());
                }
                else if(intersectionsPassed == 1){
                    tempTextDirections.add("After passing 1 intersection, turn " + directionChangeString + " at " + fromNode.getNodeID());
                }
                else{
                    tempTextDirections.add("After passing " + intersectionsPassed + " intersections, turn " + directionChangeString + " at " + fromNode.getNodeID());
                }
                intersectionsPassed = 0;
            }
        }

        //Add the destination arrival string
        tempTextDirections.add("After passing " + intersectionsPassed + " intersections, arrive at your destination: node " + toNode.getNodeID());
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

    public void saveMapImage(Node aNode) {
        WritableImage image = aNode.snapshot(new SnapshotParameters(), null);

        // TODO: probably use a file chooser here
        File file = new File("directions.png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            // TODO: handle exception here
            System.out.println("EGADS!  We have an exception!");
        }
    }

    public static int nodesToDirection(MapNode fromNode, MapNode toNode) {
        return Guidance.nodesToDirection(fromNode, toNode, false);
    }

    public static String directionChangeToString(int changeInDirection) {
        return Guidance.directionChangeToString(changeInDirection, false);
    }

    public static String directionChangeToString(int changeInDirection, boolean vFlag) {
        //If a direction comes out as "Error", somethings wrong
        String stringDirection = "Error";
        switch (changeInDirection) {
            case -7:
                stringDirection = "slight left";
                break;
            case -6:
                stringDirection = "left";
                break;
            case -5:
                stringDirection = "hard left";
                break;
            case -4:
                stringDirection = "U-Turn";
                break;
            case -3:
                stringDirection = "hard right";
                break;
            case -2:
                stringDirection = "right";
                break;
            case -1:
                stringDirection = "slight right";
                break;
            case 0:
                stringDirection = "Straight";
                break;
            case 1:
                stringDirection = "slight right";
                break;
            case 2:
                stringDirection = "left";
                break;
            case 3:
                stringDirection = "hard left";
                break;
            case 4:
                stringDirection = "U-Turn";
                break;
            case 5:
                stringDirection = "hard right";
                break;
            case 6:
                stringDirection = "right";
                break;
            case 7:
                stringDirection = "slight right";
                break;
            case 9:
                stringDirection = "up";
                break;
            case 10:
                stringDirection = "down";
                break;
            default:
                //If it reaches this, something quite wrong
                stringDirection = "Big Error";
                break;
        }
        if (vFlag) {
            System.out.println("ChangeInDirection " + changeInDirection + " changed to " + stringDirection);
        }
        return stringDirection;
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
        if (fromNode.getMyFloor().getFloorNumber() < toNode.getMyFloor().getFloorNumber()) {
            direction = 9;
        } else if (fromNode.getMyFloor().getFloorNumber() > toNode.getMyFloor().getFloorNumber()) {
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

    public boolean sendEmailGuidance(String address, Node aNode) {
        saveMapImage(aNode);
        String subjectLine;
        String directionLine = "<H2>You have chosen to navigate from " + pathNodes.get(0).getNodeID() + " to " + pathNodes.get(pathNodes.size()-1).getNodeID() + ".</H2>" + "<H3>";
        subjectLine = "Your Directions are Enclosed - Faulkner Hospital";

        for(String s: textDirections) {
            directionLine += s;
            directionLine += "<br>";
        }
        directionLine += "</H3>";
        try {
            SendEmail e = new SendEmail(address, subjectLine, directionLine);
            return true;
        } catch(Exception e) {
            System.out.println("Threw an exception: " + e);
            return false;
        }
    }
}
