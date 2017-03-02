package main.Map.Boundary;

import main.Map.Entity.*;
import javafx.collections.*;

import java.util.HashSet;
import java.util.Observable;

/**
 * Created by benhylak on 2/24/17.
 */
public class MapBoundary extends Observable
{
    protected ObservableSet<MapNode> nodesOnMap;
    protected ObservableSet<NodeEdge> edges = FXCollections.observableSet(new HashSet<NodeEdge>());

    protected Floor currentFloor;
    private Floor kioskFloor;
    protected Building currentBuilding;

    Floor campusFloor;
    MapNode kiosk;

    protected Hospital h;

    public void changeBuilding(Building b)
    {
        currentBuilding = b;

        if(currentFloor.getFloorNumber()!=1) //if not already on campus floor
        {
            changeFloor(b.getFloor(1));
        }
    }

    public static enum UpdateType
    {
        FloorChange
    }

    public Building getCurrentBuilding()
    {
        return currentBuilding;
    }

    public void addNodeSetChangeHandler(SetChangeListener<MapNode> mapChangeListener)
    {
        nodesOnMap.addListener(mapChangeListener);
    }

    public void addEdgeSetChangeHandler(SetChangeListener<NodeEdge> edgeChangeListener)
    {
        edges.addListener(edgeChangeListener);
    }

    public MapBoundary(Hospital h)
    {
        this.h = h;
        nodesOnMap = FXCollections.observableSet(new HashSet<MapNode>());

        if(h.getCurrentKiosk()!=null)
        {
            kiosk = getHospital().getCurrentKiosk();
            kioskFloor = h.getCurrentKiosk().getMyFloor();
            currentBuilding = kioskFloor.getBuilding();
        }

        campusFloor = h.getCampusFloor();
    }

    public void setInitialFloor()
    {
        try
        {
            if(h.getCurrentKiosk()!=null)
            {
                changeFloor(h.getCurrentKiosk().getMyFloor());
            }
            else{
                changeFloor(h.getBuildings().iterator().next().getFloor(1));
            }

            currentBuilding = currentFloor.getBuilding();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeFloor(Floor f)
    {
        if(currentFloor==null || !(f.getFloorNumber()==1 && currentFloor==campusFloor)) //if not already on campus floor
        {
            currentFloor = f;

            nodesOnMap.clear();

            for(MapNode n: currentFloor.getFloorNodes())
            {
                if(shouldBeOnMap(n))
                {
                    nodesOnMap.add(n);
                }
            }

            setChanged();
            notifyObservers(UpdateType.FloorChange);
        }
    }

    protected boolean shouldBeOnMap(MapNode n)
    {
        return (n.getType()!= NodeType.Connector);
    }

    public Hospital getHospital()
    {
        return h;
    }

    private int incrementFloor(int incAmount)
    {
        int nextFloorID = currentFloor.getFloorNumber() + incAmount;

        if(currentBuilding==null)
        {
            System.out.println("You have not specififed a building");
        }
        else if(nextFloorID<=currentBuilding.getFloors().size() &&( nextFloorID >0))
        {
            try
            {
                changeFloor(currentBuilding.getFloor(nextFloorID));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * Changes to previous floor
     *
     * @return New Floor Number
     */
    public int changeToPreviousFloor()
    {
        int result = incrementFloor(-1);
        return result;
    }

    /**
     * Changes to next floor
     *
     * @return New floor number
     */
    public int changeToNextFloor()
    {
        int result = incrementFloor(1);
        return result;
    }

    public void switchFloor()
    {

    }

    public void refreshNodes()
    {

    }

    public Floor getCampusFloor(){
        return campusFloor;
    }

    public Floor getCurrentFloor()
    {
        return currentFloor;
    }


    public ObservableSet<MapNode> mapElements()
    {
        return nodesOnMap;
    }
}
