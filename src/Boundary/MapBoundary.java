package Boundary;

import Domain.Map.Building;
import Domain.Map.Floor;
import Domain.Map.MapNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;

/**
 * Created by benhylak on 2/24/17.
 */
public abstract class MapBoundary extends Observable
{
   // protected ObservableList<Node> mapElements;
    protected ObservableSet<MapNode> mapElements;

    HashMap<Node, Object> hashMap = new HashMap<Node, Object>();

    protected Floor currentFloor;
    MapNode kiosk;
    Building b;

    public MapBoundary()
    {
        currentFloor = new Floor(1);
        Building b = new Building("Test");

        try
        {
            b.addFloor(currentFloor);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        mapElements = FXCollections.observableSet(new HashSet<MapNode>());

    }

    public void switchFloor()
    {

    }

    public void refreshNodes()
    {

    }

    public ObservableSet<MapNode> mapElements()
    {
        return mapElements;
    }

}
