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
        floors.add(baseFloor);

        MouseControlUtil.makeDraggable(baseFloor);

        // D&D starts
        baseFloor.setOnDragDetected((MouseEvent event) -> {

            System.out.println("Drag detected");

            baseFloor.setMouseTransparent(true);
            baseFloor.setCursor(Cursor.MOVE);
            for (Box f : floors)
            {
                System.out.println("moving floor");
                f.setTranslateX(baseFloor.getLayoutX()); // this is tha broken part
                f.setTranslateY(baseFloor.getLayoutY()); // devon
            }

        });

        topFloor = baseFloor;
    }

    public Box getBaseFloor()
    {
        return baseFloor;
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

/*
		ArrayList<Color> colors = new ArrayList<>();

		colors.add(Color.PURPLE);
		colors.add(Color.BLUE);
		colors.add(Color.GOLDENROD);
		colors.add(Color.SADDLEBROWN);

		for (int index = 0; index < colors.size(); index ++)
		{
			final Box addedBox = new Box(baseBox.getWidth(), baseBox.getHeight(), baseBox.getDepth());

			addedBox.setTranslateX(baseBox.getTranslateX());
			addedBox.setTranslateY(baseBox.getTranslateY());

			double offset = floor.getTranslateZ() - ((baseBox.getTranslateZ() + baseBox.getDepth()) - index * baseBox.getDepth());

			addedBox.setTranslateZ(offset);

			addedBox.setMaterial(new PhongMaterial(colors.get(index)));
			root.getChildren().add(addedBox);

		}

 */