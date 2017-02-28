package Map.Entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * Represents a building with floors
 */
public class Building {

    String name;

    UUID buildID;

    ObservableSet<Floor> buildingFloors;
    /**
     * Creates a building with no floors
     */
    public Building() {
        this.buildingFloors = FXCollections.observableSet(new HashSet<Floor>());
    }

    Hospital hospital;

    public Building(String name)
    {
        this();
        this.name = name;
        this.buildID = UUID.randomUUID();
    }

    public Building(UUID id, String name) {
        this();
        this.buildID = id;
        this.name = name;
    }

    public void setHospital(Hospital h)
    {
        hospital = h;
    }

    public Hospital getHospital()
    {
        return hospital;
    }

    public Floor newFloor()
    {
        Floor f = new Floor(buildingFloors.size() + 1);

        try
        {
            addFloor(f);
        }
        catch (Exception e)
        {

        }

        return f;
    }
    /**
     * Adds a floor to this building, throwing an exception if the floor already exists
     * @param f the floor to be added
     * @throws Exception if the floor already exists
     */
    public void addFloor(Floor f) throws Exception {
        boolean floorExists;

        try
        {
            getFloor(f.getFloorNumber());
            floorExists = true;
        } catch (Exception e) {
            floorExists = false;
        }
        if(!floorExists) {
            buildingFloors.add(f);
        } else {
            throw new Exception("Floor already exists");
        }

        f.setBuilding(this);
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

    public Collection<Floor> getFloors()
    {
        return buildingFloors;
    }

    /**
     *
     * @return name of building
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name Set name of building
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public UUID getBuildID() {
        return this.buildID;
    }

}
