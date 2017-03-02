package main.Map.Controller;

import main.Map.Boundary.MapBoundary;
import main.Map.Entity.MapNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.scene.Group;
import javafx.scene.image.ImageView;

/**
 * Created by benhylak on 2/25/17.
 */
public abstract class MapController
{
    protected BiMap<DragIcon, MapNode> iconEntityMap;

    protected Group mapItems = new Group();

    protected MapBoundary boundary;

    protected ImageView mapImage;

    protected static int mapWidth = 2300;
    protected static int mapHeight = 1656;

    public MapController()
    {
        mapImage = new ImageView();
        mapImage.setFitWidth(mapWidth);
        mapImage.setFitHeight(mapHeight);

        iconEntityMap = HashBiMap.create();
    }

    public void initBoundary()
    {
        boundary.addObserver( (o, arg) ->
        {
            mapImage.setImage(boundary.getCurrentFloor().getImage());
            System.out.println("Floor image updated");
        });

        boundary.addNodeSetChangeHandler(change ->
        {
            if(change.wasAdded())
            {
                importMapNode(change.getElementAdded());
            }
            else if(change.wasRemoved())
            {
                removeMapNode(change.getElementRemoved());
            }
        });
    }

    public void removeMapNode(MapNode n)
    {
        DragIcon icon = iconEntityMap.inverse().get(n);
        iconEntityMap.remove(icon);
        mapItems.getChildren().remove(icon);
    }

    protected DragIcon importMapNode(MapNode n)
    {
        DragIcon icon = new DragIcon();

        icon.setType(n.getType());
        icon.relocate(
                n.getPosX()-
                        icon.getPrefWidth()/2,
                n.getPosY()-
                        icon.getPrefHeight()/2);

        mapItems.getChildren().add(icon);
        iconEntityMap.put(icon, n);

        return icon;
    }
}
