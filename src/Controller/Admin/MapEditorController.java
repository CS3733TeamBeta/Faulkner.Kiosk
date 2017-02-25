package Controller.Admin;

import Boundary.AdminMapBoundary;
import Controller.AbstractController;
import Controller.SceneSwitcher;
import Domain.Map.*;
import Domain.ViewElements.DragContainer;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DragIconType;
import Domain.ViewElements.Events.EdgeCompleteEvent;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Model.DataSourceClasses.MapTreeItem;
import Model.DataSourceClasses.TreeViewWithItems;
import Model.Database.DatabaseManager;
import Model.MapEditorModel;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import jfxtras.labs.util.event.MouseControlUtil;
import jfxtras.scene.menu.CirclePopupMenu;
import org.controlsfx.control.PopOver;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static Controller.SceneSwitcher.switchToAddFloor;

public class MapEditorController extends AbstractController {

	@FXML SplitPane base_pane;
	@FXML AnchorPane mapPane;
	@FXML HBox bottom_bar;
	@FXML AnchorPane root_pane;
	@FXML ImageView mapImage;

	@FXML
	Button newBuildingButton;

	@FXML
	Button newFloorButton;


	@FXML
	private TabPane BuildingTabPane;

	private DragIcon mDragOverIcon = null;

	private EventHandler<DragEvent> onIconDragOverRoot = null;
	private EventHandler<DragEvent> onIconDragDropped = null;
	private EventHandler<DragEvent> onIconDragOverRightPane = null;
	private MapEditorModel model;

	NodeEdge drawingEdge;

	Group mapItems;

	AdminMapBoundary boundary;

	BiMap<DragIcon, MapNode> iconEntityMap;
	HashMap<Line, NodeEdge>  edgeEntityMap;
	CirclePopupMenu menu;

	Point2D menuOpenLoc = null;

	final ImageView target = new ImageView("@../../crosshair.png");


	public MapEditorController() {

		model = new MapEditorModel();
		boundary = new AdminMapBoundary();

		iconEntityMap = HashBiMap.create();
		edgeEntityMap = new HashMap<Line, NodeEdge>();

		//Runs once the edge is drawn from one node to another
		//connects the two, sends sources, positions them etc.
		/*model.addEdgeCompleteHandler(event->
		{
			MapNode sourceNode = event.getNodeEdge().getSource();
			MapNode targetNode = event.getNodeEdge().getTarget();

			boundary.newEdge(sourceNode, targetNode);

			mapPane.setOnMouseMoved(null);
			mapImage.toBack();
		});*/


	}

	/**
	 * FXML initialize function
	 */
	@FXML
	private void initialize() {
		System.out.println("Here");

		//initNodes()fc

		BuildingTabPane.getTabs().clear();

		//Add one icon that will be used for the drag-drop process
		//This is added as a child to the root anchorpane so it can be visible
		//on both sides of the split pane.

		mDragOverIcon = new DragIcon();
		mDragOverIcon.setVisible(false);
		mDragOverIcon.setOpacity(0.65);

		root_pane.getChildren().add(mDragOverIcon);

		//populate left pane with multiple colored icons for testing
		for (int i = 0; i < DragIconType.values().length; i++)
		{
			DragIcon icn = new DragIcon();

			icn.setStyle("-fx-background-size: 64 64");

			addDragDetection(icn);
			icn.setType(DragIconType.values()[i]);

			if (icn.getType().equals(DragIconType.Connector))
			{
				//System.out.println("Adding Connector");
				icn.setStyle("-fx-background-size: 30 30");
			}

			model.addSideBarIcon(icn);
			bottom_bar.getChildren().add(icn);
		}

		buildDragHandlers();

		boundary.addNodeSetChangeHandler(new SetChangeListener<MapNode>()
		{
			@Override
			public void onChanged(Change<? extends MapNode> change)
			{
				if(change.wasAdded())
				{
					DragIcon icon = new DragIcon();
					icon.setType(change.getElementAdded().getIconType());
					icon.relocate(change.getElementAdded().getPosX()-32,
							change.getElementAdded().getPosY()-32);

					addEventHandlersToNode(icon);

					mapPane.getChildren().add(icon);
					iconEntityMap.put(icon, change.getElementAdded());
				}
				else if(change.wasRemoved())
				{
					DragIcon n = iconEntityMap.inverse().get(change.getElementRemoved());
					iconEntityMap.remove(n);

					mapPane.getChildren().remove(n);
				}
			}
		});

		menu = new CirclePopupMenu(mapPane, null);

		mapPane.setOnMouseClicked(event->
		{
			menu.show(event.getScreenX(), event.getScreenY());

			target.setVisible(true);

			target.setX(event.getX()-target.getFitWidth()/2);
			target.setY(event.getY()-target.getFitHeight()/2);

			menuOpenLoc = new Point2D(event.getX(), event.getY());
		});

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

			model.addSideBarIcon(icn);
			bottom_bar.getChildren().add(icn);

			MenuItem item = new MenuItem(DragIconType.values()[i].name(), icn);

			icn.setOnMouseClicked(event -> {
				boundary.newNode(icn.getType(), menuOpenLoc);
				target.setVisible(false);
			});

			menu.getItems().add(item);
		}

