package main.Map.Boundary;

import main.Map.Entity.*;
import main.Application.Database.DataCache;

import main.Map.Entity.Hospital;
import main.Map.Entity.NodeType;
import main.Map.Entity.Floor;
import main.Map.Entity.MapNode;
import main.Map.Entity.NodeEdge;
import main.Application.Database.DataCache;
import com.jfoenix.controls.JFXComboBox;
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
    private Building currentBuilding;

    private Floor campusFloor;

    private boolean firstLoad = true;

    MapNode kiosk;

    protected Hospital h;

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

    public MapBoundary(Hospital h)
    {
        this.h = h;
        nodesOnMap = FXCollections.observableSet(new HashSet<MapNode>());

        if(h.getCurrentKiosk()!=null)
        {
            kioskFloor = h.getCurrentKiosk().getMyFloor();
            currentBuilding = kioskFloor.getBuilding();
        }
        System.out.println(h.getCampusFloor().toString());
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
        if(f.getFloorNumber()==1)
        {
            //currentFloor = campusFloor;
        }
        else
        {
            currentFloor = f;
        }
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

    public void changeBuilding(Building b){
        currentBuilding = b;

        try {
            changeFloor(campusFloor);
        }catch(Exception e){
            System.out.println("ERROR IN SWITCHING BUILDINGS");
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
        if(kioskFloor==null)
        {
            System.out.println("You have not specififed a kiosk");
        }
        else if(nextFloorID<=currentBuilding.getFloors().size() &&( nextFloorID >0))
        {
            try
            {
                if(nextFloorID==1)
                {
                    changeFloor(getHospital().getCampusFloor());
                }
                else
                {
                    changeFloor(currentBuilding.getFloor(nextFloorID));
                }

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

    public Building getCurrentBuilding(){
        return this.currentBuilding;
    }

    public ObservableSet<MapNode> mapElements()
    {
        return nodesOnMap;
    }
}
