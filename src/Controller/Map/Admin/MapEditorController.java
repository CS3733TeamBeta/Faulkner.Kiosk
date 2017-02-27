package Controller.Map.Admin;

import Boundary.AdminMapBoundary;
import Controller.Map.MapController;
import Controller.SceneSwitcher;
import Entity.Map.*;
import Controller.Map.ViewElements.DragIcon;
import Controller.Map.ViewElements.DragIconType;
import Controller.Map.ViewElements.Events.EdgeCompleteEvent;
import Controller.Map.ViewElements.Events.EdgeCompleteEventHandler;

import Model.DataSourceClasses.TreeViewWithItems;
import Model.Database.DataCache;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import jfxtras.labs.util.event.MouseControlUtil;
import jfxtras.scene.menu.CirclePopupMenu;
import org.controlsfx.control.PopOver;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MapEditorController extends MapController
{
	public ScrollPane scroll_pane;
	@FXML AnchorPane mapPane;
	@FXML AnchorPane root_pane;

	@FXML
	Button newBuildingButton;

	@FXML
	Button newFloorButton;

	@FXML
	private TabPane BuildingTabPane;

	NodeEdge drawingEdge;
	Line drawingEdgeLine;

	BiMap<Tab, Floor> tabFloorMap;

	BiMap<Line, NodeEdge>  edgeEntityMap;
	CirclePopupMenu menu;

	Point2D menuOpenLoc = null;

	final ImageView target = new ImageView("@../../crosshair.png");
	ArrayList<EdgeCompleteEventHandler> edgeCompleteHandlers = new ArrayList<>();

	AdminMapBoundary adminBoundary;

	Rectangle selectionRectangle;

	HashSet<DragIcon> selectedIcons;

	boolean drawingEdgeFrozen = false;

	public MapEditorController()
	{
		super();

		selectedIcons = new HashSet<>();

		boundary = new AdminMapBoundary();
		adminBoundary = (AdminMapBoundary)boundary;

		edgeEntityMap = HashBiMap.create();
		tabFloorMap = HashBiMap.create();

		//Runs once the edge is drawn from one node to another
		//connects the two, sends sources, positions them etc.

		edgeCompleteHandlers.add(event->
		{
			MapNode sourceNode = event.getNodeEdge().getSource();
			MapNode targetNode = event.getNodeEdge().getTarget();

			adminBoundary.newEdge(sourceNode, targetNode);

			drawingEdgeLine.setVisible(false);
			drawingEdge = null;

			mapPane.setOnMouseMoved(null);
			mapImage.toBack();

			makeIconDraggable(iconEntityMap.inverse().get(sourceNode));

			System.out.println("Edge complete");
		});
	}

	/**
	 * FXML initialize function
	 */
	@FXML
	private void initialize()
	{
		Rectangle selectionRect = new Rectangle();
		selectionRect.setFill(Color.GREY);
		selectionRect.setOpacity(.3);
		selectionRect.setStroke(Color.BLACK);

		ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();

		mapPane.setOnMousePressed(e -> {
			mouseAnchor.set(new Point2D(e.getX(), e.getY()));
			selectionRect.setX(e.getX());
			selectionRect.setY(e.getY());
			selectionRect.setWidth(0);
			selectionRect.setHeight(0);
		});

		mapPane.setOnMouseDragged(e -> {
			selectionRect.setX(Math.min(e.getX(), mouseAnchor.get().getX()));
			selectionRect.setY(Math.min(e.getY(), mouseAnchor.get().getY()));
			selectionRect.setWidth(Math.abs(e.getX() - mouseAnchor.get().getX()));
			selectionRect.setHeight(Math.abs(e.getY() - mouseAnchor.get().getY()));
		});

		mapPane.setOnMouseReleased(e -> {
			selectedIcons.clear();

			selectedIcons.addAll(
					iconEntityMap.keySet().stream()
							.filter(r -> r.getBoundsInParent().intersects(selectionRect.getBoundsInParent()))
							.collect(Collectors.toList()));


			for(DragIcon icon: selectedIcons)
			{
				Glow glow = new Glow();
				glow.setLevel(.3);

				icon.setEffect(glow);
			}

			selectionRect.setWidth(0);
			selectionRect.setHeight(0);
		});

		mapPane.getChildren().add(selectionRect);

		selectionRectangle = new Rectangle();

		AnchorPane pane = new AnchorPane();
		pane.setPrefHeight(2000);
		pane.setPrefWidth(2000);
		pane.relocate(0, 0);


		mapPane.getChildren().add(pane);

		//MouseControlUtil.addSelectionRectangleGesture(pane, selectionRectangle);


		drawingEdgeLine = new Line();
		drawingEdgeLine.setStrokeWidth(5);

		mapPane.getChildren().add(mapImage);
		mapPane.getChildren().add(mapItems);
		mapPane.getChildren().add(drawingEdgeLine);

		drawingEdgeLine.setVisible(false);

		BuildingTabPane.getTabs().clear();

		initBoundary();

		adminBoundary.addEdgeSetChangeHandler(change->
		{
			if(change.wasAdded())
			{
				onEdgeAdded(change.getElementAdded());
			}
			else if(change.wasRemoved())
			{
				onEdgeRemoved(change.getElementRemoved());
			}
		});

		adminBoundary.setInitialFloor();

		buildRadialMenu(); //instantiates and populates the radial menu

		menu.shownProperty().addListener((e, oldValue, newValue)-> //when the menu is closed, hide the target icon
		{
			if(!newValue)
			{
				target.setVisible(false);
				drawingEdgeFrozen = false;
			}
			else
			{
				drawingEdgeFrozen = true;
			}
		});

		root_pane.setOnKeyPressed(keyEvent-> { //handle escaping from edge creation
			if (drawingEdge != null && keyEvent.getCode() == KeyCode.ESCAPE) {
				if(drawingEdgeLine.isVisible()) //and the right pane has the drawing edge as child
				{
					drawingEdgeLine.setVisible(false);
				}

				makeIconDraggable(iconEntityMap.inverse().get(drawingEdge.getSource()));
				drawingEdge = null;
			}
			else if(drawingEdge!=null && keyEvent.getCode() == KeyCode.A) //radial menu chain linking
			{
				Point2D location= new Point2D(MouseInfo.getPointerInfo().getLocation().getX(),
						MouseInfo.getPointerInfo().getLocation().getY());

				showRadialMenu(mapPane.screenToLocal(location));
			}
		});

		mapPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event->
		{
			Node clickedNode = event.getPickResult().getIntersectedNode();

			if(!iconEntityMap.containsKey(clickedNode) && !edgeEntityMap.containsKey(clickedNode))
			{
				if(!drawingEdgeLine.isVisible()) //radial menu requested
				{
					showRadialMenu(new Point2D(event.getX(), event.getY()));
				}
				else //Chain Linking
				{
					chainLink(clickedNode, new Point2D(event.getX(), event.getY()));
				}
			}
			else if(edgeEntityMap.containsKey(clickedNode)) //dropped in the middle of a line
			{
				dropOnLine(clickedNode, new Point2D(event.getX(), event.getY()));
			}
		});

		for(Floor f: boundary.getCurrentFloor().getBuilding().getFloors()) //makes a floor tab for each floor in the building
		{
			makeFloorTab(f);
		}

		BuildingTabPane.getSelectionModel().selectedItemProperty().addListener(
				(ov, newvalue, oldvalue)->{
					boundary.changeFloor(tabFloorMap.get(oldvalue)); //called when floor tab is selected

		});

		target.setVisible(false); //hides target
		target.setFitWidth(14); //sets width
		target.setFitHeight(14); //sets height

		mapPane.getChildren().add(target); //adds to map pane

		BuildingTabPane.getTabs().sort(Comparator.comparing(Tab::getText)); //puts the tabs in order
	}

	protected void chainLink(Node n, Point2D point)
	{
		MapNode mapNode = adminBoundary.newNode(DragIconType.Connector, point);
		drawingEdge.setTarget(mapNode);

		onEdgeComplete();
		onStartEdgeDrawing(mapNode);
	}

	protected void showRadialMenu(Point2D point)
	{
		Point2D screenCoords = mapPane.localToScreen(point);

		menu.show(screenCoords.getX(), screenCoords.getY());

		target.setVisible(true);

		target.setX(point.getX() - target.getFitWidth() / 2);
		target.setY(point.getY() - target.getFitHeight() / 2);

		menuOpenLoc = point;
	}

	protected void dropOnLine(Node n, Point2D point)
	{
		NodeEdge edge = edgeEntityMap.get(n);

		MapNode node = adminBoundary.newNode(DragIconType.Connector, point);

		MapNode source = edge.getSource();
		MapNode target = edge.getTarget();

		adminBoundary.newEdge(source, node);
		adminBoundary.newEdge(target, node);
		adminBoundary.removeEdge(edge);

		if(drawingEdgeLine.isVisible()) //if also drawing to the middle of the line
		{
			drawingEdge.setTarget(node);
			onEdgeComplete();
		}

		onStartEdgeDrawing(node);
	}

	/**
	 * Adds new map node to canvas
	 *
	 * @param n
	 * @return
	 */
	@Override
	protected DragIcon importMapNode(MapNode n)
	{
		DragIcon icon = super.importMapNode(n);
		addEventHandlersToNode(icon);

		return icon;
	}

	/**
	 * Builds the radial menu used on click
	 */
	public void buildRadialMenu()
	{
		menu = new CirclePopupMenu(mapPane, null);

		for (int i = 0; i < DragIconType.values().length; i++)
		{
			DragIcon icn = new DragIcon();
			icn.setType(DragIconType.values()[i]);

			if(icn.getType().equals(DragIconType.Connector))
			{
				icn.setPrefWidth(20);
				icn.setPrefHeight(20);
			}
			else
			{
				icn.setPrefWidth(32);
				icn.setPrefHeight(32);
			}

			MenuItem item = new MenuItem(DragIconType.values()[i].name(), icn);

			icn.setOnMouseClicked(event -> {
				MapNode n = adminBoundary.newNode(icn.getType(), menuOpenLoc);

				if(drawingEdgeLine.isVisible())
				{
					drawingEdge.setTarget(n);
					onEdgeComplete();
					onStartEdgeDrawing(n);
				}

				target.setVisible(false);
			});

			menu.getItems().add(item);
		}
	}

	/**
	 * Called when a NodeEdge is added to the boundary
	 * @param edge the edge that was added
	 */
	public void onEdgeAdded(NodeEdge edge)
	{
		Line line = new Line();
		line.setStrokeWidth(5);

		mapPane.getChildren().add(line);
		line.toBack();
		mapImage.toBack();

		edgeEntityMap.put(line, edge);

		addHandlersToEdge(line);
		updateEdgeLine(edge);
	}

	/**
	 * Called when an edge is removed
	 * @param edge The edge to remove
	 */
	public void onEdgeRemoved(NodeEdge edge)
	{
		Line l = edgeEntityMap.inverse().get(edge);
		mapPane.getChildren().remove(l);
		edgeEntityMap.remove(l);
	}

	/**
	 * Adds a building to the tab pane/model
	 *
	 * @author Ben Hylak
	 * @param f Floor
	 * @return
	 */
	public Tab makeFloorTab(Floor f)
	{
		final Tab tab = new Tab();
		tab.setText(f.toString());

		TreeViewWithItems tV = new TreeViewWithItems<MapNode>();

		tV.setRoot(new TreeItem<MapNode>(null));
		tV.setShowRoot(false);

		tV.setItems(f.getChildren());

		tV.getSelectionModel().selectedItemProperty().addListener((o, oldSelection, newSelection)->
		{
			if(oldSelection!=null)
			{
			//	DragIcon oldIcon = iconEntityMap.inverse().get(((TreeItem<Map>)oldSelection).getValue());
				//oldIcon.setEffect(null);
			}

			DragIcon icon = iconEntityMap.inverse().get(((TreeItem<MapNode>)newSelection).getValue());
			final Glow glow = new Glow();

			glow.setLevel(0.0);
			icon.setEffect(glow);

			final Timeline timeline = new Timeline();

			timeline.setCycleCount(5);
			timeline.setAutoReverse(true);
			final KeyValue kvA = new KeyValue(icon.scaleXProperty(), 1.7);
			final KeyFrame kfA = new KeyFrame(Duration.millis(200), kvA);

			final KeyValue kv1 = new KeyValue(icon.scaleYProperty(), 1.7);
			final KeyFrame kf1 = new KeyFrame(Duration.millis(200), kv1);
			timeline.getKeyFrames().add(kfA);
			timeline.getKeyFrames().add(kf1);
			timeline.play();

			timeline.setOnFinished(e->
			{
				final Timeline shrinkTimeline = new Timeline();

				shrinkTimeline.setCycleCount(1);
				shrinkTimeline.setAutoReverse(false);

				final KeyValue kvB = new KeyValue(icon.scaleXProperty(), 1);
				final KeyFrame kfB = new KeyFrame(Duration.millis(200), kvB);

				final KeyValue kvC = new KeyValue(icon.scaleYProperty(), 1);
				final KeyFrame kfC = new KeyFrame(Duration.millis(200), kvC);

				shrinkTimeline.getKeyFrames().add(kfB);
				shrinkTimeline.getKeyFrames().add(kfC);
				shrinkTimeline.play();
			});
		});

		tab.setContent(tV);

		BuildingTabPane.getTabs().add(tab);

		tabFloorMap.put(tab, f);

		return tab;
	}


	/**
	 * Runs when "New Floor" is clicked
	 *
	 * @param event from button click
	 */
	@FXML
	void onNewFloor(ActionEvent event) throws IOException
	{
	}
	/**Adds handlers to handle edge deletion mostly
	 *
	 * @param edge to add handlers to
	 */
	public void addHandlersToEdge(Line edge)
	{
		edge.setOnMouseEntered(deEvent->{
			if (edge != null)
			{
				edge.setStroke(Color.RED);
			}
		});

		edge.setOnMouseExited(deEvent->{
			if (edge != null) {
				edge.setStroke(Color.BLACK);
			}
		});

		edge.setOnMouseClicked(deEvent->{
			if (edge != null) {
				if (deEvent.getClickCount() == 2)
				{
					adminBoundary.removeEdge(edgeEntityMap.get(edge));
				}
			}
		});
	}

	/**Called when edge drawing is complete
	 *
	 */
	public void onEdgeComplete()
	{
		for(EdgeCompleteEventHandler handler : edgeCompleteHandlers)
		{
			handler.handle(new EdgeCompleteEvent(drawingEdge));
		}
	}


	/**
	 * Handler to be called when node is dragged... updates end point of any edges connected to it
	 * Also makes sure the mapnode location gets updated on a drag
	 *
	 * @param n node to keep in sync
	 * @return event handler for mouse event that updates positions of lines when triggered
	 */
	private void makeIconDraggable (Node n)
	{
		MouseControlUtil.makeDraggable(n, //could be used to track node and update line
				event ->
				{
					Bounds iconBounds = n.getBoundsInParent();

					Point2D movedTo = new Point2D((iconBounds.getMaxX()+iconBounds.getMinX())/2,
							(iconBounds.getMinY()+iconBounds.getMaxY())/2);

					adminBoundary.moveNode(iconEntityMap.get(n), movedTo);
					updateEdgeLinesForNode(iconEntityMap.get(n));
					System.out.println("Node moved to (X: "+ n.getParent().sceneToLocal(event.getSceneX(),event.getSceneY()));

				},
				null);

		mapPane.setOnDragDone(e->{
			System.out.println("Drag complete");
				}
		);
	}

	/**
	 * Updates all of the edge lines in a node with the new position
	 * @param n
	 */
	public void updateEdgeLinesForNode(MapNode n)
	{
		for(NodeEdge edge : n.getEdges())
		{
			if(edgeEntityMap.inverse().containsKey(edge))
			{
				updateEdgeLine(edge);
			}
		}
	}

	/**
	 * Updates an edge line with the new location o f anode
	 * @param edge
	 */
	public void updateEdgeLine(NodeEdge edge)
	{
		Line l = edgeEntityMap.inverse().get(edge);

		MapNode source = edge.getSource();
		MapNode target = edge.getTarget();

		l.setStartX(source.getPosX());
		l.setStartY(source.getPosY());

		l.setEndX(target.getPosX());
		l.setEndY(target.getPosY());
	}


	/**
	 * Adds all of the event handlers to handle dragging, edge creation, deletion etc.
	 * Needs to be called on newly constructed nodes to interact properly with map
	 *
	 * //@param mapNode
	 * @author Benjamin Hylak
	 */
	public void addEventHandlersToNode(Node n)
	{
	    makeIconDraggable(n); //make it draggable

		MapNode mapNode = iconEntityMap.get(n);

		/**Handles when the node is clicked
		 *
		 * Right Click -> Popover
		 * Double click -> Start Drawing
		 * Single Click + Drawing -> Set location
		 */
		n.setOnMouseClicked(ev -> {

			if(ev.getButton() == MouseButton.SECONDARY) //if right click
			{
				PopOver popOver = mapNode.getEditPopover();

				popOver.show(n,
						ev.getScreenX(),
						ev.getScreenY());

			}
			else if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse clicks
				if(ev.getClickCount() == 2) // double click
				{
					onStartEdgeDrawing(iconEntityMap.get(n));
				} //could add code to print location changes here.
			}

			/*** if...
			 * 1. We are drawing
			 * 2. This node was clicked
			 * 3. This node isn't the source of the edge we are drawing
			 */
			if (drawingEdge!=null && !drawingEdge.getSource().equals(iconEntityMap.get(n)))
			{
				drawingEdge.setTarget(iconEntityMap.get(n));
				onEdgeComplete();
				onStartEdgeDrawing(iconEntityMap.get(n));
			}
		});

		n.setOnMouseEntered(ev->
		{
			n.setOpacity(.65);
		});

		n.setOnMouseExited(ev->
		{
			n.setOpacity(1);
		});
	}

	/**
	 * Runs when a node is double clicked and a line needs to start drawing
	 * @param mapNode
	 */
	public void onStartEdgeDrawing(MapNode mapNode)
	{
		drawingEdge = new NodeEdge();
		drawingEdge.setSource(mapNode);

		mapPane.setOnMouseMoved(mouseEvent->{ //handle mouse movement in the right pane

			if (!drawingEdgeFrozen && drawingEdgeLine.isVisible() && drawingEdge != null)
			{
				Point p = MouseInfo.getPointerInfo().getLocation(); // get the absolute current loc of the mouse on screen
				Point2D mouseCoords = drawingEdgeLine.screenToLocal(p.x, p.y); // convert coordinates to relative within the window
				drawingEdgeLine.setEndX(mouseCoords.getX());
				drawingEdgeLine.setEndY(mouseCoords.getY());
			}
		});

		Point2D startPoint = mapPane.sceneToLocal(mapPane.localToScene(mapNode.getPosX(), mapNode.getPosY()));
		drawingEdgeLine.setStartY(startPoint.getY());
		drawingEdgeLine.setStartX(startPoint.getX());
		drawingEdgeLine.setEndX(startPoint.getX());
		drawingEdgeLine.setEndY(startPoint.getY());

		drawingEdgeLine.setVisible(true);

		drawingEdgeLine.toBack();
		mapImage.toBack();

		iconEntityMap.inverse().get(mapNode).setOnMouseDragEntered(null); //sets drag handlers to null so they can't be repositioned during line drawing
		iconEntityMap.inverse().get(mapNode).setOnMouseDragged(null);
	}

	/**Handles saving out all of the map info
	 * @TODO Save directory changes
	 * @throws IOException
	 */

	@FXML
	public void saveInfoAndExit() throws IOException, SQLException
	{
		DataCache.getInstance().save();

		SceneSwitcher.switchToUserMapView(this.getStage());
	}

	@FXML
	public void onDirectoryEditorSwitch(ActionEvent actionEvent) throws IOException
	{
		//SceneSwitcher.switchToModifyDirectoryView(this.getStage(), model.getHospital());
	}
}
