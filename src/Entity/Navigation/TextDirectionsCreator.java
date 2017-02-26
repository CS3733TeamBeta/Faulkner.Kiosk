package Entity.Navigation;

import Entity.Map.MapNode;
import Entity.Map.NodeEdge;

import java.util.LinkedList;

/**
 * Created by IanCJ on 2/22/2017.
 */
public class TextDirectionsCreator {

    LinkedList<DirectionStep> directionSteps;

    boolean vFlag;

    TextDirectionsCreator(LinkedList<MapNode> listNodes, LinkedList<NodeEdge> listEdges, int kioskDirection, boolean vFlag) {
        this.vFlag = vFlag;
        this.directionSteps = makeDirectionSteps(listNodes, listEdges, kioskDirection);
    }

    public LinkedList<DirectionStep> getDirectionSteps() {
        return this.directionSteps;
    }

    private LinkedList<DirectionStep> makeDirectionSteps(LinkedList<MapNode> listNodes, LinkedList<NodeEdge> listEdges, int kioskDirection) {
        LinkedList<DirectionStep> tempDirectionSteps = new LinkedList<>();
        LinkedList<String> tempTextDirections = new LinkedList<String>();
        LinkedList<MapNode> tempMapNodes = new LinkedList<>();
        int prevDirection = kioskDirection;
        MapNode fromNode = new MapNode();
        MapNode toNode = new MapNode();

        int intersectionsPassed = 0;

        //Add the first node to the textual directions
        if (vFlag) {
            tempTextDirections.add("Start at the Kiosk. (Node " + listNodes.get(0).getNodeID() + ")");
        } else {
            tempTextDirections.add("Start at the Kiosk, facing " + Guidance.numToDirection(kioskDirection));
        }

        for (int i = 0; i < listNodes.size() - 1; i++) {

            //For each set of two nodes
            fromNode = listNodes.get(i);
            toNode = listNodes.get(i+1);


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
                tempDirectionSteps.addLast(new DirectionStep(fromNode.getMyFloor(), tempTextDirections, tempMapNodes));
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
        tempDirectionSteps.addLast(new DirectionStep(fromNode.getMyFloor(), tempTextDirections, tempMapNodes));

        return tempDirectionSteps;

    }
}
