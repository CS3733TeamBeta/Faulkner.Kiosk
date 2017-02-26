package Domain.ViewElements;

import Domain.Map.NodeEdge;
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

        floor.setMaterial(new PhongMaterial((Color.color(Math.random(), Math.random(), Math.random()))));

        floor.setTranslateX(baseFloor.getTranslateX());
        floor.setTranslateY(baseFloor.getTranslateY());
        floor.setTranslateZ(topFloor.getTranslateZ() - baseFloor.getDepth());

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