		target.setVisible(false);
		target.setFitWidth(14);
		target.setFitHeight(14);
		mapPane.getChildren().add(target);
	}

	/**
	 * Takes a collection of buildings and creates tabs for them
	 * @param buildings
	 */
	public void loadBuildingsToTabPane(Collection<Building> buildings)
	{
		for(Building b: buildings)
		{
			Tab t = makeBuildingTab(b);

			TreeViewWithItems<MapTreeItem> treeView = (TreeViewWithItems<MapTreeItem>) t.getContent();

			treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newvalue) ->
			{
				Floor selectedFloor;

				if (newvalue.getValue().getValue() instanceof Floor)
				{
					selectedFloor =(Floor) newvalue.getValue().getValue();
				}
				else
				{
					selectedFloor = (Floor) (newvalue.getParent().getValue().getValue());
				}

				if(model.getCurrentFloor() == null || !model.getCurrentFloor().equals(selectedFloor))
				{
					changeFloorSelection(selectedFloor);
				}
			});

			for (Floor f : b.getFloors())
			{
				treeView.getRoot().getChildren().add(makeTreeItem(f));
			}

			treeView.getRoot().getChildren().sort(Comparator.comparing(o -> o.toString()));

			model.addBuilding(b, t); //adds to building tab map
		}

		createCampusTab();
	}


	public void createCampusTab()
	{
		final Label label = new Label("Campus");
		final Tab tab = new Tab();
		tab.setGraphic(label);

		TreeView tV = new TreeView<>();

		tV.setRoot(new TreeItem<MapTreeItem>(null));
		tV.setShowRoot(false);

		label.setOnMouseClicked(e->
				{
					Floor campusFloor = model.getHospital().getCampusFloor();

					//changeFloorSelection(campusFloor);
					tV.getRoot().getChildren().clear();

					for(Building b: model.getHospital().getBuildings())
					{
						try
						{
							TreeItem<String> buildingItem = new TreeItem<String>(b.getName());

							Floor groundFloor = b.getFloor(1);

							for(MapNode n: groundFloor.getFloorNodes())
							{
								if(n.getIconType()!=DragIconType.Connector)
								{
									buildingItem.getChildren().add(new TreeItem<String>(n.toString()));
								}
							}

							tV.getRoot().getChildren().add(buildingItem);

						} catch (Exception e1)
						{
						}
					}

					//model.setCurrentFloor(campusFloor);
					//renderFloorMap(campusFloor);
				}
		);

		tab.setContent(tV);

		//BuildingTabPane.getTabs().add(tab);
	}
	/**
	 * Adds a building to the tab pane/model
	 *
	 * @author Ben Hylak
	 * @param b Building
	 * @return
	 */
	public Tab makeBuildingTab(Building b)
	{
		final Label label = new Label(b.getName());
		final Tab tab = new Tab();
		tab.setGraphic(label);

		final TextField textField = new TextField();

		label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount()==2)
				{
					textField.setText(label.getText());

					tab.setGraphic(textField);
					textField.selectAll();
					textField.requestFocus();
				}
				else
				{
					//select floor and change map
				}
			}
		});

		textField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				label.setText(textField.getText());
				tab.setGraphic(label);
				b.setName(label.getText());
			}
		});


		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
								Boolean oldValue, Boolean newValue) {
				if (! newValue) {
					label.setText(textField.getText());
					tab.setGraphic(label);
				}
			}
		});

		TreeViewWithItems<MapTreeItem> tV = new TreeViewWithItems<MapTreeItem>();

		tV.setRoot(new TreeItem<MapTreeItem>(null));
		tV.setShowRoot(false);

		tab.setContent(tV);

		BuildingTabPane.getTabs().add(tab);

		return tab;
	}

	/**
	 * Runs when new building button is clicked
	 *
	 * @param event
	 */
	@FXML
	void onNewBuilding(ActionEvent event)
	{
		//boundary.newBuilding();
		//boundary has building observable list

		//Building b = new Building("Building " + (model.getBuildingCount()+1)); //@TODO Hacky fix -BEN

		//new building pane

		//model.addBuilding(b, makeBuildingTab(b));
	}

	/**
	 * Runs when "New Floor" is clicked
	 *
	 * @param event from button click
	 */
	@FXML
	void onNewFloor(ActionEvent event) throws IOException
	{
		//boundary.newFloor()

		//make new floor in building
		//refresh treeview (observable, not needed)
	}

	public TreeItem<MapTreeItem> makeTreeItem(Object o)
	{
		MapTreeItem treeObj = new MapTreeItem(o);

		TreeItem<MapTreeItem> treeItem = new TreeItem<>(treeObj);

		treeItem.setExpanded(true);

		return treeItem;
	}

	public void changeFloorSelection(Floor f)
	{
		//boundary.changeFloor();

		if(!f.equals(model.getCurrentFloor()))
		{
			if (f.getImageLocation() == null)
			{
				try
				{
					switchToAddFloor(this.getStage());
				} catch (IOException e)
				{
					System.out.println("Threw an exception in MapEditorController: changeFloorSelection");
					e.printStackTrace();
				}
			}

			model.setCurrentFloor(f);
			//renderFloorMap();

			System.out.println("Changed floor to " + f);
		}
	}

	protected void renderFloorMap(Floor f)
	{
		/*mapItems = new Group();
		mapPane.getChildren().clear();
		mapPane.getChildren().add(mapItems);

		mapImage.setImage(f.getImageInfo().getFXImage());
		mapItems.getChildren().add(mapImage);

		for(MapNode n: f.getFloorNodes())
		{
			importNode(n);

			for(NodeEdge e : n.getEdges())
			{
				if(f.getFloorNodes().contains(e.getOtherNode(n)))
				{
					if (!mapItems.getChildren().contains(e.getEdgeLine()))
					{
						mapItems.getChildren().add(e.getEdgeLine());
					}

					addHandlersToEdge(e);

					e.updatePosViaNode(n);
				}
			}

			n.getNodeToDisplay().toFront();
		}

		mapImage.toBack();*/
	}

	protected void renderFloorMap()
	{
		renderFloorMap(model.getCurrentFloor());
		loadFloorTreeView();
	}

	/**
	 *
	 * Imports map node without adding it to model
	 *
	 * @param mapNode
	 */
	protected void importNode(MapNode mapNode)
	{
		//addEventHandlersToNode(mapNode);

		mapNode.toFront(); //send the node to the front
	}

	/**
	 * Loads the tree view for a specific floor
	 */
	protected void loadFloorTreeView()
	{
		for(MapNode n: model.getCurrentFloor().getFloorNodes())
		{
			if(!n.getIconType().equals(DragIconType.Connector)) //treeview checks that floor actually contains
			{
				addToTreeView(n);
			}
		}
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
					//edge.getSource().getEdges().remove(edge);
					//edge.getTarget().getEdges().remove(edge);
					//mapItems.getChildren().remove(edge.getNodeToDisplay()); //remove from the right pane
					//model.removeMapEdge(edge);
				}
			}
		});
	}

	public void onEdgeComplete()
	{
		System.out.println("Edge complete");

		for(EdgeCompleteEventHandler handler : model.getEdgeCompleteHandlers())
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
	private void makeMapNodeDraggable (Node n)
	{
		MouseControlUtil.makeDraggable(n, //could be used to track node and update line
				event ->
				{
					Point2D movedTo = n.getParent().sceneToLocal(event.getSceneX(),event.getSceneY());
					boundary.moveNode(iconEntityMap.get(n), movedTo);

					//boundary.updateNode()
					System.out.println("Node moved to (X: "+ n.getParent().sceneToLocal(event.getSceneX(),event.getSceneY()));

				},
				null);
	}

	private void addDragDetection(DragIcon dragIcon) {
		
		dragIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				// set drag event handlers on their respective objects
				base_pane.setOnDragOver(onIconDragOverRoot);
				mapPane.setOnDragOver(onIconDragOverRightPane);
				mapPane.setOnDragDropped(onIconDragDropped);
				
				// get a reference to the clicked DragIcon object
				DragIcon icn = (DragIcon) event.getSource();

				//begin drag ops
				mDragOverIcon.setType(icn.getType());
				mDragOverIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));
            
				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();
				
				container.addData ("type", mDragOverIcon.getType().toString());
				content.put(DragContainer.AddNode, container);

				mDragOverIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
				mDragOverIcon.setVisible(false);
				mDragOverIcon.setMouseTransparent(true);
				event.consume();					
			}
		});
	}

	private void buildDragHandlers()
	{
		//drag over transition to move widget form left pane to right pane
		onIconDragOverRoot = new EventHandler<DragEvent>()
		{
			@Override
			public void handle(DragEvent event)
			{

				Point2D p = mapPane.sceneToLocal(event.getSceneX(), event.getSceneY());

				//turn on transfer mode and track in the right-pane's context 
				//if (and only if) the mouse cursor falls within the right pane's bounds.
				if (!mapPane.boundsInLocalProperty().get().contains(p))
				{
					event.acceptTransferModes(TransferMode.ANY);
					mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
					return;
				}

				event.consume();
			}
		};

		onIconDragOverRightPane = new EventHandler<DragEvent>()
		{

			@Override
			public void handle(DragEvent event)
			{
				event.acceptTransferModes(TransferMode.ANY);

				//convert the mouse coordinates to scene coordinates,
				//then convert back to coordinates that are relative to 
				//the parent of mDragIcon.  Since mDragIcon is a child of the root
				//pane, coodinates must be in the root pane's coordinate system to work
				//properly.

				mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
				event.consume();
			}
		};

		onIconDragDropped = new EventHandler<DragEvent>()
		{
			@Override
			public void handle(DragEvent event)
			{
				System.out.println("Node added");

				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

				container.addData("scene_coords", new Point2D(event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddNode, container);

				event.getDragboard().setContent(content);
				event.setDropCompleted(true);
			}
		};

		root_pane.setOnDragDone(new EventHandler<DragEvent>()
		{
			@Override
			public void handle(DragEvent event)
			{
				System.out.println("test");
				mapPane.removeEventHandler(DragEvent.DRAG_OVER, onIconDragOverRightPane); //remove the event handlers created on drag start
				mapPane.removeEventHandler(DragEvent.DRAG_DROPPED, onIconDragDropped);
				base_pane.removeEventHandler(DragEvent.DRAG_OVER, onIconDragOverRoot);

				mDragOverIcon.setVisible(false);

				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode); //information from the drop

				if (container != null)
				{

					if (container.getValue("scene_coords") != null)
					{
						MapNode droppedNode;

						droppedNode = DragIcon.constructMapNodeFromType((DragIconType.valueOf(container.getValue("type"))));

						Point2D cursorPoint = container.getValue("scene_coords"); //cursor point
						Point2D movedTo = mapPane.sceneToLocal(cursorPoint);

						droppedNode.setPosX(movedTo.getX());
						droppedNode.setPosY(movedTo.getY());

						boundary.addNode(droppedNode);
					}
					event.consume();
				}
			}
		});
	}


	public void addToTreeView(MapNode d)
	{
		if(!(d.toString().isEmpty()))
		{
			TreeViewWithItems<MapTreeItem> treeView = (TreeViewWithItems<MapTreeItem>) BuildingTabPane.getSelectionModel().getSelectedItem().getContent();

			for (TreeItem<MapTreeItem> floorTreeItem : treeView.getRoot().getChildren())
			{
				if (floorTreeItem.getValue().getValue() instanceof Floor && ((Floor) floorTreeItem.getValue().getValue()).getFloorNodes().contains(d))
				{
					boolean treeContainsNode = false;

					for (TreeItem<MapTreeItem> item : floorTreeItem.getChildren())
					{
						if (item.getValue().getValue().equals(d))
						{
							treeContainsNode = true;
						}
					}

					if (!treeContainsNode)
					{
						floorTreeItem.getChildren().add(makeTreeItem(d));
					}
				}

				treeView.refresh();
			}
		}
	}

	/**
	 *
	 * @return Returns the treeview of the currently selected building
	 * @author Benjamin Hylak
	 */
	public TreeViewWithItems<MapTreeItem> getCurrentTreeView()
	{
		return (TreeViewWithItems<MapTreeItem>)BuildingTabPane.getSelectionModel().getSelectedItem().getContent();
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
	    makeMapNodeDraggable(n); //make it draggable

		/***Handles deletion from a popup menu**/
		/*mapNode.setOnDeleteRequested(event -> {
			removeNode(event.getSource());
		});*/

		/**Handles when the node is clicked
		 *
		 * Right Click -> Popover
		 * Double click -> Start Drawing
		 * Single Click + Drawing -> Set location
		 */
		n.setOnMouseClicked(ev -> {

			if(ev.getButton() == MouseButton.SECONDARY) //if right click
			{
				PopOver popOver = iconEntityMap.get(n).getEditPopover();

				/***If the name is set, at it to the tree*/
				popOver.setOnHiding(event -> {
					/*if(!model.getCurrentFloor().equals(model.getHospital().getCampusFloor()))
					{
						getCurrentTreeView().refresh(); //refresh the treeview once the popup editor closes
					}*/
				});

				popOver.show(n,
						ev.getScreenX(),
						ev.getScreenY());

			}
			else if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse clicks
				if(ev.getClickCount() == 2) // double click
				{
					onStartEdgeDrawing(iconEntityMap.get(n));
					//boundary.remove(n);
				} //could add code to print location changes here.
			}

			/*** if...
			 * 1. We are drawing
			 * 2. This node was clicked
			 * 3. This node isn't the source of the edge we are drawing
			 */
			if (drawingEdge!=null && !drawingEdge.getSource().equals(iconEntityMap.get(n)))
			{
				//drawingEdge.setTarget(mapNode);
				onEdgeComplete();
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
		if(drawingEdge != null) //if currently drawing... handles case of right clicking to start a new node
		{
			if(mapPane.getChildren().contains(drawingEdge.getNodeToDisplay())) //and the right pane has the drawing edge as child
			{
				mapPane.getChildren().remove(drawingEdge.getNodeToDisplay()); //remove from the right pane
			}
		}

		drawingEdge = new NodeEdge();
		drawingEdge.setSource(mapNode);

		mapPane.getChildren().add(drawingEdge.getNodeToDisplay());
		drawingEdge.toBack();
		mapImage.toBack();

		mapNode.getNodeToDisplay().setOnMouseDragEntered(null); //sets drag handlers to null so they can't be repositioned during line drawing
		mapNode.getNodeToDisplay().setOnMouseDragged(null);

		root_pane.setOnKeyPressed(keyEvent-> { //handle escaping from edge creation
			if (drawingEdge != null && keyEvent.getCode() == KeyCode.ESCAPE) {
				if(mapPane.getChildren().contains(drawingEdge.getNodeToDisplay())) //and the right pane has the drawing edge as child
				{
					mapPane.getChildren().remove(drawingEdge.getNodeToDisplay()); //remove from the right pane
				}
				drawingEdge = null;

				mapPane.setOnMouseMoved(null);
			}
		});

		mapPane.setOnMouseMoved(mouseEvent->{ //handle mouse movement in the right pane

			if (drawingEdge != null)
			{
				//System.out.println("Moving Mouse");
				Point p = MouseInfo.getPointerInfo().getLocation(); // get the absolute current loc of the mouse on screen
				Point2D mouseCoords = drawingEdge.getEdgeLine().screenToLocal(p.x, p.y); // convert coordinates to relative within the window
				drawingEdge.setEndPoint(mouseCoords); //set the end point
			}
		});
	}

	/**Handles saving out all of the map info
	 * @TODO Save directory changes
	 * @throws IOException
	 */

	@FXML
	public void saveInfoAndExit() throws IOException, SQLException
	{

		new DatabaseManager().saveData(model.getHospital());

		SceneSwitcher.switchToUserMapView(this.getStage());
	}

	@FXML
	public void onDirectoryEditorSwitch(ActionEvent actionEvent) throws IOException
	{
		SceneSwitcher.switchToModifyDirectoryView(this.getStage(), model.getHospital());
	}
}
