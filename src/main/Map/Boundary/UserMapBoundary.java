package main.Map.Boundary;

import main.Application.ApplicationController;
import main.Application.Exceptions.PathFindingException;
import main.Map.Entity.Building;
import main.Map.Entity.Floor;
import main.Map.Entity.Hospital;
import main.Map.Entity.MapNode;
import main.Map.Navigation.Guidance;

/**
 * Created by benhylak on 3/1/17.
 */
public class UserMapBoundary extends MapBoundary
{
    Guidance currentGuidance;
    protected Floor kioskFloor;
    protected Floor campusFloor;

    private MapNode kiosk;
    private Building currentBuilding;

    public void setInitFloor()
    {
        if(kioskFloor!=null)
        {
            changeFloor(kioskFloor);
        }
        else
        {
            Building b = ApplicationController.getHospital().getBuildings().iterator().next();

            currentBuilding =b;

            changeFloor(b.getFloor(1));
        }
    }

    public UserMapBoundary(Hospital h)
    {
        super(h);

        if(h.getCurrentKiosk()!=null)
        {
            kiosk = getHospital().getCurrentKiosk();
            kioskFloor = h.getCurrentKiosk().getMyFloor();
            currentBuilding = kioskFloor.getBuilding();
        }

        campusFloor = h.getCampusFloor();
    }

    public Guidance findPathToNode(MapNode endPoint)
    {
        MapNode startPoint = getHospital().getCurrentKiosk();

        if (startPoint == null)
        {
            System.out.println("ERROR: NO KIOSK NODE SET ON USERSIDE. SETTING ONE RANDOMLY.");
            startPoint = campusFloor.getFloorNodes().iterator().next();
        }

        if (endPoint == startPoint)
        {
            System.out.println("ERROR; CANNOT FIND PATH BETWEEN SAME NODES");
            return null;//TODO add error message of some kind
        }

        try
        {
            currentGuidance = new Guidance(startPoint, endPoint, "North");
        } catch (PathFindingException e)
        {
            System.out.println("Exception in guidance");
            return null;
        }

        return currentGuidance;
    }

    public Guidance getCurrentGuidance()
    {
        return currentGuidance;
    }
}
