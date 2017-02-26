package Boundary;

import Entity.Hospital;
import Entity.Map.*;
import Controller.Map.ViewElements.DragIconType;
import Model.Database.DataCache;
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
    MapNode kiosk;

    protected Hospital h = DataCache.getInstance().getHospital();

    public static enum UpdateType
    {
        FloorChange
    }

    public void addNodeSetChangeHandler(SetChangeListener<MapNode> mapChangeListener)
    {
        nodesOnMap.addListener(mapChangeListener);
    }

    public void addEdgeSetChangeHandler(SetChangeListener<NodeEdge> edgeChangeListener)
    {
        edges.addListener(edgeChangeListener);
    }

    public MapBoundary()
    {
        nodesOnMap = FXCollections.observableSet(new HashSet<MapNode>());
    }

    public void setInitialFloor()
    {
        try
        {
            changeFloor(h.getBuildings().iterator().next().getFloor(1));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeFloor(Floor f)
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

    protected boolean shouldBeOnMap(MapNode n)
    {
        return (n.getType()!= DragIconType.Connector);
    }

    public Hospital getHospital()
    {
        return h;
    }

    private int incrementFloor(int incAmount)
    {
        int nextFloorID = currentFloor.getFloorNumber() + incAmount;

        if(nextFloorID<=currentFloor.getBuilding().getFloors().size() && nextFloorID >0)
        {
            try
            {
                changeFloor(currentFloor.getBuilding().getFloor(nextFloorID));
                return nextFloorID;
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
        int result = incrementFloor(-1);
        return result;
    }

    public void switchFloor()
    {

    }

    public void refreshNodes()
    {

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
