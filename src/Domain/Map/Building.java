package Domain.Map;

import java.util.LinkedList;

/**
 * Represents a building with floors
 */
public class Building {

    LinkedList<Floor> buildingFloors;

    public Building() {
        this.buildingFloors = new LinkedList<Floor>();
    }

    public void addFloor(Floor f) {
        buildingFloors.add(f);
    }
}
