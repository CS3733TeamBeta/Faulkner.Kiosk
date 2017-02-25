package Domain.ViewElements;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import jfxtras.labs.util.event.MouseControlUtil;

import java.util.ArrayList;

public class VisualBuilding
{
    Box baseFloor;
    ArrayList<Box> floors;
    Box topFloor;

    public VisualBuilding(double width, double height, double depth, double startingTranslationX, double startingTranslationY, double startingTranslationZ)
    {
        floors = new ArrayList<Box>();
        baseFloor = new Box(width, height, depth);

        baseFloor.setTranslateX(startingTranslationX);
        baseFloor.setTranslateY(startingTranslationY);
        baseFloor.setTranslateZ(startingTranslationZ);

        baseFloor.setMaterial(new PhongMaterial(Color.RED));
        setUpFloor(baseFloor);

        topFloor = baseFloor;
    }

    public Box getBaseFloor()
    {
        return baseFloor;
    }

    public void setUpFloor(Box floor)
    {

        MouseControlUtil.makeDraggable(floor);

        // D&D starts
        floor.setOnDragDetected((MouseEvent event)-> {
            floor.setMouseTransparent(true);
            floor.setCursor(Cursor.MOVE);
            floor.startFullDrag();
        });

        // D&D ends
        floor.setOnMouseReleased((MouseEvent event)-> {
            floor.setMouseTransparent(false);
            floor.setCursor(Cursor.DEFAULT);
        });

        floor.setOnMouseEntered(event->
        {
            floor.setMaterial(new PhongMaterial(Color.YELLOW));
        });

        floor.setOnMouseClicked((MouseEvent event)->{
            
        });
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