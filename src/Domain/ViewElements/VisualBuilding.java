package Domain.ViewElements;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import jfxtras.labs.util.event.MouseControlUtil;

import java.util.ArrayList;

public class VisualBuilding
{
    Group group;
    Box baseFloor;
    ArrayList<Box> floors;
    Box topFloor;

    public VisualBuilding(double width, double height, double depth, double startingTranslationX, double startingTranslationY, double startingTranslationZ)
    {
        floors = new ArrayList<>();
        baseFloor = new Box(width, height, depth);
        group = new Group();

        baseFloor.setTranslateX(startingTranslationX);
        baseFloor.setTranslateY(startingTranslationY);
        baseFloor.setTranslateZ(startingTranslationZ);

        baseFloor.setMaterial(new PhongMaterial(Color.RED));
        setUpFloor(baseFloor);

        group.getChildren().add(baseFloor);

        MouseControlUtil.makeDraggable(baseFloor);

        // D&D starts
        baseFloor.setOnDragDetected((MouseEvent event) -> {

            for (Box f : floors)
            {
                f.setTranslateX(baseFloor.getTranslateX() + baseFloor.getLayoutX()); // this is tha broken part
                f.setTranslateY(baseFloor.getTranslateY() + baseFloor.getLayoutY()); // devon
            }

        });

        topFloor = baseFloor;
    }

    public void setUpFloor(Box floor)
    {

        // D&D ends
        floor.setOnMouseReleased((MouseEvent event)-> {
            floor.setMouseTransparent(false);
            floor.setCursor(Cursor.DEFAULT);
        });

        floor.setOnMouseEntered(event->
        {

        });

        floor.setOnMouseClicked((MouseEvent event)->{
            createNewFloor();
        });
    }

    private Box createNewFloor()
    {
        Box floor = new Box(baseFloor.getWidth(), baseFloor.getHeight(), baseFloor.getDepth());

        floor.setMaterial(new PhongMaterial((Color.color(Math.random(), Math.random(), Math.random()))));

        floor.setTranslateX(baseFloor.getTranslateX());
        floor.setTranslateY(baseFloor.getTranslateY());
        floor.setTranslateZ(topFloor.getTranslateZ() - baseFloor.getDepth());

        setUpFloor(floor);
        group.getChildren().add(floor);
        floors.add(floor);
        topFloor = floor;

        return floor;
    }

    public Group getGroup()
    {
        return group;
    }

}