package Model;

import Domain.Map.*;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import javafx.scene.control.Tab;

import java.util.*;

/**
 * Created by benhylak on 2/4/17.
 */
public class MapEditorModel
{
    LinkedList<EdgeCompleteEventHandler> edgeCompleteHandlers;
    ArrayList<DragIcon> sideBarIcons;

    HashSet<MapNode> mapNodes;
    HashSet<NodeEdge> mapEdges;
    HashMap<Tab, Building> buildingTabMap;

    Hospital hospital;
    Floor currentFloor;

    public MapEditorModel()
    {
        edgeCompleteHandlers = new LinkedList<EdgeCompleteEventHandler>(); //instantiate empty linked list for handlers;

        sideBarIcons = new ArrayList<DragIcon>();
        mapNodes = new HashSet<MapNode>();
        mapEdges = new HashSet<NodeEdge>();

        hospital = new Hospital();

        Building b  = new Building("Building 1");

        Floor f1 = new Floor(1);

        try //attempts to add floor 1
        {
            b.addFloor(f1);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        hospital.addBuilding(b);

        currentFloor = f1;

        buildingTabMap = new HashMap<>();
    }

    public void makeNewBuilding(String name, Tab tab)
    {
        hospital.addBuilding(new Building());

    }

    public void addBuilding(Building b, Tab tab)
    {
        if(!hospital.containsBuilding(b))
        {
            hospital.addBuilding(b);
        }

        buildingTabMap.put(tab, b);
    }

    public Building getBuildingFromTab(Tab t)
    {
        return buildingTabMap.get(t);
    }

    public boolean containsBuilding(Building b)
    {
        return hospital.containsBuilding(b);
    }

    public int getBuildingCount()
    {
        return hospital.buildingCount();
    }

    public Hospital getHospital()
    {
        return hospital;
    }

    public void setCurrentFloor(Floor floor){
        this.currentFloor = floor;
        mapNodes.clear();

        for(MapNode n : floor.getFloorNodes()) {
            mapNodes.add(n);
        }

        mapEdges.clear();
        for(NodeEdge m : floor.getFloorEdges()) {
            mapEdges.add(m);
        }
    }

    public Floor getCurrentFloor()
    {
        return currentFloor;
    }

    /**
     * Adds a node to the current floor
     *
     * @param nodeToAdd The node to add to the floor
     */
    public void addNodeToCurrentFloor(MapNode nodeToAdd)
    {
        getCurrentFloor().addNode(nodeToAdd);
    }

    /**
     * Place holder for function to switch floors
     * @param f
     */
    public void switchFloor(Floor f)
    {

    }

    /**
     * Adds a handler to handle the edge complete event raised when two nodes are connected
     * @param e
     */
    public void addEdgeCompleteHandler(EdgeCompleteEventHandler e)
    {
        edgeCompleteHandlers.add(e);
    }

    public List<EdgeCompleteEventHandler> getEdgeCompleteHandlers()
    {
        return edgeCompleteHandlers;
    }

    //Add icon to side bar
    public void addSideBarIcon(DragIcon iconToAdd)
    {
        sideBarIcons.add(iconToAdd);
    }

    //get icons in side bar
    public List<DragIcon> getSideBarIcons()
    {
        return sideBarIcons;
    }

    /**
     * Add map node to model
     *
     * @param n
     */
    public void addMapNode(MapNode n)
    {
        mapNodes.add(n);
    }

    /**
     * Remove map node from floor under edit
     * @param n
     */
    public void removeMapNodeFromCurrentFloor(MapNode n)
    {
       getCurrentFloor().removeNode(n);
    }

    /**
     * Add map edge to model
     * @param e map edge to add
     */
    public void addMapEdge(NodeEdge e)
    {
        mapEdges.remove(e);
    }

    /**
     * Remove map edge from model
     * @param e map edge to remove
     */
    public void removeMapEdge(NodeEdge e)
    {
        mapEdges.remove(e);
    }
}
