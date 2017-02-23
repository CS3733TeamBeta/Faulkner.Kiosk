package Model;

import Domain.Map.*;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Model.Database.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IanCJ on 2/8/2017.
 */
public class MapModel {
    LinkedList<EdgeCompleteEventHandler> edgeCompleteHandlers;
    ArrayList<DragIcon> sideBarIcons;

    HashSet<MapNode> mapNodes;
    HashSet<NodeEdge> mapEdges;

    Hospital hospital;
    Floor currentFloor;

    Building building; //this kiosks building

    public MapModel() {
        edgeCompleteHandlers = new LinkedList<EdgeCompleteEventHandler>(); //instantiate empty linked list for handlers;

        sideBarIcons = new ArrayList<DragIcon>();
        mapNodes = new HashSet<MapNode>();
        mapEdges = new HashSet<NodeEdge>();
        currentFloor = new Floor(1);

        try
        {
            hospital = new DatabaseManager().loadData();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        /**@TODO HACKY **/

        for(Building b : hospital.getBuildings())
        {
            if(b.getName().equals("Faulkner")) //@TODO def gonna be a problem someday
            {
                building = b;
            }
        }

        try
        {
            setCurrentFloor(building.getFloor(1));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int incrementFloor(int incAmount)
    {
        int nextFloorID = getCurrentFloor().getFloorNumber() + incAmount;

        if(nextFloorID<=building.getFloors().size() && nextFloorID >0)
        {
            try
            {
                this.setCurrentFloor(building.getFloor(nextFloorID));
                return nextFloorID;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        return -1;
    }

    public int chooseNextFloor()
    {
       return incrementFloor(1);
    }

    public int choosePreviousFloor()
    {
        return incrementFloor(-1);
    }

    public void setCurrentFloor(Floor floor)
    {
        try
        {
            if(floor == building.getFloor(1))
            {
                this.currentFloor = hospital.getCampusFloor();
            }
            else
            {
                this.currentFloor = floor;
            }
        }
        catch (Exception e)
        {
            this.currentFloor = floor;
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
    public void addMapNode(MapNode n) {
        if(!mapNodes.contains(n)) {
            mapNodes.add(n);
            getCurrentFloor().addNode(n);
        }
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
    public void addMapEdge(NodeEdge e) {
        if(!mapEdges.contains(e)) {
            mapEdges.add(e);
            getCurrentFloor().addEdge(e);
        }
    }

    /**
     * Remove map edge from model
     * @param e map edge to remove
     */
    public void removeMapEdge(NodeEdge e)
    {
        mapEdges.remove(e);
    }

    public Hospital getHospital()
    {
        return hospital;
    }
}
