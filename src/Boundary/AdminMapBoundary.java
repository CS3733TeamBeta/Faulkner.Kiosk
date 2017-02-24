package Boundary;

import Domain.Map.*;
import Domain.ViewElements.DragIconType;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by benhylak on 2/24/17.
 */
public class AdminMapBoundary extends MapBoundary
{
    public void addMapChangeHandler(MapChangeListener<Node, Object> mapChangeListener)
    {
        mapElements.addListener(mapChangeListener);
    }

    public void selectFloor()
    {

    }

    public void selectBuilding()
    {

    }

    public void loadFloorElements()
    {
        mapElements.clear();

        for(MapNode n: currentFloor.getFloorNodes())
        {
            for(NodeEdge e : n.getEdges())
            {
                if(currentFloor.getFloorNodes().contains(e.getOtherNode(n)))
                {
                    if (!mapElements.containsKey(e.getEdgeLine()))
                    {
                        mapElements.put(e.getEdgeLine(), e);
                    }

                    e.updatePosViaNode(n);
                }
            }

            n.getNodeToDisplay().toFront();
        }
    }

    public void addNode(MapNode n)
    {
        if(n.getIconType().equals(DragIconType.Elevator))
        {
            addElevator(n);
        }
        else
        {
            if (!currentFloor.getFloorNodes().contains(n))
            {
                n.setFloor(currentFloor);
                currentFloor.addNode(n);

                mapElements.put(n.getNodeToDisplay(), n);
            }
        }

        //n.setPos(n.getPosX(), n.getPosY());
    }

    public void newEdge(MapNode source, MapNode target)
    {
        NodeEdge edge = new NodeEdge(source, target);
        mapElements.put(edge.getEdgeLine(), edge);
    }

    private void addElevator(MapNode n)
    {
        ArrayList<MapNode> nodesToAdd = new ArrayList<MapNode>();

        if (!mapElements.containsKey(n.getNodeToDisplay()))
        {
            mapElements.put(n.getNodeToDisplay(), n); //add to right panes children
        }

        MapNode last = null;

        MapNode e;

        for (Floor f : b.getFloors())
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

    public void remove(Node n)
    {
        mapElements.remove(n);
    }
}
