package Model;

import Domain.ViewElements.DragIcon;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
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

    HashSet<Node> rightPaneNodes;

    public MapEditorModel()
    {
        edgeCompleteHandlers = new LinkedList<EdgeCompleteEventHandler>(); //instantiate empty linked list for handlers;

        sideBarIcons = new ArrayList<DragIcon>();
        rightPaneNodes = new HashSet<Node>();
    }

    public void addEdgeCompleteHandler(EdgeCompleteEventHandler e)
    {
        edgeCompleteHandlers.add(e);
    }

    public List<EdgeCompleteEventHandler> getEdgeCompleteHandlers()
    {
        return edgeCompleteHandlers;
    }

    public void addSideBarIcon(DragIcon iconToAdd)
    {
        sideBarIcons.add(iconToAdd);
    }

    public List<DragIcon> getSideBarIcons()
    {
        return sideBarIcons;
    }

    public void addToRightPane(Node n)
    {
        rightPaneNodes.add(n);
    }

    public HashSet<Node> getRightPaneChildren()
    {
        return rightPaneNodes;
    }

    public void removeNodeFromRightPane(Node n)
    {
        rightPaneNodes.remove(n);
    }
}
