package Map.Navigation;


import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.awt.Point;

import Map.Entity.NodeType;
import Application.Exceptions.*;
import Map.Entity.Floor;
import Map.Entity.MapNode;
import Map.Entity.NodeEdge;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;

/**
 * Direction tells you how to get from
 */
public class Guidance extends Path {

    //This is the direction that the user of the kiosk starts off facing.
    private int kioskDirection = 3;
    private int scaleFactor = 1;

    private int desiredWidth = 375;
    private int desiredHeight = 375;
    private int boarderSize = 100;

    BufferedImage nodeImg = null;
    BufferedImage bathImg= null;
    BufferedImage docImg = null;
    BufferedImage elevatorImg = null;
    BufferedImage foodImg = null;
    BufferedImage infoImg = null;
    BufferedImage storeImg = null;
    BufferedImage currentKioskImg = null;

    LinkedList<DirectionFloorStep> floorSteps;

    public void setImages() {
        try {

            int imgRescaleSize = 46;

            nodeImg = createResizedCopy(ImageIO.read(new File("src/View/Admin/MapBuilder/blank2.png")), imgRescaleSize/2, imgRescaleSize/2, true);
            bathImg = createResizedCopy(ImageIO.read(new File("src/View/Admin/MapBuilder/bathroom.png")), imgRescaleSize, imgRescaleSize, true);
            docImg = createResizedCopy(ImageIO.read(new File("src/View/Admin/MapBuilder/doctor.png")), imgRescaleSize, imgRescaleSize, true);
            elevatorImg = createResizedCopy(ImageIO.read(new File("src/View/Admin/MapBuilder/elevator.png")), imgRescaleSize, imgRescaleSize, true);
            foodImg = createResizedCopy(ImageIO.read(new File("src/View/Admin/MapBuilder/food.png")), imgRescaleSize, imgRescaleSize, true);
            infoImg = createResizedCopy(ImageIO.read(new File("src/View/Admin/MapBuilder/info.png")), imgRescaleSize, imgRescaleSize, true);
            storeImg = createResizedCopy(ImageIO.read(new File("src/View/Admin/MapBuilder/store.png")), imgRescaleSize, imgRescaleSize, true);
            currentKioskImg = createResizedCopy(ImageIO.read(new File("src/View/Admin/MapBuilder/star.png")), imgRescaleSize, imgRescaleSize, true);
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
        floorSteps = new LinkedList<DirectionFloorStep>();
        TextDirectionsCreator tdc = new TextDirectionsCreator(pathNodes, pathEdges, kioskDirection, vFlag);
        floorSteps = tdc.getDirectionFloorSteps();

        if (vFlag) {
            printTextDirections();
        }
    }

    public Guidance (MapNode start, MapNode end, String kioskInputDirection) throws PathFindingException{
        super(start, end, false);
        setImages();

        kioskDirection = Guidance.directionToNum(kioskInputDirection);

        floorSteps = new LinkedList<DirectionFloorStep>();
        TextDirectionsCreator tdc = new TextDirectionsCreator(pathNodes, pathEdges, kioskDirection, false);
        floorSteps = tdc.getDirectionFloorSteps();


    }

    public LinkedList<DirectionFloorStep> getSteps()
    {
        return floorSteps;
    }

    public void printTextDirections() {
        System.out.println("");
        System.out.println("Printing Textual Directions");
        System.out.println("");
        for (DirectionFloorStep step: floorSteps) {
            System.out.println("Printing a floorStep: ");
            for(DirectionStep s : step.getDirectionSteps()) {
                System.out.println(s);
            }
        }
    }

    public void printTextDirections(boolean superVerbose) {
        System.out.println("");
        System.out.println("Printing Textual Directions");
        System.out.println("");
        for (int i = 0; i < floorSteps.size(); i++ ) {
            System.out.println("Printing floorstep with index " + i);
            System.out.println("");
            for (int k = 0; k < floorSteps.get(i).getDirectionSteps().size(); k++) {
                System.out.println("Printing directionStep in floorstep " + i + " with index " + k);
                System.out.println(floorSteps.get(i).getDirectionSteps().get(k).toString());
                System.out.println("");
            }
        }
    }


    public static int nodesToDirection(MapNode fromNode, MapNode toNode) {
        return Guidance.nodesToDirection(fromNode, toNode, false);
    }

    public static String directionChangeToString(int changeInDirection, boolean vFlag) {
        //If a direction comes out as "Error", somethings wrong
        String stringDirection;
        switch (changeInDirection) {
            case -7:
                stringDirection = "slight right";
                break;
            case -6:
                stringDirection = "right";
                break;
            case -5:
                stringDirection = "hard right";
                break;
            case -4:
                stringDirection = "around";
                break;
            case -3:
                stringDirection = "hard left";
                break;
            case -2:
                stringDirection = "left";
                break;
            case -1:
                stringDirection = "slight left";
                break;
            case 0:
                stringDirection = "Straight";
                break;
            case 1:
                stringDirection = "slight right";
                break;
            case 2:
                stringDirection = "right";
                break;
            case 3:
                stringDirection = "hard right";
                break;
            case 4:
                stringDirection = "around";
                break;
            case 5:
                stringDirection = "hard left";
                break;
            case 6:
                stringDirection = "left";
                break;
            case 7:
                stringDirection = "slight left";
                break;
            case 9:
                stringDirection = "up";
                break;
            case 10:
                stringDirection = "down";
                break;
            case 11:
                stringDirection = "outside";
                break;
            case 12:
                stringDirection = "inside";
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
            case 11:
                textDirection = "Outside";
                break;
            case 12:
                textDirection = "Inside";
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
            case "Outside":
                num = 11;
                break;
            case "Inside":
                num = 12;
                break;
            default:
                num = 100;
                break;
        }
        return num;
    }

    public LinkedList<DirectionFloorStep> getFloorSteps() {
        return floorSteps;
    }

    public void saveStepImages() {
        System.out.println("Saving images!");
        System.out.println("There are " + this.floorSteps.size() + " directionFloorSteps");
        for(int i = 1; i <= this.floorSteps.size(); i++){
            DirectionFloorStep d = floorSteps.get(i-1);
            System.out.println("Creating info for floor " + d.getFloor().getFloorNumber());
            d.getFloor().initImage();
            try {
                d.getFloor().getImage();
                javafx.scene.image.Image realBaseImage = d.getFloor().getImage();
                System.out.println("Width of image: " + realBaseImage.getWidth());
                System.out.println("Height of image: " + realBaseImage.getHeight());

                DirectionFloorStep aStep = d;
                Floor aFloor = d.getFloor();

                if (aStep == null) {
                    System.out.println("It's aStep");
                    throw new Exception();
                }
                if (aFloor == null) {
                    System.out.println("It's aFloor");
                    throw new Exception();
                }

                if (realBaseImage == null) {
                    System.out.println("It's bufferedImage");
                    throw new Exception();
                }


                // create the new image, canvas size is the max. of both image sizes
                double w = realBaseImage.getWidth();
                double h = realBaseImage.getHeight();
                BufferedImage combined = new BufferedImage((int)w, (int)h, BufferedImage.TYPE_INT_ARGB);

                // paint both images, preserving the alpha channels
                Graphics2D g = combined.createGraphics();
                g.drawImage(SwingFXUtils.fromFXImage(realBaseImage, null), 0, 0, null);
                double constant = 2.175;
/*                int w = realBaseImage.getWidth();
                int h = realBaseImage.getHeight();
                BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                System.out.println("ERE");
                // paint both images, preserving the alpha channels
                Graphics2D g = combined.createGraphics();
                g.drawImage(realBaseImage, 0, 0, null);
                System.out.println("THERE");
                double constant = 2.185;
*/
                //add edges to the map
                g.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
                g.setColor(Color.RED);
                int offsetHeight = nodeImg.getHeight() / 2;
                int offsetWidth = nodeImg.getWidth() / 2;
                //System.out.println("Height: " + offsetHeight + " Width: " + offsetWidth);
                for (NodeEdge e : this.pathEdges){
                    //check the edge to see if it's on the current floor
                    boolean edgeIsInThisFloorStep = false;
                    for (DirectionStep aDirectionStep: d.getDirectionSteps()) {
                        if (aDirectionStep.getStepEdges().contains(e)) {
                            edgeIsInThisFloorStep = true;
                        }
                    }
                    if(e.getSource().getMyFloor().getFloorNumber() == d.getFloor().getFloorNumber()
                            && e.getTarget().getMyFloor().getFloorNumber() == d.getFloor().getFloorNumber()
                            && edgeIsInThisFloorStep){
                        //get the nodes to draw the lines between
                        MapNode targetNode = e.getTarget();
                        MapNode sourceNode = e.getSource();

                        int targetIsConnector = 0;
                        int sourceIsConnector = 0;

                        if (targetNode.getType().toString().equals("connector")) {
                            targetIsConnector = 12;
                        }
                        if (sourceNode.getType().toString().equals("connector")) {
                            sourceIsConnector = 12;
                        }
                        g.drawLine((int)Math.round(targetNode.getPosX() * constant) + (targetIsConnector + this.iconToImg(targetNode.getType()).getWidth()/2), (int)Math.round(targetNode.getPosY() * constant) + (targetIsConnector + this.iconToImg(targetNode.getType()).getHeight()/2),
                                (int)Math.round(sourceNode.getPosX() * constant) + (sourceIsConnector + this.iconToImg(sourceNode.getType()).getWidth()/2), (int)Math.round(sourceNode.getPosY() * constant) + (sourceIsConnector + this.iconToImg(sourceNode.getType()).getHeight()/2));


                    }
                }

                //add nodes to the map
                for (MapNode n: d.nodesForThisFloor) {
                    if (d.getNodesForThisFloor().contains(n)) {
                        int isConnector = 0;
                        System.out.println("X: " + Math.round(n.getPosX()) * constant + " Y; " + ((int) Math.round(n.getPosY())) * constant);
                        String thisIconType = n.getType().toString();
                        System.out.println("This is an icon of type: " + thisIconType.toString());

                        BufferedImage currentImage = this.iconToImg(n.getType());

                        if (n.getType().toString().equals("connector")) {
                            isConnector = 12;
                        }

                        g.drawImage(currentImage, (int) (Math.round(n.getPosX()) * constant + isConnector), (int) (Math.round(n.getPosY()) * constant + isConnector), null);
                    }
                }

                // Save as new image
                LinkedList<Point> startAndEnd = findParemeters(d.getNodesForThisFloor(), d.getFloor().getFloorNumber());
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
                BufferedImage croppedImage = cropImage(combined,scaledStartX, scaledStartY, scaledEndX ,scaledEndY );
                float newX;
                float newY;
                int oldX = (scaledEndX - scaledStartX);
                int oldY = (scaledEndY - scaledStartY);
                System.out.println("OLD X: " + oldX);
                System.out.println("OLD Y: " + oldY);
                float R = (float)oldX/(float)oldY;
                System.out.println("R is: " + R);
                if(R > desiredWidth/desiredHeight){
                    System.out.println("Height smaller than Width");
                    newX = desiredWidth;
                    newY = desiredWidth/R;
                }
                else if(R < desiredWidth/desiredHeight){
                    System.out.println("Width smaller than Height");
                    newX = desiredHeight*R;
                    newY = desiredHeight;
                }
                else{
                    System.out.println("Height and width are the same");
                    newY = desiredHeight;
                    newX = desiredWidth;
                }
                System.out.println("NEW X: " + newX);
                System.out.println("NEW Y: " + newY);
                BufferedImage resizedVersion = createResizedCopy(croppedImage, (int)newX, (int)newY, true);
                System.out.println("Writing image to combined" + i + ".png");
                ImageIO.write(resizedVersion, "PNG", new File("combined" + i + ".png"));
            } catch (Exception e) {
                System.out.println("threw something very wrong");
                e.printStackTrace();
            }

        }
    }

    public BufferedImage iconToImg(NodeType t) {
        BufferedImage currentImage;
        NodeType typeOfNode = t;
        if(t == NodeType.Connector){
            currentImage = nodeImg;
        }
        else if(t == NodeType.Store){
            currentImage = storeImg;
        }
        else if(t == NodeType.Elevator){
            currentImage = elevatorImg;
        }
        else if(t == NodeType.Food){
            currentImage = foodImg;
        }
        else if(t == NodeType.Info){
            currentImage = infoImg;
        }
        else if(t == NodeType.Department){
            currentImage = docImg;
        }
        else if(t == NodeType.Restroom){
            currentImage = bathImg;
        }
        else if(t == NodeType.CurrentKiosk){
            currentImage = currentKioskImg;
        }
        else{
            System.out.println("ERROR. NO NODE IMAGE SET. LOOKING FOR: " + t.toString());
            currentImage = null;
        }
        return currentImage;
    }

    public boolean sendEmailGuidance(String address) {
        String subjectLine;
        String directionLine = "<H1><center> You have chosen to navigate to " + pathNodes.get(pathNodes.size() - 1).getNodeID() + ".</center></H1>" + "<H4>";
        subjectLine = "BEN IT WORKS!";
        subjectLine = "Your Directions are Enclosed - Faulkner Hospital";

        int stepNumber = 1;
        for (DirectionFloorStep step: floorSteps) {
            for(DirectionStep s: step.getDirectionSteps()) {
                directionLine += "<b>" + stepNumber + ":</b> ";
                directionLine += s;
                directionLine += "<br>";
                stepNumber++;
            }
        }
        directionLine += "</H4>";

        saveStepImages();


        try {
            SendEmail e = new SendEmail(address, subjectLine, directionLine, true, floorSteps.size());
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
        if(scaledHeight == 0) {
            System.out.println("desired height is 0?");
            scaledHeight = desiredHeight;
        }
        if(scaledWidth == 0){
            System.out.println("desired width is 0?");
            scaledWidth = desiredWidth;
        }
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
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
        for (int i = 0; i < floorSteps.size(); i++) {
            textMessagebody += ((i+1) + ". ");
            for (DirectionStep s: floorSteps.get(i).getDirectionSteps()) {
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
