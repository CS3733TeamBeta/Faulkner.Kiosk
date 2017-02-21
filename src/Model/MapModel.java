package Model;

import Domain.Map.*;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Model.Database.DatabaseManager;

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

    public MapModel() {
        edgeCompleteHandlers = new LinkedList<EdgeCompleteEventHandler>(); //instantiate empty linked list for handlers;

        sideBarIcons = new ArrayList<DragIcon>();
        mapNodes = new HashSet<MapNode>();
        mapEdges = new HashSet<NodeEdge>();
        currentFloor = new Floor(1);

        hospital = DatabaseManager.getInstance().Faulkner;

        /**@TODO HACKY **/

        Floor arbitraryFloor;

        for(Building b : hospital.getBuildings())
        {
            try
            {
                arbitraryFloor = b.getFloor(3);
                setCurrentFloor(arbitraryFloor);
                break;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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
}
