package Domain.Navigation;


import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import Domain.Map.*;
import Exceptions.*;
import Model.MapModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;

/**
 * Direction tells you how to get from
 */
public class Guidance extends Path {

    //This is the direction that the user of the kiosk starts off facing.
    private int kioskDirection = 3;

    double xNodeScale = 1200/941;
    double yNodeScale = 700/546;

    LinkedList<DirectionStep> textDirections;

    public Guidance (MapNode start, MapNode end) throws PathFindingException {
            this(start, end, false);
            printTextDirections();
    }

    public Guidance (MapNode start, MapNode end, boolean vFlag) throws PathFindingException {
        //Make the path part
        super(start, end, vFlag);

        //Declare and initialize directions
        textDirections = new LinkedList<DirectionStep>();
        createTextDirections(vFlag);

        if (vFlag) {
            printTextDirections();
        }
    }

    public Guidance (MapNode start, MapNode end, String kioskInputDirection) throws PathFindingException{
        super(start, end, false);

        kioskDirection = Guidance.directionToNum(kioskInputDirection);

        textDirections = new LinkedList<DirectionStep>();
        createTextDirections(false);


    }

    public void createTextDirections() {
        createTextDirections(false);
    }

    public void createTextDirections(boolean vFlag) {
        LinkedList<String> tempTextDirections = new LinkedList<String>();
        int prevDirection = kioskDirection;
        MapNode fromNode = new MapNode();
        MapNode toNode = new MapNode();

        int intersectionsPassed = 0;

        //Add the first node to the textual directions
        tempTextDirections.add("Start at the Kiosk. (Node " + pathNodes.get(0).getNodeID() + ")");

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
            } else if (!directionChangeString.equals("Straight") && (!directionChangeString.equals("up")) && (!directionChangeString.equals("down"))) {
                if(intersectionsPassed  == 0) {
                    if (vFlag) {
                        tempTextDirections.add("Turn " + directionChangeString + " at the next intersection; ID: " + fromNode.getNodeID());
                    } else {
                        tempTextDirections.add("Turn " + directionChangeString + " at the next intersection.");

                    }
                }
                else if(intersectionsPassed == 1){
                    if (vFlag) {
                        tempTextDirections.add("After passing 1 intersection, turn " + directionChangeString + " at " + fromNode.getNodeID());
                    } else {
                        tempTextDirections.add("After passing 1 intersection, turn " + directionChangeString + ".");
                    }
                }
                else{
                    if (vFlag) {
                        tempTextDirections.add("After passing " + intersectionsPassed + " intersections, turn " + directionChangeString + " at " + fromNode.getNodeID());
                    } else {
                        tempTextDirections.add("After passing " + intersectionsPassed + " intersections, turn " + directionChangeString + ".");

                    }
                }

                intersectionsPassed = 0;
            } else if (directionChangeString.equals("up") || directionChangeString.equals("down")) {
                if(intersectionsPassed == 0){
                    if (vFlag) {
                        tempTextDirections.add("Take an elevator at the next intersection from " + fromNode.getNodeID() + " on floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                    } else {
                        tempTextDirections.add("Take an elevator at the next intersection from floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                    }
                }
                else if(intersectionsPassed == 1){
                    if (vFlag) {
                        tempTextDirections.add("After passing 1 intersection, take an elevator at " + fromNode.getNodeID() + " on floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                    } else {
                        tempTextDirections.add("After passing 1 intersection, take an elevator from floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());

                    }
                }
                else {
                    if (vFlag) {
                        tempTextDirections.add("After passing " + intersectionsPassed + " intersections" + ", take the elevator" + " at " + fromNode.getNodeID() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                    } else {
                        tempTextDirections.add("After passing " + intersectionsPassed + " intersections" + ", take the elevator " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());

                    }
                }
                this.textDirections.addLast(new DirectionStep(fromNode.getMyFloor()));
                this.textDirections.getLast().setDirections(tempTextDirections);
                tempTextDirections.clear();
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
        this.textDirections.add(new DirectionStep(toNode.getMyFloor()));
        this.textDirections.getLast().setDirections(tempTextDirections);
    }

    public void printTextDirections() {
        System.out.println("");
        System.out.println("Printing Textual Directions");
        System.out.println("");
        for (DirectionStep step: textDirections) {
            for(String s : step.getDirections()) {
                System.out.println(s);
            }
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

    public static int directionToNum(String direction) {
        int num;
        switch (direction) {
            case "North":
                num = 1;
                break;
            case "NorthEast":
                num = 2;
                break;
            case "East":
                num = 3;
                break;
            case "SouthEast":
                num = 4;
                break;
            case "South":
                num = 5;
                break;
            case "SouthWest":
                num = 6;
                break;
            case "West":
                num = 7;
                break;
            case "NorthWest":
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

    public LinkedList<DirectionStep> getTextDirections()
    {
        return textDirections;
    }

    public void createImageFromScratch(Floor aFloor) {

        try {
            LinkedList<BufferedImage> listOfOverlays = new LinkedList<>();

// load source images
            BufferedImage image = ImageIO.read(new File("emptyImage.png"));
            BufferedImage overlay = ImageIO.read(new File("lildude.png"));

            for (int i = 0; i < 25; i++) {
                listOfOverlays.add(ImageIO.read(new File("lildude.png")));
            }

// create the new image, canvas size is the max. of both image sizes
            int w = image.getWidth();
            int h = image.getHeight();
            BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

// paint both images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(image, 0, 0, null);
            for (int i = 0; i < aFloor.getFloorNodes().size(); i++) {

                g.drawImage(listOfOverlays.get(i), 50 * (int) Math.round(aFloor.getFloorNodes().get(i).getPosX()), 50 * (int) Math.round(aFloor.getFloorNodes().get(i).getPosY()), null);
            }

// Save as new image
            ImageIO.write(combined, "PNG", new File("combined.png"));
        } catch (Exception e) {
            System.out.println("Threw another exception over here.");
        }
    }

    public void overlayOnMap() {

        try {
// load source images
            BufferedImage image = ImageIO.read(new File("emptyImage.png"));
            BufferedImage overlay = ImageIO.read(new File("directionPath.png"));

// create the new image, canvas size is the max. of both image sizes
            int w = Math.max(image.getWidth(), overlay.getWidth());
            int h = Math.max(image.getHeight(), overlay.getHeight());
            BufferedImage combined = new BufferedImage(1244, 700, BufferedImage.TYPE_INT_ARGB);

// paint both images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.drawImage(overlay, 0, 0, null);

// Save as new image
            ImageIO.write(combined, "PNG", new File("combined.png"));
        } catch (Exception e) {
            System.out.println("Threw another exception over here.");
        }
    }

    private java.awt.Image transformColorToTransparency(BufferedImage image, Color c1, Color c2)
    {
        // Primitive test, just an example
        final int r1 = c1.getRed();
        final int g1 = c1.getGreen();
        final int b1 = c1.getBlue();
        final int r2 = c2.getRed();
        final int g2 = c2.getGreen();
        final int b2 = c2.getBlue();
        ImageFilter filter = new RGBImageFilter()
        {
            public final int filterRGB(int x, int y, int rgb)
            {
                int r = (rgb & 0xFF0000) >> 16;
                int g = (rgb & 0xFF00) >> 8;
                int b = rgb & 0xFF;
                if (r >= r1 && r <= r2 &&
                        g >= g1 && g <= g2 &&
                        b >= b1 && b <= b2)
                {
                    // Set fully transparent but keep color
                    return rgb & 0xFFFFFF;
                }
                return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private java.awt.Image transformWhiteToTransparency(BufferedImage image)
    {
        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                return (rgb << 8) & 0xFF000000;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public AnchorPane createDirectionPane() {
        AnchorPane directionPane = new AnchorPane();

        //and then set all the existing nodes up
        HashSet<NodeEdge> collectedEdges = new HashSet<NodeEdge>();

        for(MapNode n : pathNodes)
        {
            for(NodeEdge edge: n.getEdges())
            {
                if(!collectedEdges.contains(edge)) collectedEdges.add(edge);
            }

            if(!directionPane.getChildren().contains(n.getNodeToDisplay()))
            {
                directionPane.getChildren().add(n.getNodeToDisplay());
            }


            System.out.println("Adding node at X:" + n.getPosX() + "Y: " + n.getPosY());

            n.getNodeToDisplay().relocate(n.getPosX()*xNodeScale*1.27, 1.27*n.getPosY()*yNodeScale);
            n.getNodeToDisplay().setOnMouseClicked(null);
            n.getNodeToDisplay().setOnMouseEntered(null);
            n.getNodeToDisplay().setOnMouseDragged(null);

        }

        for(NodeEdge edge : collectedEdges)
        {

            if(!directionPane.getChildren().contains(edge.getEdgeLine()))
            {
                directionPane.getChildren().add(edge.getEdgeLine());
            }

            MapNode source = edge.getSource();
            MapNode target = edge.getTarget();

            //@TODO BUG WITH SOURCE DATA, I SHOULDNT HAVE TO DO THIS

            if(!directionPane.getChildren().contains(source.getNodeToDisplay()))
            {

                directionPane.getChildren().add(source.getNodeToDisplay());

                source.getNodeToDisplay().relocate(source.getPosX() * 2*xNodeScale, source.getPosY() * 2* yNodeScale);
            }

            if(!directionPane.getChildren().contains(target.getNodeToDisplay()))
            {
                directionPane.getChildren().add(target.getNodeToDisplay());
                target.getNodeToDisplay().relocate(target.getPosX() * 2*xNodeScale, target.getPosY() * 2*yNodeScale);
            }

            edge.updatePosViaNode(source);
            edge.updatePosViaNode(target);

            edge.setSource(source);
            edge.setTarget(target);

            source.toFront();
            target.toFront();

            edge.getEdgeLine().setOnMouseEntered(null);
            edge.getEdgeLine().setOnMouseClicked(null);

        }


        return directionPane;
    }

    public void savePathImage(Node aNode) {
        WritableImage image = aNode.snapshot(new SnapshotParameters(), null);

        // TODO: probably use a file chooser here
        File file;

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("directionPath1.png"));
        } catch (IOException e) {
            // TODO: handle exception here
            System.out.println("EGADS!  We have an exception!");
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("directionPath1.png"));
        } catch (IOException e) {
            System.out.println("Something bad");
        }

        //java.awt.Image transImage = transformWhiteToTransparency(img);
        java.awt.Image transImage = transformColorToTransparency(img, Color.lightGray, Color.white);


        int w = transImage.getWidth(null);
        int h = transImage.getHeight(null);
        int type = BufferedImage.TYPE_INT_ARGB;  // other options
        BufferedImage transImageBuff = new BufferedImage(w, h, type);
        Graphics2D g2 = transImageBuff.createGraphics();
        g2.drawImage(transImage, 0, 0, null);
        g2.dispose();


        file = new File("directionPath.png");
        try {
            ImageIO.write(transImageBuff, "png", file);  // ignore returned boolean
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                    ": " + e.getMessage());
        }

    }

    public boolean sendEmailGuidance(String address) {

        AnchorPane newDirectionPane = this.createDirectionPane();

        savePathImage(newDirectionPane);

        overlayOnMap();

        String subjectLine;
        String directionLine = "<H2>You have chosen to navigate from " + pathNodes.get(0).getNodeID() + " to " + pathNodes.get(pathNodes.size()-1).getNodeID() + ".</H2>" + "<H3>";
        subjectLine = "Your Directions are Enclosed - Faulkner Hospital";

        for (DirectionStep step: textDirections) {
            for(String s: step.getDirections()) {

                System.out.println(s);
                directionLine += s;
                directionLine += "<br>";
            }
        }

        directionLine += "</H3>";
        try {
            SendEmail e = new SendEmail(address, subjectLine, directionLine, true);
            return true;
        } catch(Exception e) {
            System.out.println("Threw an exception: " + e);
            return false;
        }
    }

    public void saveMapImage(MapModel model) {
        File file;
        Image backgroundImage = new Image("/floor3.png", true);
        /*
        BufferedImage bImage = SwingFXUtils.fromFXImage(backgroundImage, null);
        file = new File("tempTest.png");
        try {
            ImageIO.write(bImage, "png", file);
        } catch (Exception e) {
            System.out.println("Another exception thrown");
        } */
    }

    public boolean sendEmailGuidance(String address, MapModel model) {
        saveMapImage(model);

        String subjectLine;
        String directionLine = "<H2>You have chosen to navigate from " + pathNodes.get(0).getNodeID() + " to " + pathNodes.get(pathNodes.size()-1).getNodeID() + ".</H2>" + "<H3>";
        subjectLine = "Your Directions are Enclosed - Faulkner Hospital";

        for (DirectionStep step: textDirections) {
            for(String s: step.getDirections()) {

                System.out.println(s);
                directionLine += s;
                directionLine += "<br>";
            }
        }

        directionLine += "</H3>";
        try {
            SendEmail e = new SendEmail(address, subjectLine, directionLine, true);
            return true;
        } catch(Exception e) {
            System.out.println("Threw an exception: " + e);
            return false;
        }
    }
}
