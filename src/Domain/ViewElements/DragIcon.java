package Domain.ViewElements;

import java.io.IOException;

import Domain.Map.Destination;
import Domain.Map.Elevator;
import Domain.Map.MapNode;
import Domain.Map.Office;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Map;

/**
 *
 */
public class DragIcon extends AnchorPane{
	
	@FXML AnchorPane root_pane;

	private DragIconType mType = null;

	public DragIcon() {

		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/Admin/MapBuilder/DragIcon.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
	}
	
	@FXML
	private void initialize() {

	}

	/**
	 * Relocates the drag icon to a specific point
	 * @param p point to relocate to
	 */
	public void relocateToPoint (Point2D p) {

		//relocates the object to a point that has been converted to
		//scene coordinates
		Point2D localCoords = getParent().sceneToLocal(p);
		
		relocate ( 
				(int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
				(int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
			);
	}

	/**
	 *
	 * @return the type of this drag icon
	 */
	public DragIconType getType () { return mType; }

	/**
	 * Sets the type of this drag icon, changes picture accordingly
	 * @param type of drag icon
	 */
	public void setType (DragIconType type) {
		
		mType = type;
		
		getStyleClass().clear();
		getStyleClass().add("dragicon");
		
		switch (mType) {

			case bathroom:
				getStyleClass().add("bathroom");
				break;

			case store:
				getStyleClass().add("store");
				break;

			case elevator:
				getStyleClass().add("elevator");
				break;

			case doctor:
				getStyleClass().add("doctor");
				break;

			case food:
				getStyleClass().add("food");
				break;

			case info:
				getStyleClass().add("info");
				break;

			case connector:
				getStyleClass().add("connector");
				break;

			default:
				break;
		}
	}

	public static MapNode constructMapNodeFromType(DragIconType type)
	{
		MapNode newNode;

		switch (type)
		{
			case elevator:
			{
				newNode = new Elevator();
				break;
			}

			case doctor: //@TODO change this to office
			{
				newNode = new Office();
				break;
			}

			case connector:
			{
				newNode = new MapNode();
				break;
			}

			default:
			{
				newNode = new Destination();
			}

		}

		return newNode;
	}
}
