package Domain.Map;

import java.util.HashSet;

/**
 * Represents a building with floors
 */
public class Building {

    HashSet<Floor> buildingFloors;

    /**
     * Creates a building with no floors
     */
    public Building() {
        this.buildingFloors = new HashSet<>();
    }

    /**
     * Adds a floor to this building, throwing an exception if the floor already exists
     * @param f the floor to be added
     * @throws Exception if the floor already exists
     */
    public void addFloor(Floor f) throws Exception {
        boolean floorExists;

        try {
            getFloor(f.floorNumber);
            floorExists = true;
        } catch (Exception e) {
            floorExists = false;
        }
        if(!floorExists) {
            buildingFloors.add(f);
        } else {
            throw new Exception("Floor already exists");
        }
    }

    /**
     * Retrieves the floor with the given floorNumber from this building's list of floors
     * @param floorNumber
     * @return floor with matching number
     */
    public Floor getFloor(int floorNumber) throws Exception {
        for (Floor f : buildingFloors) {
            if(f.getFloorNumber() == floorNumber) {
                return f;
            }
        }
        throw new Exception("Floor not found");
    }
}
