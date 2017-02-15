package Model;

import Domain.Map.*;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by benhylak on 2/4/17.
 */
public class MapEditorModel
{
    LinkedList<EdgeCompleteEventHandler> edgeCompleteHandlers;
    ArrayList<DragIcon> sideBarIcons;

    HashSet<MapNode> mapNodes;
    HashSet<NodeEdge> mapEdges;

    Hospital hospital;
    Floor currentFloor;

    public MapEditorModel()
    {
        edgeCompleteHandlers = new LinkedList<EdgeCompleteEventHandler>(); //instantiate empty linked list for handlers;

        sideBarIcons = new ArrayList<DragIcon>();
        mapNodes = new HashSet<MapNode>();
        mapEdges = new HashSet<NodeEdge>();

        hospital = new Hospital();

        Building b  = new Building();
        Floor f1 = new Floor(1,1);

        try //attempts to add floor 1
        {
            b.addFloor(f1);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        hospital.addBuilding(b);

        currentFloor = f1;
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
