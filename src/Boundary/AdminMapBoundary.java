package Boundary;

import Domain.Map.*;
import Domain.ViewElements.DragIconType;
import Domain.ViewElements.Events.DeleteRequestedEvent;
import Domain.ViewElements.Events.DeleteRequestedHandler;
import javafx.collections.*;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;

/**
 * Created by benhylak on 2/24/17.
 */
public class AdminMapBoundary extends MapBoundary
{
    protected ObservableSet<NodeEdge> edges = FXCollections.observableSet(new HashSet<NodeEdge>());

    public void addMapChangeHandler(SetChangeListener<MapNode> mapChangeListener)
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
                    /*if (!mapElements.containsKey(e.getEdgeLine()))
                    {
                        mapElements.put(e.getEdgeLine(), e);
                    }

                    e.updatePosViaNode(n);*/
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

                mapElements.add(n);
            }
        }

        n.setOnDeleteRequested(e->
        {
            remove(n);
        });
    }

    public void newEdge(MapNode source, MapNode target)
    {
        NodeEdge edge = new NodeEdge(source, target);
        edges.add(edge);
    }

    private void addElevator(MapNode n)
    {
        ArrayList<MapNode> nodesToAdd = new ArrayList<MapNode>();

        if (!mapElements.contains(n))
        {
            mapElements.contains(n); //add to right panes children
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

    public void remove(MapNode n)
    {
        mapElements.remove(n);
    }
}
