package Domain.Map;

/**
 * Elevators allow for navigation inbetween floors.
 */
public class Elevator extends Domain.Map.Destination
{
    Elevator elevatorAbove;
    Elevator elevatorBelow;

    /**
     * Sets the elevator that is on floor directly above (should only be set once)
     *
     * @param above Elevator on floor directly above this elevator
     *
     * @TODO make sure that this hasn't been set yet, throw exception
     */
    public void setElevatorAbove(Elevator above)
    {
        LinkEdge edgeToElevator = new LinkEdge(this, above);
        elevatorAbove = above;

        edges.add(edgeToElevator);
    }

    /**
     * Sets the elevator that is on floor directly below (should only be set once)
     *
     * @param below Elevator on floor directly below this elevator
     *
     * @TODO make sure that this hasn't been set yet, throw exception
     */
    public void setElevatorBelow(Elevator below)
    {
        elevatorBelow = below;
    }

    /*
    public MapNode goToFloor(Floor f)
    {
        return null;
    }*/
}
