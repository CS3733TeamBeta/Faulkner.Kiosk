package Model;

import Domain.Map.Building;
import Domain.Map.Floor;
import Domain.Map.Hospital;
import Domain.Map.MapNode;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Domain.ViewElements.GraphicalMapNode;
import Domain.ViewElements.GraphicalNodeEdge;
import javafx.scene.Node;

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

    HashSet<GraphicalMapNode> mapNodes;
    HashSet<GraphicalNodeEdge> mapEdges;

    Hospital hospital;
    Floor currentFloor;

    public MapEditorModel()
    {
        edgeCompleteHandlers = new LinkedList<EdgeCompleteEventHandler>(); //instantiate empty linked list for handlers;

        sideBarIcons = new ArrayList<DragIcon>();
        mapNodes = new HashSet<GraphicalMapNode>();
        mapEdges = new HashSet<GraphicalNodeEdge>();

        hospital = new Hospital();

        Building b  = new Building();
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
    public void addNodeToCurrentFloor(GraphicalMapNode nodeToAdd)
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
    public void addMapNode(GraphicalMapNode n)
    {
        mapNodes.add(n);
    }

    /**
     * Remove map node from floor under edit
     * @param n
     */
    public void removeMapNodeFromCurrentFloor(GraphicalMapNode n)
    {
       getCurrentFloor().removeNode(n);
    }

    /**
     * Add map edge to model
     * @param e map edge to add
     */
    public void addMapEdge(GraphicalNodeEdge e)
    {
        mapEdges.remove(e);
    }

    /**
     * Remove map edge from model
     * @param e map edge to remove
     */
    public void removeMapEdge(GraphicalNodeEdge e)
    {
        mapEdges.remove(e);
    }
}
