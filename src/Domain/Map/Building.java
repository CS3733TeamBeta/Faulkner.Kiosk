package Domain.Map;

import java.util.HashSet;

/**
 * Represents a building with floors
 */
public class Building {

    HashSet<Floor> buildingFloors;

    public Building() {
        this.buildingFloors = new HashSet<>();
    }

    public void addFloor(Floor f) throws Exception
    {
        boolean floorExists;

        try
        {
            getFloor(f.getFloorNumber());
            floorExists = true;
        }
        catch (Exception e)
        {
            floorExists = false;
        }

        if(!floorExists)
        {
            buildingFloors.add(f);
        }
        else
        {
            throw new Exception("Floor already exists");
        }
    }

    /**
     *
     * @param floorNumber
     * @return floor with matching number
     */
    public Floor getFloor(int floorNumber) throws Exception
    {
        for (Floor f : buildingFloors)
        {
            if(f.getFloorNumber() == floorNumber)
            {
                return f;
            }
        }

        throw new Exception("Floor not found");
    }
}
