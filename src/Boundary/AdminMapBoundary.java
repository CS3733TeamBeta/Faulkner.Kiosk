package Boundary;

import Domain.Map.*;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DragIconType;
import Model.Database.DataCache;
import javafx.collections.*;
import javafx.geometry.Point2D;
import javafx.scene.Node;


import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by benhylak on 2/24/17.
 */
public class AdminMapBoundary extends MapBoundary
{
    public void selectFloor(Floor f)
    {

    }

    public void addNode(MapNode n)
    {
        if(n.getType().equals(DragIconType.Elevator))
        {
            addElevator(n);
        }
        else
        {
            if (!currentFloor.getFloorNodes().contains(n))
            {
                currentFloor.addNode(n);
                nodesOnMap.add(n);
            }
        }

        n.setOnDeleteRequested(e-> remove(n));
    }

    @Override
    public void changeFloor(Floor f)
    {
        super.changeFloor(f);

        for(MapNode n: nodesOnMap)
        {
            for(NodeEdge edge: n.getEdges())
            {
                if (!edges.contains(edge) && nodesOnMap.contains(edge.getOtherNode(n)))
                {
                    edges.add(edge);
                }
            }
        }
    }

    public void newEdge(MapNode source, MapNode target)
    {
        NodeEdge edge = new NodeEdge(source, target);
        edges.add(edge);
    }

    public void newNode(DragIconType type, Point2D loc)
    {
        MapNode n = new MapNode();
        n.setType(type);

        n.setPosX(loc.getX());
        n.setPosY(loc.getY());

        currentFloor.getFloorNodes().add(n);

        nodesOnMap.add(n);
    }

    private void addElevator(MapNode n)
    {
        ArrayList<MapNode> nodesToAdd = new ArrayList<MapNode>();

       /* if (!nodesOnMap.contains(n))
        {
            //mapElements.contains(n); //add to right panes children
        }*/

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
