package Map.Navigation;


import Map.Entity.*;

import java.util.LinkedList;

/**
 * Created by IanCJ on 2/22/2017.
 */
public class TextDirectionsCreator {

    LinkedList<DirectionFloorStep> directionFloorSteps;

    /**
     * This class takes a bunch of nodes and edges, and turns it into a bunch of string representing the human-readable directions
     * along the given nodes and edges.
     */
    boolean vFlag;


    TextDirectionsCreator(LinkedList<MapNode> listNodes, LinkedList<NodeEdge> listEdges, int kioskDirection, boolean vFlag) {
        this.vFlag = vFlag;
        this.directionFloorSteps = makeDirectionSteps(listNodes, listEdges, kioskDirection);
    }

    public LinkedList<DirectionFloorStep> getDirectionFloorSteps() {
        return this.directionFloorSteps;
    }

    private LinkedList<DirectionFloorStep> makeDirectionSteps(LinkedList<MapNode> listNodes, LinkedList<NodeEdge> listEdges, int kioskDirection) {
        LinkedList<DirectionFloorStep> tempDirectionFloorSteps = new LinkedList<>();
        LinkedList<DirectionStep> tempDirectionSteps = new LinkedList<>();
        String tempTextDirection;
        LinkedList<MapNode> tempMapNodes = new LinkedList<>();
        LinkedList<NodeEdge> tempNodeEdges = new LinkedList<>();
        int prevDirection = kioskDirection;
        MapNode fromNode = new MapNode();
        MapNode toNode = new MapNode();



        int intersectionsPassed = 0;

        //Add the first node to the textual directions
        if (vFlag) {
            tempTextDirection = ("Start at the Kiosk. (Node " + listNodes.get(0).getNodeID() + ")");
        } else {
            tempTextDirection = ("Start facing the Kiosk");
            //tempTextDirection =("Start by turning towards the Kiosk");
        }
        tempDirectionSteps.add(new DirectionStep(tempTextDirection, tempNodeEdges));
        tempTextDirection = "";
        tempNodeEdges = new LinkedList<>();

        //For every node
        for (int i = 0; i < listNodes.size() - 1; i++) {

            //For each set of two nodes
            fromNode = listNodes.get(i);
            toNode = listNodes.get(i+1);

            double costConstant = 25; //Should be 25 in the final

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

            System.out.println("previous direction was " + Guidance.numToDirection(prevDirection));

            int changeInDirection;
            //If direction is not in an elevator
            if (currentDirection < 9) {
                //change in direction is the difference between directions
                changeInDirection = prevDirection - currentDirection;
                prevDirection = currentDirection;
            } else {
                //If you're on an elevator, your previous direction doesn't matter
                changeInDirection = currentDirection;
                //Presume the elevator passenger faces opposite the way they come in. Add 4 to previous direction, wraparound with modulo.
                if (prevDirection > 8) {
                    prevDirection = 8;
                }
                prevDirection = (prevDirection+4)%8;
            }


            System.out.println("new Direction is " + Guidance.numToDirection(currentDirection));
            //Change the directionChange into a textual string
            String directionChangeString = Guidance.directionChangeToString(changeInDirection, vFlag);
            System.out.println("Direction change is " + directionChangeString);

            if (directionChangeString.equals("Straight")) {
                System.out.println("Passing a possible intersection");
                if (fromNode.getEdges().size() > 3) {
                    System.out.println("Passing a definite intersection");
                    System.out.println("fromNode has size of edges: " + fromNode.getEdges().size());
                    intersectionsPassed++;
                }
                tempMapNodes.add(fromNode);
                tempNodeEdges.add(fromNode.getEdgeTo(toNode));
                intersectionsPassed++;
            } else if (!directionChangeString.equals("Straight") && (!directionChangeString.equals("up")) && (!directionChangeString.equals("down"))) {
                if(intersectionsPassed  == 0) {
                    if ((i == 0)  || fromNode.getType().toString().equals("Elevator")){
                        tempTextDirection = ("Turn " + directionChangeString + " and proceed forward");
                    } else {
                            tempTextDirection = ("Take a " + directionChangeString + " at the next intersection");
                    }
                    tempMapNodes.add(fromNode);
                    if (vFlag) {
                        tempTextDirection += (" ID: " + fromNode.getNodeID());
                    }
                }
                else if(intersectionsPassed == 1){
                    tempTextDirection = ("After passing 1 intersection, take a " + directionChangeString);
                    tempMapNodes.add(fromNode);
                    if (vFlag) {
                        tempTextDirection += (" at " + fromNode.getNodeID());
                    }
                }
                else{
                    tempTextDirection = ("After passing " + intersectionsPassed + " intersections, take a " + directionChangeString);
                    tempMapNodes.add(fromNode);
                    if (vFlag) {
                        tempTextDirection += (" at " + fromNode.getNodeID());

                    }
                }
                intersectionsPassed = 0;
                tempNodeEdges.add(fromNode.getEdgeTo(toNode));
                String directionsRelativeToOtherNodes = "";
                //check if the node we are at is a node with a name (IE, going through an office), and make sure that it's not the destination
                if(fromNode instanceof Destination) {
                    directionsRelativeToOtherNodes = " at " + ((Destination) fromNode).getName();
                    intersectionsPassed = 0;
                }
                else {
                    System.out.println("not a destination");
                    double max = 70;
                    // If a nearby node has a name use that
                    for( NodeEdge e: fromNode.getEdges()) {
                        MapNode potentialNeighbor = e.getOtherNode(fromNode);
                        double distance = getDistanceBetweenNodes(fromNode,potentialNeighbor);
                        if( (distance < max) && (potentialNeighbor instanceof Destination) ) {
                            max = distance;
                            // If the nearby node is the node you came from
                            if(potentialNeighbor == listNodes.get(i - 1)) {
                                directionsRelativeToOtherNodes = " away from ";
                            }
                            else if(potentialNeighbor == toNode) {
                                directionsRelativeToOtherNodes = " towards ";
                            }
                            else{
                                directionsRelativeToOtherNodes = " near ";
                            }
                            directionsRelativeToOtherNodes += ((Destination) potentialNeighbor).getName();
                            intersectionsPassed = 0;
                        }
                    }
                }
                tempTextDirection += directionsRelativeToOtherNodes;

                //If there are nodes after this
                if(listNodes.size() > i+4) {
                    System.out.println("It's greater");
                    MapNode tempFromNode = listNodes.get(i+1);
                    MapNode tempToNode = listNodes.get(i+2);
                    int tempPrevDirection = currentDirection;
                    int tempCurrentDirection = Guidance.nodesToDirection(tempFromNode, tempToNode);
                    //If the distance to the next node is small, add it into this string
                    if (tempFromNode.getEdgeTo(tempToNode).getCost() < costConstant) {
                        System.out.println("good cost");
                        int tempChangeInDirection = 10;
                        //If direction is not in an elevator
                        if (tempCurrentDirection < 9) {
                            System.out.println("good current dir");
                            //change in direction is the difference between directions
                            tempChangeInDirection = tempPrevDirection - tempCurrentDirection;
                        }
                        if ((Math.abs(tempChangeInDirection) < 8) && !(Guidance.directionChangeToString(tempChangeInDirection, false).equals("Straight")) && (!tempToNode.getType().toString().equals("Elevator"))){
                            System.out.println("good change");
                            String tempDirectionChangeString = Guidance.directionChangeToString(tempChangeInDirection, vFlag);
                            tempTextDirection += ",\n then immediately take a " + tempDirectionChangeString;
                            tempMapNodes.add(tempFromNode);
                            tempNodeEdges.add(tempFromNode.getEdgeTo(tempToNode));
                            i++;
                            intersectionsPassed = 0;
                        }
                    }
                }

                //replace stupid substring that sometimes gets generated
                if(tempTextDirection.contains("at the next intersection at")){
                    tempTextDirection = tempTextDirection.replace("at the next intersection at", "at");
                    System.out.println(tempTextDirection);
                }

                tempDirectionSteps.add(new DirectionStep(tempTextDirection, tempNodeEdges));
                tempNodeEdges = new LinkedList<>();
                if (directionChangeString.equals("outside") || directionChangeString.equals("inside")) {
                    tempDirectionFloorSteps.addLast(new DirectionFloorStep(fromNode.getMyFloor(), tempMapNodes, tempDirectionSteps));
                    tempDirectionSteps = new LinkedList<>();
                    tempMapNodes = new LinkedList<>();
                    intersectionsPassed = 0;
                }
            } else if (directionChangeString.equals("up") || directionChangeString.equals("down")) {
                System.out.println("Hit an elevator");
                if(intersectionsPassed == 0){
                    tempTextDirection = ("Take an elevator at the next intersection from floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                    tempMapNodes.add(fromNode);
                    if (vFlag) {
                        tempTextDirection += ("ID " + fromNode.getNodeID() + " to " + toNode.getNodeID());
                    }
                }
                else if(intersectionsPassed == 1){
                    tempTextDirection = ("After passing 1 intersection, take an elevator from floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                    tempMapNodes.add(fromNode);
                    if (vFlag) {
                        tempTextDirection += ("ID " + fromNode.getNodeID() + " to " + toNode.getNodeID());
                    }
                }
                else {
                    tempTextDirection = ("After passing " + intersectionsPassed + " intersections" + ", take the elevator " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                    tempMapNodes.add(fromNode);
                    if (vFlag) {
                        tempTextDirection += ("ID " + fromNode.getNodeID() + " to " + toNode.getNodeID());
                    }
                }
                tempNodeEdges.add(fromNode.getEdgeTo(toNode));
                tempDirectionSteps.addLast(new DirectionStep(tempTextDirection, tempNodeEdges));
                tempDirectionFloorSteps.addLast(new DirectionFloorStep(fromNode.getMyFloor(), tempMapNodes, tempDirectionSteps));
                tempDirectionSteps = new LinkedList<>();
                tempNodeEdges = new LinkedList<>();
                tempMapNodes = new LinkedList<>();
                intersectionsPassed = 0;
            }
        }

        //Add the destination arrival string
        if (intersectionsPassed >= 2) {
            tempTextDirection = ("After passing " + intersectionsPassed + " intersections, arrive at your destination.");
            if (vFlag) {
                tempTextDirection += ("At ID " + toNode.getNodeID());
            }
        } else if (intersectionsPassed == 1) {
            tempTextDirection = ("After passing 1 intersection, arrive at your destination.");
            if (vFlag) {
                tempTextDirection += ("At ID " + toNode.getNodeID());
            }
        } else {
            tempTextDirection = ("Arrive at your destination.");
            if (vFlag) {
                tempTextDirection += ("At ID " + toNode.getNodeID());
            }
        }

        tempNodeEdges.add(fromNode.getEdgeTo(toNode));
        tempDirectionSteps.addLast(new DirectionStep(tempTextDirection, tempNodeEdges));
        tempMapNodes.add(toNode);
        tempDirectionFloorSteps.addLast(new DirectionFloorStep(fromNode.getMyFloor(), tempMapNodes, tempDirectionSteps));
        tempDirectionSteps = new LinkedList<>();
        tempNodeEdges = new LinkedList<>();
        tempMapNodes = new LinkedList<>();
        intersectionsPassed = 0;

        return tempDirectionFloorSteps;

    }

    private double getDistanceBetweenNodes(MapNode a, MapNode b){
        double currentNodeX = a.getPosX();
        double currentNodeY = a.getPosY();
        double endNodeX = b.getPosX();
        double endNodeY = b.getPosY();
        return Math.sqrt(Math.pow(endNodeX - currentNodeX, 2) + Math.pow(endNodeY - currentNodeY, 2));
    }


    /**
     * Totally necessary
     */
    public void makeSmartass() {
        for(DirectionFloorStep floorStep: this.directionFloorSteps) {
            for (DirectionStep step: floorStep.getDirectionSteps()) {
                String tempString = step.getInstruction() + ", idiot.";
                step.setInstruction(tempString);
            }
        }
    }
}