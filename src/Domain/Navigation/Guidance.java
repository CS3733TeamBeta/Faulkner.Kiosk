package Domain.Navigation;


import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.Point;

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
    private int kioskDirection = 3;
    private int scaleFactor = 1;
    private int boarderSize = 100;

    BufferedImage nodeImg = null;
    BufferedImage bathImg= null;
    BufferedImage docImg = null;
    BufferedImage elevatorImg = null;
    BufferedImage foodImg = null;
    BufferedImage infoImg = null;
    BufferedImage storeImg = null;

    LinkedList<DirectionStep> textDirections;

    public void setImages() {
        try {
            nodeImg = ImageIO.read(new File("src/View/Admin/MapBuilder/blank2.png"));
            bathImg = ImageIO.read(new File("src/View/Admin/MapBuilder/bathroom.png"));
            docImg = ImageIO.read(new File("src/View/Admin/MapBuilder/doctor.png"));
            elevatorImg = ImageIO.read(new File("src/View/Admin/MapBuilder/elevator.png"));
            foodImg = ImageIO.read(new File("src/View/Admin/MapBuilder/food.png"));
            infoImg = ImageIO.read(new File("src/View/Admin/MapBuilder/info.png"));
            storeImg = ImageIO.read(new File("src/View/Admin/MapBuilder/store.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Guidance (MapNode start, MapNode end) throws PathFindingException {
            this(start, end, false);
            setImages();
            printTextDirections();
    }

    public Guidance (MapNode start, MapNode end, boolean vFlag) throws PathFindingException {
        //Make the path part
        super(start, end, vFlag);
        setImages();

        //Declare and initialize directions
        textDirections = new LinkedList<DirectionStep>();
        TextDirectionsCreator tdc = new TextDirectionsCreator(pathNodes, pathEdges, kioskDirection, vFlag);
        textDirections = tdc.getDirectionSteps();

        if (vFlag) {
            printTextDirections();
        }
    }

    public Guidance (MapNode start, MapNode end, String kioskInputDirection) throws PathFindingException{
        super(start, end, false);
        setImages();

        kioskDirection = Guidance.directionToNum(kioskInputDirection);

        textDirections = new LinkedList<DirectionStep>();
        TextDirectionsCreator tdc = new TextDirectionsCreator(pathNodes, pathEdges, kioskDirection, false);
        textDirections = tdc.getDirectionSteps();
        //createTextDirections(false);


    }

    public LinkedList<DirectionStep> getSteps()
    {
        return textDirections;
    }

    public void createTextDirections() {
        TextDirectionsCreator tdc = new TextDirectionsCreator(pathNodes, pathEdges, kioskDirection, false);
        textDirections = tdc.getDirectionSteps();
        //createTextDirections(false);
    }

    private void createTextDirections(boolean vFlag) {
        LinkedList<String> tempTextDirections = new LinkedList<String>();
        LinkedList<MapNode> tempMapNodes = new LinkedList<>();
        int prevDirection = kioskDirection;
        MapNode fromNode = new MapNode();
        MapNode toNode = new MapNode();

        int intersectionsPassed = 0;

        //Add the first node to the textual directions
        if (vFlag) {
            tempTextDirections.add("Start at the Kiosk. (Node " + pathNodes.get(0).getNodeID() + ")");
        } else {
            tempTextDirections.add("Start at the Kiosk, facing " + numToDirection(kioskDirection));
        }

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
                tempMapNodes.add(fromNode);
                intersectionsPassed++;
            } else if (!directionChangeString.equals("Straight") && (!directionChangeString.equals("up")) && (!directionChangeString.equals("down"))) {
                if(intersectionsPassed  == 0) {
                    if (vFlag) {
                        tempTextDirections.add("Turn " + directionChangeString + " at the next intersection; ID: " + fromNode.getNodeID());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirections.add("Turn " + directionChangeString + " at the next intersection.");
                        tempMapNodes.add(fromNode);

                    }
                }
                else if(intersectionsPassed == 1){
                    if (vFlag) {
                        tempTextDirections.add("After passing 1 intersection, turn " + directionChangeString + " at " + fromNode.getNodeID());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirections.add("After passing 1 intersection, turn " + directionChangeString + ".");
                        tempMapNodes.add(fromNode);
                    }
                }
                else{
                    if (vFlag) {
                        tempTextDirections.add("After passing " + intersectionsPassed + " intersections, turn " + directionChangeString + " at " + fromNode.getNodeID());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirections.add("After passing " + intersectionsPassed + " intersections, turn " + directionChangeString + ".");
                        tempMapNodes.add(fromNode);

                    }
                }

                intersectionsPassed = 0;
            } else if (directionChangeString.equals("up") || directionChangeString.equals("down")) {
                if(intersectionsPassed == 0){
                    if (vFlag) {
                        tempTextDirections.add("Take an elevator at the next intersection from " + fromNode.getNodeID() + " on floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirections.add("Take an elevator at the next intersection from floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    }
                }
                else if(intersectionsPassed == 1){
                    if (vFlag) {
                        tempTextDirections.add("After passing 1 intersection, take an elevator at " + fromNode.getNodeID() + " on floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirections.add("After passing 1 intersection, take an elevator from floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    }
                }
                else {
                    if (vFlag) {
                        tempTextDirections.add("After passing " + intersectionsPassed + " intersections" + ", take the elevator" + " at " + fromNode.getNodeID() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirections.add("After passing " + intersectionsPassed + " intersections" + ", take the elevator " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    }
                }

                System.out.println("Adding a direction step at 185");
                this.textDirections.addLast(new DirectionStep(fromNode.getMyFloor(), tempTextDirections, tempMapNodes));
                //this.textDirections.getLast().setDirections(tempTextDirections);
                //this.textDirections.getLast().setNodesForThisFloor(tempMapNodes);
                tempTextDirections = new LinkedList<>();
                tempMapNodes = new LinkedList<>();
                intersectionsPassed = 0;
            }
        }

        //Add the destination arrival string
        if (intersectionsPassed >= 2) {
            if (vFlag) {
                tempTextDirections.add("After passing " + intersectionsPassed + " intersections, arrive at your destination: node " + toNode.getNodeID());
            } else {
                tempTextDirections.add("After passing " + intersectionsPassed + " intersections, arrive at your destination.");
            }
        } else if (intersectionsPassed == 1) {
            if (vFlag) {
                tempTextDirections.add("After passing 1 intersection, arrive at your destination: node " + toNode.getNodeID());
            } else {
                tempTextDirections.add("After passing 1 intersection, arrive at your destination.");
            }
        } else {
            if (vFlag) {
                tempTextDirections.add("Arrive at your destination: node " + toNode.getNodeID());
            } else {
                tempTextDirections.add("Arrive at your destination.");
            }
        }
        tempMapNodes.add(toNode);
        System.out.println("Adding step to textdirections");
        this.textDirections.addLast(new DirectionStep(fromNode.getMyFloor(), tempTextDirections, tempMapNodes));
        //this.textDirections.getLast().setDirections(tempTextDirections);
        //this.textDirections.getLast().setNodesForThisFloor(tempMapNodes);
        //tempTextDirections.clear();
        //tempMapNodes.clear();

    }

    public void printTextDirections() {
        System.out.println("");
        System.out.println("Printing Textual Directions");
        System.out.println("");
        for (DirectionStep step: textDirections) {
            System.out.println("Printing a step: ");
            for(String s : step.getDirections()) {
                System.out.println(s);
            }
            /*
            for (MapNode n: step.nodesForThisFloor){
                System.out.println("X is " + n.getPosX() + ", Y is " + n.getPosY());
            }
            */
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
                stringDirection = "slight right2-new";
                break;
            case -6:
                stringDirection = "right-new";
                break;
            case -5:
                stringDirection = "hard right-new";
                break;
            case -4:
                stringDirection = "U-Turn";
                break;
            case -3:
                stringDirection = "hard left-new";
                break;
            case -2:
                stringDirection = "left-new";
                break;
            case -1:
                stringDirection = "slight left-new";
                break;
            case 0:
                stringDirection = "Straight";
                break;
            case 1:
                stringDirection = "slight right-new";
                break;
            case 2:
                stringDirection = "right-new";
                break;
            case 3:
                stringDirection = "hard right-new";
                break;
            case 4:
                stringDirection = "U-Turn";
                break;
            case 5:
                stringDirection = "hard left-new";
                break;
            case 6:
                stringDirection = "left-new";
                break;
            case 7:
                stringDirection = "slight left-new";
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
        System.out.println("Direction is " + direction);
        return direction;
    }

    public static String numToDirection(int num) {
        String textDirection;
        switch (num) {
            case 0:
                textDirection = "Error";
                break;
            case 1:
                textDirection = "South";
                break;
            case 2:
                textDirection = "SouthWest";
                break;
            case 3:
                textDirection = "West";
                break;
            case 4:
                textDirection = "NorthWest";
                break;
            case 5:
                textDirection = "North";
                break;
            case 6:
                textDirection = "NorthEast";
                break;
            case 7:
                textDirection = "East";
                break;
            case 8:
                textDirection = "SouthEast";
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

    public static int directionToNum(String direction) {
        int num;
        switch (direction) {
            case "South":
                num = 1;
                break;
            case "SouthEast":
                num = 2;
                break;
            case "East":
                num = 3;
                break;
            case "NorthEast":
                num = 4;
                break;
            case "North":
                num = 5;
                break;
            case "NorthWest":
                num = 6;
                break;
            case "West":
                num = 7;
                break;
            case "SouthWest":
                num = 8;
                break;
            case "Up":
                num = 9;
                break;
            case "Down":
                num = 10;
                break;
            default:
                num = 100;
                break;
        }
        return num;
    }

    public LinkedList<DirectionStep> getTextDirections() {
        return textDirections;
    }

    public void saveStepImages() {
        System.out.println("Saving images!");
        System.out.println("There are " + this.textDirections.size() + " directionSteps");
        for(int i = 1; i <= this.textDirections.size(); i++){
            DirectionStep d = textDirections.get(i-1);
            System.out.println("Creating info for floor " + d.getFloor().getFloorNumber());
            d.getFloor().initImage();
            try {

                // BEGIN AREA WITH ERROR

                //BufferedImage buffImg = d.getFloor().getImageInfo().getBufferedImage();
                d.getFloor().getImageInfo().display();
                BufferedImage realBaseImage = d.getFloor().getImageInfo().getBufferedImage();
                System.out.println("Width of image: " + realBaseImage.getWidth());
                System.out.println("Height of image: " + realBaseImage.getHeight());

                DirectionStep aStep = d;
                Floor aFloor = d.getFloor();
                ProxyImage aImageInfo = d.getFloor().getImageInfo();

                if (aStep == null) {
                    System.out.println("It's aStep");
                    throw new Exception();
                }
                if (aFloor == null) {
                    System.out.println("It's aFloor");
                    throw new Exception();
                }
                if (aImageInfo == null) {
                    System.out.println("It's imageInfo");
                    throw new Exception();
                }

                if (realBaseImage == null) {
                    System.out.println("It's bufferedImage");
                    throw new Exception();
                }

                // END AREA WITH ERROR

                // create the new image, canvas size is the max. of both image sizes
                int w = realBaseImage.getWidth();
                int h = realBaseImage.getHeight();
                BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

                // paint both images, preserving the alpha channels
                Graphics2D g = combined.createGraphics();
                g.drawImage(realBaseImage, 0, 0, null);
                double constant = 2.173;
                //add edges to the map
                g.setStroke(new BasicStroke(25, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                g.setColor(Color.RED);
                int offsetHeight = nodeImg.getHeight() / 2;
                int offsetWidth = nodeImg.getWidth() / 2;
                //System.out.println("Height: " + offsetHeight + " Width: " + offsetWidth);
                for (NodeEdge e : this.pathEdges){
                    //check the edge to see if it's on the current floor
                    if(e.getSource().getMyFloor().getFloorNumber() == d.getFloor().getFloorNumber()
                            && e.getTarget().getMyFloor().getFloorNumber() == d.getFloor().getFloorNumber()){
                        //get the nodes to draw the lines between
                        MapNode targetNode = e.getTarget();
                        MapNode sourceNode = e.getSource();
                        //output info to user for debugging
                        System.out.println("Drawing line between nodes on floor: " + e.getSource().getMyFloor().getFloorNumber());
                        System.out.println("x1: " + Math.round(targetNode.getPosX() * constant) + " y1: " + Math.round( targetNode.getPosY() * constant) +
                                " x2: " + Math.round(sourceNode.getPosX() * constant) + " y2: " + Math.round(sourceNode.getPosY() * constant));
                        //draw the line between the two points. apply offset to account
                        //for image being anchored at upper left of picture
                        g.drawLine((int)Math.round(targetNode.getPosX() * constant) + offsetWidth, (int)Math.round(targetNode.getPosY() * constant) + offsetHeight,
                                (int)Math.round(sourceNode.getPosX() * constant) + offsetWidth, (int)Math.round(sourceNode.getPosY() * constant) + offsetHeight);
                    }
                }

                //add nodes to the map
                for (MapNode n: d.nodesForThisFloor) {
                    System.out.println("X: " + Math.round(n.getPosX()) *constant + " Y; " +  ((int) Math.round(n.getPosY()))*constant);
                    g.drawImage(nodeImg, (int) (Math.round(n.getPosX())*constant), (int)(Math.round(n.getPosY())*constant), null);
                }

                // Save as new image
                LinkedList<Point> startAndEnd = findParemeters(this.pathNodes, d.getFloor().getFloorNumber());
                Point startPoint = startAndEnd.getFirst();
                Point endPoint = startAndEnd.getLast();

                int scaledStartX = (int)(startPoint.x * constant) - boarderSize + offsetWidth;
                int scaledStartY = (int)(startPoint.y * constant) - boarderSize + offsetHeight;
                int scaledEndX = (int)(endPoint.x * constant) + boarderSize + offsetWidth;
                int scaledEndY = (int)(endPoint.y * constant) + boarderSize + offsetHeight;

                if(scaledEndX > combined.getWidth()){
                    scaledEndX = combined.getWidth();
                }
                if(scaledEndY > combined.getHeight()){
                    scaledEndY = combined.getHeight();
                }
                if(scaledStartX < 0){
                    scaledStartX = 0;
                }
                if(scaledStartY < 0){
                    scaledStartY = 0;
                }
                System.out.println("starting with: " + scaledStartX + " " + scaledStartY);
                System.out.println("ending with: " + scaledEndX + " " + scaledEndY);
                BufferedImage croppedImage = cropImage(combined,scaledStartX, scaledStartY, scaledEndX ,scaledEndY );
                int resizedScaleWidthFactor = croppedImage.getWidth() * scaleFactor / (scaledEndX - scaledStartX);
                int resizedScaleHeightFactor = croppedImage.getHeight() * scaleFactor / (scaledEndY - scaledStartY);
                System.out.println("scaled height: " + resizedScaleHeightFactor);
                System.out.println("scaled width: " + resizedScaleWidthFactor);
                BufferedImage resizedVersion = createResizedCopy(croppedImage, croppedImage.getWidth()/resizedScaleWidthFactor, croppedImage.getHeight()/resizedScaleHeightFactor, true);
                System.out.println("Writing image to combined" + i + ".png");
                //ImageIO.write(resizedVersion, "PNG", new File("combined" + i + ".png"));
                ImageIO.write(combined, "PNG", new File("combined" + i + ".png"));
            } catch (Exception e) {
                System.out.println("threw something very wrong");
                e.printStackTrace();
            }

        }
    }

    public boolean sendEmailGuidance(String address) {
        String subjectLine;
        String directionLine = "<H2><center> You have chosen to navigate to " + pathNodes.get(pathNodes.size() - 1).getNodeID() + ".</center></H2>" + "<H3>";
        subjectLine = "Your Directions are Enclosed - Faulkner Hospital";

        int stepNumber = 1;
        for (DirectionStep step: textDirections) {
            for(String s: step.getDirections()) {
                directionLine += "<b>" + stepNumber + ":</b> ";
                directionLine += s;
                directionLine += "<br>";
                stepNumber++;
            }
        }
        directionLine += "</H3>";

        saveStepImages();

        if (Math.random() < .25) {
            System.out.println("Egg called");
            directionLine += "<a href=\"https://youtu.be/dQw4w9WgXcQ\">Some music for your enjoyement, hospital-goer.</a>";
        }

        try {
            SendEmail e = new SendEmail(address, subjectLine, directionLine, true, textDirections.size());
            return true;
        } catch(Exception e) {
            System.out.println("Threw an exception: " + e);
            return false;
        }
    }

    BufferedImage createResizedCopy(Image originalImage,
                                    int scaledWidth, int scaledHeight,
                                    boolean preserveAlpha)
    {
        System.out.println("resizing...");
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    BufferedImage cropImage(BufferedImage origionalImage, int startX, int startY, int endX, int endY){
        // create a cropped image from the original image
        System.out.println("Start point: " + startX + ", " + startY);
        System.out.println("End point: " + endX + ", " + endY);
        return origionalImage.getSubimage(startX, startY, Math.abs(endX - startX), Math.abs(endY - startY));
    }

    LinkedList<Point> findParemeters(LinkedList<MapNode> listOfNodes, int floorNum){
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (MapNode n : listOfNodes){
            if(n.getMyFloor().getFloorNumber() == floorNum){
                if(n.getPosX() < minX){
                    minX = n.getPosX();
                }
                if(n.getPosY() < minY){
                    minY = n.getPosY();
                }
                if(n.getPosX() > maxX){
                    maxX = n.getPosX();
                }
                if(n.getPosY() > maxY){
                    maxY = n.getPosY();
                }
            }
        }
        Point startPoint = new Point((int)minX, (int)minY);
        Point endPoint = new Point((int)maxX, (int)maxY);
        LinkedList<Point> returnList = new LinkedList<>();
        returnList.add(startPoint);
        returnList.addLast(endPoint);
        return returnList;
    }


    public boolean sendTextGuidance(String address) {
        String textMessagebody = "Your instructions are:\n";
        for (int i = 0; i < textDirections.size(); i++) {
            textMessagebody += ((i+1) + ". ");
            for (String s: textDirections.get(i).directionsForThisFloor) {
                textMessagebody += (s + "\n");
            }
        }
        try {
            SendText t = new SendText(address, textMessagebody);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
