package Domain.Navigation;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;

import java.util.LinkedList;

/**
 * Created by IanCJ on 2/22/2017.
 */
public class TextDirectionsCreator {

    LinkedList<DirectionFloorStep> directionFloorSteps;

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
            tempTextDirection =("Start at the Kiosk, facing " + Guidance.numToDirection(kioskDirection));
        }
        tempDirectionSteps.add(new DirectionStep(tempTextDirection, tempNodeEdges));
        tempTextDirection = "";
        tempNodeEdges = new LinkedList<>();

        for (int i = 0; i < listNodes.size() - 1; i++) {

            //For each set of two nodes
            fromNode = listNodes.get(i);
            toNode = listNodes.get(i+1);

            double costConstant = 100;

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
                tempNodeEdges.add(fromNode.getEdgeTo(toNode));
                intersectionsPassed++;
            } else if (!directionChangeString.equals("Straight") && (!directionChangeString.equals("up")) && (!directionChangeString.equals("down"))) {
                if(intersectionsPassed  == 0) {
                    if (vFlag) {
                        tempTextDirection = ("Turn " + directionChangeString + " at the next intersection; ID: " + fromNode.getNodeID());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirection = ("Turn " + directionChangeString + " at the next intersection");
                        tempMapNodes.add(fromNode);
                    }
                }
                else if(intersectionsPassed == 1){
                    if (vFlag) {
                        tempTextDirection = ("After passing 1 intersection, turn " + directionChangeString + " at " + fromNode.getNodeID());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirection = ("After passing 1 intersection, turn " + directionChangeString);
                        tempMapNodes.add(fromNode);
                    }
                }
                else{
                    if (vFlag) {
                        tempTextDirection = ("After passing " + intersectionsPassed + " intersections, turn " + directionChangeString + " at " + fromNode.getNodeID());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirection = ("After passing " + intersectionsPassed + " intersections, turn " + directionChangeString);
                        tempMapNodes.add(fromNode);

                    }
                }
                intersectionsPassed = 0;
                tempNodeEdges.add(fromNode.getEdgeTo(toNode));
                if(listNodes.size() > i+2) {
                    System.out.println("It's greater");
                    MapNode tempFromNode = listNodes.get(i+1);
                    MapNode tempToNode = listNodes.get(i+2);
                    int tempPrevDirection = currentDirection;
                    int tempCurrentDirection = Guidance.nodesToDirection(tempFromNode, tempToNode);
                    if (tempFromNode.getEdgeTo(tempToNode).getCost() < costConstant) {
                        System.out.println("good cost");
                        int tempChangeInDirection = 10;
                        //If direction is not in an elevator
                        if (tempCurrentDirection < 9) {
                            System.out.println("good current dir");
                            //change in direction is the difference between directions
                            tempChangeInDirection = tempPrevDirection - tempCurrentDirection;
                        }
                        if ((Math.abs(tempChangeInDirection) < 8) && !(Guidance.directionChangeToString(tempChangeInDirection, false).equals("Straight"))){
                            System.out.println("good change");
                            String tempDirectionChangeString = Guidance.directionChangeToString(tempChangeInDirection, vFlag);
                            tempTextDirection += ", then immediately turn " + tempDirectionChangeString;
                            tempMapNodes.add(tempFromNode);
                            tempNodeEdges.add(tempFromNode.getEdgeTo(tempToNode));
                            i++;
                        }
                    }
                }
                tempDirectionSteps.add(new DirectionStep(tempTextDirection, tempNodeEdges));
                tempNodeEdges = new LinkedList<>();
            } else if (directionChangeString.equals("up") || directionChangeString.equals("down")) {
                if(intersectionsPassed == 0){
                    if (vFlag) {
                        tempTextDirection = ("Take an elevator at the next intersection from " + fromNode.getNodeID() + " on floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirection = ("Take an elevator at the next intersection from floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    }
                }
                else if(intersectionsPassed == 1){
                    if (vFlag) {
                        tempTextDirection = ("After passing 1 intersection, take an elevator at " + fromNode.getNodeID() + " on floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirection = ("After passing 1 intersection, take an elevator from floor " + fromNode.getMyFloor().getFloorNumber() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    }
                }
                else {
                    if (vFlag) {
                        tempTextDirection = ("After passing " + intersectionsPassed + " intersections" + ", take the elevator" + " at " + fromNode.getNodeID() + " " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    } else {
                        tempTextDirection = ("After passing " + intersectionsPassed + " intersections" + ", take the elevator " + directionChangeString + " to floor " + toNode.getMyFloor().getFloorNumber());
                        tempMapNodes.add(fromNode);
                    }
                }
                tempNodeEdges.add(fromNode.getEdgeTo(toNode));
                tempDirectionSteps.addLast(new DirectionStep(tempTextDirection, tempNodeEdges));
                //this.listOfFloorSteps.getLast().setDirections(tempTextDirections);
                //this.listOfFloorSteps.getLast().setNodesForThisFloor(tempMapNodes);
                tempDirectionFloorSteps.addLast(new DirectionFloorStep(fromNode.getMyFloor(), tempMapNodes, tempDirectionSteps));
                tempDirectionSteps = new LinkedList<>();
                tempNodeEdges = new LinkedList<>();
                tempMapNodes = new LinkedList<>();
                intersectionsPassed = 0;
            }
        }

        //Add the destination arrival string
        if (intersectionsPassed >= 2) {
            if (vFlag) {
                tempTextDirection = ("After passing " + intersectionsPassed + " intersections, arrive at your destination: node " + toNode.getNodeID());
            } else {
                tempTextDirection = ("After passing " + intersectionsPassed + " intersections, arrive at your destination.");
            }
        } else if (intersectionsPassed == 1) {
            if (vFlag) {
                tempTextDirection = ("After passing 1 intersection, arrive at your destination: node " + toNode.getNodeID());
            } else {
                tempTextDirection = ("After passing 1 intersection, arrive at your destination.");
            }
        } else {
            if (vFlag) {
                tempTextDirection = ("Arrive at your destination: node " + toNode.getNodeID());
            } else {
                tempTextDirection = ("Arrive at your destination.");
            }
        }

        tempNodeEdges.add(fromNode.getEdgeTo(toNode));
        tempDirectionSteps.addLast(new DirectionStep(tempTextDirection, tempNodeEdges));
        //this.listOfFloorSteps.getLast().setDirections(tempTextDirections);
        //this.listOfFloorSteps.getLast().setNodesForThisFloor(tempMapNodes);
        tempMapNodes.add(toNode);
        tempDirectionFloorSteps.addLast(new DirectionFloorStep(fromNode.getMyFloor(), tempMapNodes, tempDirectionSteps));
        tempDirectionSteps = new LinkedList<>();
        tempNodeEdges = new LinkedList<>();
        tempMapNodes = new LinkedList<>();
        intersectionsPassed = 0;

        return tempDirectionFloorSteps;

    }
}
