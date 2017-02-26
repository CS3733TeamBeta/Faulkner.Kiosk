package Boundary;

import Entity.Map.*;
import Controller.Map.ViewElements.DragIconType;
import javafx.geometry.Point2D;


import java.util.ArrayList;

/**
 * Created by benhylak on 2/24/17.
 */
public class AdminMapBoundary extends MapBoundary
{
    @Override
    public void changeFloor(Floor f)
    {
        super.changeFloor(f);

        edges.clear();

        for(MapNode n: nodesOnMap)
        {
            n.setOnDeleteRequested(e-> remove(n));

            for(NodeEdge edge: n.getEdges())
            {
                if (!edges.contains(edge) && nodesOnMap.contains(edge.getOtherNode(n)))
                {
                    edges.add(edge);
                }
            }
        }
    }

    /**
     * Function returns whether or not the node should be on the map, runs on floor change
     * In the parent class, this function is used to filter out connector nodes. On the admin side,
     * of course, we want to see connector nodes, hence the reason we always returns true.
     * @param n
     * @return
     */
    @Override
    protected boolean shouldBeOnMap(MapNode n)
    {
        return true;
    }

    public void newEdge(MapNode source, MapNode target)
    {
        NodeEdge edge = new NodeEdge(source, target);
        edges.add(edge);
    }

    public MapNode newNode(DragIconType type, Point2D loc)
    {
        MapNode n = new MapNode();
        n.setType(type);

        n.setPosX(loc.getX());
        n.setPosY(loc.getY());

        n.setOnDeleteRequested(e-> remove(n));

        currentFloor.getFloorNodes().add(n);

        nodesOnMap.add(n);

        return n;
    }

    private void addElevator(MapNode n)
    {
        ArrayList<MapNode> nodesToAdd = new ArrayList<MapNode>();

        MapNode last = null;

        MapNode e;

        for (Floor f : currentFloor.getBuilding().getFloors())
        {
            if (!f.equals(currentFloor))
            {
                e = new MapNode();

                e.setIsElevator(true);
                e.setPos(n.getPosX(), n.getPosY());

                f.addNode(e);

                nodesToAdd.add(e);
            }
            else
            {
                e = n;
                nodesToAdd.add(n);
            }

            if (last != null)
            {
                LinkEdge edge = new LinkEdge(last, e);
            }

            last = e;
        }
    }

    public void remove(MapNode n)
    {
        nodesOnMap.remove(n);
        currentFloor.removeNode(n);

        for(NodeEdge edge: n.getEdges())
        {
            edge.getOtherNode(n).getEdges().remove(edge);

            if(edges.contains(edge))
            {
                edges.remove(edge);
            }
        }
    }

    public void removeEdge(NodeEdge edge)
    {
        edge.getSource().getEdges().remove(edge);
        edge.getTarget().getEdges().remove(edge);

        edges.remove(edge);
    }

    public void moveNode(MapNode n, Point2D movedTo)
    {
        n.setPosX(movedTo.getX());
        n.setPosY(movedTo.getY());

        //update database

        for (NodeEdge edge : n.getEdges())
        {
            //edge.updatePosViaNode(boundary.getMapNode(n)); //@TODO
            edge.updateCost();
        }
    }
}
