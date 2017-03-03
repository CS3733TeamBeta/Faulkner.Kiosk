package main.Map.Entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by benhylak on 2/23/17.
 */
public class CampusFloor extends Floor
{
    ObservableList<MapNode> campusNodes;

    public CampusFloor()
    {
        super(1); //sorta hacky but should do the trick...
        campusNodes = FXCollections.observableList(new ArrayList<MapNode>());
        setFloorUUID(UUID.fromString("00000000-0000-00c1-0000-000000000000"));

    }

    @Override
    public void addNode(MapNode n)
    {
        campusNodes.add(n);
        super.addNode(n);
    }

    @Override
    public void removeNode(MapNode n)
    {
        campusNodes.remove(n);
        super.removeNode(n);
    }

    public void clearBuildings()
    {
        getFloorNodes().clear();

        for(MapNode n: campusNodes)
        {
            super.addNode(n);
        }
    }

    public ObservableList<MapNode> getCampusNodes()
    {
        return campusNodes;
    }

    public void importBuilding(Building b)
    {
        try
        {
            Floor ground = b.getBaseFloor();

            for(MapNode n: ground.getFloorNodes())
            {
                super.addNode(n, false); //make sure floor doesn't get set to campus floor
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString()
    {
        return "Campus";
    }

    public void setFloorID(UUID id) {
        this.floorID = id;
    }
}
