package main.Map.Controller;

import main.Application.ApplicationController;
import main.Map.Entity.Building;
import main.Map.Entity.Floor;
import main.Map.Entity.NodeEdge;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import jfxtras.labs.util.event.MouseControlUtil;

import java.io.IOException;
import java.util.ArrayList;

public class VisualBuilding
{
    Group group;
    Box baseFloor;
    ArrayList<Box> floors;
    Box topFloor;
    double opacity = 1;
    PhongMaterial defaultMaterial = new PhongMaterial(Color.rgb(0, 0, 200, .6));
    PhongMaterial highlightedMaterial = new PhongMaterial(Color.rgb(0, 0, 100, 1));

    Building b; //the building this visual building corresponds to

    public static VisualBuilding BuildingFactory(Building b)
    {
        if(b.getName().equals("Faulkner"))
        {
            return new VisualBuilding(b,150, 75, 10, 210, 190, 45);
        }
        else if(b.getName().equals("Belkin"))
        {
            return new VisualBuilding(40, 50, 10, 100, 70, 45);
        }
        else
        {
            return null;
        }
    }

    public VisualBuilding(Building b, double width, double height, double depth, double startingTranslationX, double startingTranslationY, double startingTranslationZ)
    {
        this(width, height,depth,startingTranslationX,startingTranslationY, startingTranslationZ);

        for(Floor f: b.getFloors())
        {
            createNewFloor();
        }
    }

    public VisualBuilding(double width, double height, double depth, double startingTranslationX, double startingTranslationY, double startingTranslationZ)
    {
        floors = new ArrayList<>();
        baseFloor = new Box(width, height, depth);
        group = new Group();

        group.addEventHandler(MouseEvent.MOUSE_CLICKED, e->
        {
            if(e.getClickCount()==2)
            {
                try
                {
                    ApplicationController.getController().switchToMapEditorView(b);
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        baseFloor.setTranslateX(startingTranslationX);
        baseFloor.setTranslateY(startingTranslationY);
        baseFloor.setTranslateZ(startingTranslationZ+10);

        baseFloor.setMaterial(defaultMaterial);

        baseFloor.setOnMouseEntered(e->
        {
            baseFloor.setMaterial(highlightedMaterial);
        });

        baseFloor.setOnMouseExited(e->
        {
            baseFloor.setMaterial(defaultMaterial);
        });

        setUpFloor(baseFloor);

        MouseControlUtil.makeDraggable(group,
                event ->
                {
                    group.setMouseTransparent(false);
                    group.setCursor(Cursor.DEFAULT);

                    for (Box floor : floors) {
                        floor.setTranslateX(baseFloor.getTranslateX() + baseFloor.getLayoutX());
                        floor.setTranslateY(baseFloor.getTranslateY() + baseFloor.getLayoutY());
                    }

                },

                null
        );

        topFloor = baseFloor;
    }

    public void setUpFloor(Box floor)
    {
        floor.setOnMouseClicked((MouseEvent event)->{
            createNewFloor();
        });
    }

    private Box createNewFloor()
    {
        Box floor = new Box(baseFloor.getWidth(), baseFloor.getHeight(), baseFloor.getDepth());

        floor.setMaterial(defaultMaterial);

        floor.setTranslateX(baseFloor.getTranslateX());
        floor.setTranslateY(baseFloor.getTranslateY());
        floor.setTranslateZ(topFloor.getTranslateZ() - baseFloor.getDepth());

        floor.setOnMouseEntered(e->
        {
            floor.setMaterial(highlightedMaterial);
        });

        floor.setOnMouseExited(e->
        {
            floor.setMaterial(defaultMaterial);
        });

        setUpFloor(floor);
        group.getChildren().add(floor);
        floors.add(floor);

        topFloor.setOnMouseClicked(null);

        topFloor = floor;

        return floor;
    }

    public Group getGroup()
    {
        return group;
    }

}