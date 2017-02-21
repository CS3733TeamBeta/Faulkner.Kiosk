package Controller.Admin;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import Controller.AbstractController;
import Controller.SceneSwitcher;
import Domain.Map.*;
import Domain.ViewElements.*;
import Domain.ViewElements.Events.EdgeCompleteEvent;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Model.DataSourceClasses.MapTreeItem;
import Model.DataSourceClasses.TreeViewWithItems;
import Model.Database.DatabaseManager;
import Model.MapEditorModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import jfxtras.labs.util.event.MouseControlUtil;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.PopOver;
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

	public MapEditorController() {

		model = new MapEditorModel();

		//Runs once the edge is drawn from one node to another
		//connects the two, sends sources, positions them etc.
		model.addEdgeCompleteHandler(event->
		{

			System.out.println("Edge Complete Handler Invoked");

			NodeEdge completedEdge = drawingEdge;

			addHandlersToEdge(completedEdge);

			MapNode sourceNode = event.getNodeEdge().getSource();
			MapNode targetNode = event.getNodeEdge().getTarget();

			model.addMapEdge(drawingEdge);

			mapPane.setOnMouseMoved(null);

			drawingEdge.setSource(sourceNode);
			drawingEdge.setTarget(targetNode);

			sourceNode.addEdge(drawingEdge); // add the current drawing edge to the list of this node's edges
			targetNode.addEdge(drawingEdge); // add the current drawing edge to the list of this node's edges

			makeMapNodeDraggable(sourceNode);
			makeMapNodeDraggable(targetNode);

			drawingEdge.getNodeToDisplay().toBack(); //send drawing edge to back
			drawingEdge = null;

			sourceNode.toFront();
			sourceNode.toFront();
			mapImage.toBack();

		});
	}

	/**
	 * FXML initialize function
	 */
	@FXML
	private void initialize() {

		mapItems = new Group();

		mapPane.getChildren().add(mapItems);

		//mapItems.relocate(0, 0);
		mapImage.relocate(0, 0);
		mapPane.relocate(0, 0);

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

			if (icn.getType().equals(DragIconType.connector))
			{
				//System.out.println("Adding Connector");
				icn.setStyle("-fx-background-size: 30 30");
			}

			model.addSideBarIcon(icn);
			bottom_bar.getChildren().add(icn);
		}

		buildDragHandlers();

		loadBuildingsToTabPane(model.getHospital().getBuildings());

		getCurrentTreeView().getSelectionModel().select(0); //selects first floor

		renderFloorMap();

		/*mapPane.addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent -> {
			if(drawingEdge != null)
			{
				// devondevon

				Node sourceNode = drawingEdge.getSource().getNodeToDisplay();

				Bounds sourceNodeBounds = sourceNode.getBoundsInParent();

				Point2D clickPoint = new Point2D(clickEvent.getX(), clickEvent.getY());

				if(!sourceNodeBounds.contains(clickPoint))
				{

					MapNode chainLinkNode = DragIcon.constructMapNodeFromType(DragIconType.connector);
					chainLinkNode.setType(DragIconType.connector); //set the type

					clickPoint = mapPane.localToScene(clickPoint);

					clickPoint = new Point2D(clickPoint.getX() - 12.5, clickPoint.getY() - 12.5);

					chainLinkNode.setPosX(clickPoint.getX());
					chainLinkNode.setPosY(clickPoint.getY());

					addToAdminMap(chainLinkNode);

					drawingEdge.setTarget(chainLinkNode);

					onEdgeComplete();
				}
			}
		});*/
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
				if (newvalue.getValue().getValue() instanceof Floor)
				{
					changeFloorSelection((Floor) newvalue.getValue().getValue());
				}
				else
				{
					changeFloorSelection((Floor) (newvalue.getParent().getValue().getValue()));
				}
			});

			for (Floor f : b.getFloors())
			{
				treeView.getRoot().getChildren().add(makeTreeItem(f));
			}

			treeView.getRoot().getChildren().sort(Comparator.comparing(o -> o.toString()));

			model.addBuilding(b, t); //adds to building tab map
		}
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
		Building b = new Building("Building " + (model.getBuildingCount()+1)); //@TODO Hacky fix -BEN

		model.addBuilding(b, makeBuildingTab(b));
	}

	/**
	 * Runs when "New Floor" is clicked
	 *
	 * @param event from button click
	 */
	@FXML
	void onNewFloor(ActionEvent event)
	{
		TreeViewWithItems<MapTreeItem> treeView = (TreeViewWithItems<MapTreeItem>)BuildingTabPane.getSelectionModel().getSelectedItem().getContent();

		Building b = model.getBuildingFromTab(BuildingTabPane.getSelectionModel().getSelectedItem());

		Floor f = b.newFloor(); //makes new floor

		treeView.getRoot().getChildren().add(makeTreeItem(f));
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
		model.setCurrentFloor(f);

		if(f.getImageLocation() == null)
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

		this.mapImage.setImage(f.getImageInfo().getFXImage());

		model.setCurrentFloor(f);
		renderFloorMap();

		System.out.println("Changed floor to " + f);
	}

	protected void renderFloorMap()
	{
		mapItems = new Group();
		mapItems.getChildren().add(mapImage);

		mapImage.setImage(model.getCurrentFloor().getImageInfo().getFXImage());

		mapPane.getChildren().clear();
		mapPane.getChildren().add(mapItems);

		//and then set all the existing nodes up
		HashSet<NodeEdge> collectedEdges = new HashSet<NodeEdge>();

		/*//@TODO CONCURRENCY ERROR
		for(Object x : model.getCurrentFloor().getFloorNodes().toArray())
		{
			MapNode n = (MapNode)x;

			addToAdminMap(n);

			for(NodeEdge edge: n.getEdges())
			{
				if(!collectedEdges.contains(edge)) collectedEdges.add(edge);
			}

			addEventHandlersToNode(n);

			n.getNodeToDisplay().setOnMouseClicked(null);
			n.getNodeToDisplay().setOnDragDetected(null);
		}*/

		for(MapNode n: model.getCurrentFloor().getFloorNodes())
		{
			for(NodeEdge edge: n.getEdges())
			{
				if(!collectedEdges.contains(edge)) collectedEdges.add(edge);
			}

			n.getNodeToDisplay().setOnMouseClicked(null);
			n.getNodeToDisplay().setOnDragDetected(null);
		}

		for(NodeEdge edge : collectedEdges)
		{
			edge.getEdgeLine().setOnMouseClicked(null);
			edge.getEdgeLine().setOnMouseEntered(null);
			edge.getEdgeLine().setOnMouseExited(null);

			addHandlersToEdge(edge);

			if(!mapItems.getChildren().contains(edge.getNodeToDisplay()))
			{
				mapItems.getChildren().add(edge.getNodeToDisplay());
			}

			MapNode source = edge.getSource();
			MapNode target = edge.getTarget();

			//@TODO BUG WITH SOURCE DATA, I SHOULDNT HAVE TO DO THIS

			if(!mapItems.getChildren().contains(source.getNodeToDisplay()))
			{
				importNode(source);
			}

			if(!mapItems.getChildren().contains(target.getNodeToDisplay()))
			{
				importNode(target);
			}

			edge.updatePosViaNode(source);
			edge.updatePosViaNode(target);

			edge.toBack();
			source.toFront();
			target.toFront();
		}

		mapImage.toBack();
	}

	/**
	 *
	 * Imports map node without adding it to model
	 *
	 * @param mapNode
	 */
	protected void importNode(MapNode mapNode)
	{
		model.addMapNode(mapNode); //add node to model

		if(!mapItems.getChildren().contains(mapNode.getNodeToDisplay()))
		{
			mapItems.getChildren().add(mapNode.getNodeToDisplay()); //add to right panes children
		}

		addEventHandlersToNode(mapNode);

		if(!mapNode.getIconType().equals(DragIconType.connector)) //treeview checks that floor actually contains
		{
			addToTreeView(mapNode);
		}

		mapNode.toFront(); //send the node to the front
	}

	/**
	 * Refreshes the node positions so they're up to date from latest drags/changes
	 *
	 * Probably redundant
	 */
	public void refreshNodePositions()
	{
		for (MapNode n : model.getCurrentFloor().getFloorNodes())
		{
			DragIcon icon = (DragIcon)n.getNodeToDisplay();

			Point2D newPoint = new Point2D(icon.getLayoutX() + icon.getBoundsInLocal().getWidth() / 2,
					icon.getLayoutY() + icon.getBoundsInLocal().getHeight() / 2);

			n.setPosX((newPoint.getX()));
			n.setPosY((newPoint.getY()));
		}
	}

	/*protected void renderFloorMap()
	{
		mapItems.getChildren().clear();
		mapPane.getChildren().remove(mapImage);
		mapItems.getChildren().add(mapImage);
		mapImage.toBack();

		//and then set all the existing nodes up
		HashSet<NodeEdge> collectedEdges = new HashSet<NodeEdge>();

		for(MapNode n : model.getCurrentFloor().getFloorNodes())
		{
			System.out.println("Adding node");
			addToAdminMap(n);

			if(n.getIsElevator())
			{
				System.out.println("Before Set Location: " + n.getPosX() + ", " + n.getPosY());
			}

			n.setPos(n.getPosX(), n.getPosY()); // @TODO refresh function

			if(n.getIsElevator())
			{
				System.out.println("After Se Location: " + n.getPosX() + ", " + n.getPosY());
			}

			for(NodeEdge edge: n.getEdges())
			{
				if(!collectedEdges.contains(edge) && !(edge instanceof LinkEdge)) collectedEdges.add(edge);
			}
		}

		for(NodeEdge edge : collectedEdges)
		{
			addHandlersToEdge(edge);

			if(!mapItems.getChildren().contains(edge.getNodeToDisplay()))
			{
				mapItems.getChildren().add(edge.getNodeToDisplay());
			}

			MapNode source = edge.getSource();
			MapNode target = edge.getTarget();

			//@TODO BUG WITH SOURCE DATA, I SHOULDNT HAVE TO DO THIS

			if(!mapItems.getChildren().contains(source.getNodeToDisplay()))
			{
				addToAdminMap(source);
			}

			if(!mapItems.getChildren().contains(target.getNodeToDisplay()))
			{
				addToAdminMap(target);
			}

			source.setPos(source.getPosX(), source.getPosY());
			target.setPos(target.getPosX(), target.getPosY());

			edge.updatePosViaNode(source);
			edge.updatePosViaNode(target);

			edge.toBack();
			source.toFront();
			target.toFront();

			mapImage.toBack();
		}
	}*/

	/**Adds handlers to handle edge deletion mostly
	 *
	 * @param edge to add handlers to
	 */
	public void addHandlersToEdge(NodeEdge edge)
	{
		edge.getNodeToDisplay().setOnMouseEntered(deEvent->{
			if (edge != null)
			{
				edge.getEdgeLine().setStroke(Color.RED);
			}
		});

		edge.getNodeToDisplay().setOnMouseExited(deEvent->{
			if (edge != null) {
				edge.getEdgeLine().setStroke(Color.BLACK);
			}
		});

		edge.getNodeToDisplay().setOnMouseClicked(deEvent->{
			if (edge != null) {
				if (deEvent.getClickCount() == 2) {
					edge.getSource().getEdges().remove(edge);
					edge.getTarget().getEdges().remove(edge);
					mapItems.getChildren().remove(edge.getNodeToDisplay()); //remove from the right pane
					model.removeMapEdge(edge);
				}
			}
		});
	}

	public void onEdgeComplete()
	{
		System.out.println("Edge complete");

		for(EdgeCompleteEventHandler handler : model.getEdgeCompleteHandlers())
		{
			if(!model.getCurrentFloor().getFloorEdges().contains(drawingEdge)){
				model.getCurrentFloor().getFloorEdges().add(drawingEdge);
			}
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
	private static void makeMapNodeDraggable (MapNode n)
	{
		MouseControlUtil.makeDraggable(n.getNodeToDisplay(), //could be used to track node and update line
				event ->
				{
					for (NodeEdge edge : n.getEdges())
					{
						edge.updatePosViaNode(n);
					}

					n.setPosX(event.getSceneX());
					n.setPosY(event.getSceneY());

					//System.out.println("Node " + n.getIconType().name() + " moved to (X: "+ event.getSceneX() + ", Y: " + event.getSceneY() + ")");

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
						//droppedNode.setType(DragIconType.valueOf(container.getValue("type"))); //set the type

						mapItems.getChildren().add(droppedNode.getNodeToDisplay());

						Point2D cursorPoint = container.getValue("scene_coords"); //cursor point

						System.out.println("Started off with: " + cursorPoint.getX());

						cursorPoint = droppedNode.getNodeToDisplay().getParent().sceneToLocal(cursorPoint);

						System.out.println("Ended with: ");
						System.out.println(droppedNode.getNodeToDisplay().parentToLocal(droppedNode.
										getNodeToDisplay().getParent().localToScene(cursorPoint)).getX());

						droppedNode.setPos(cursorPoint.getX()-12.5,
								cursorPoint.getY()-12.5); //offset because mouse drag and pictures should start from upper corn

						if(droppedNode.getIsElevator())
						{
							addNewElevatorToAdminMap(droppedNode);
						}
						else addToAdminMap(droppedNode);
					}
					event.consume();
				}
			}

			;
		});
	}

	public void addNewElevatorToAdminMap(MapNode mapNode)
	{
		if(mapNode.getIsElevator())
		{
			System.out.println("Adding new elevator...");

			ArrayList<MapNode> nodesToAdd = new ArrayList<MapNode>();

			if (!mapItems.getChildren().contains(mapNode.getNodeToDisplay()))
			{
				mapItems.getChildren().add(mapNode.getNodeToDisplay()); //add to right panes children
			}

			//mapNode.setPos(mapNode.getPosX(), mapNode.getPosY());

			Building b = model.getBuildingFromTab(BuildingTabPane.getSelectionModel().getSelectedItem());

			MapNode last = null;

			MapNode e;

			for (Floor f : b.getFloors())
			{
				if (f != model.getCurrentFloor())
				{
					e = new MapNode();

					e.setIsElevator(true);

					mapItems.getChildren().add(e.getNodeToDisplay()); //ya don't try to understand this

					e.setPos(mapNode.getPosX(), mapNode.getPosY());

					mapItems.getChildren().remove(e.getNodeToDisplay()); //cause i don't either

					f.addNode(e);

					nodesToAdd.add(e);
				}
				else
				{
					e = mapNode;
					nodesToAdd.add(mapNode);
					model.getCurrentFloor().addNode(mapNode);
				}

				if (last != null)
				{
					LinkEdge edge = new LinkEdge(last, e);
				}

				last = e;
			}

			for (MapNode n : nodesToAdd)
			{
				addEventHandlersToNode(n);
				addToTreeView(n);
			}

			model.addMapNode(mapNode); //add node to model
			mapNode.toFront();
		}
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
						((Floor) floorTreeItem.getValue().getValue()).addNode(d);
						floorTreeItem.getChildren().add(makeTreeItem(d));
					}
				}

				treeView.refresh();
			}
		}
	}

	/**
	 * Adds a fresh node to the admin map, handles event handler creation, layering etc.
	 * @param mapNode
	 */
	public void addToAdminMap(MapNode mapNode)
	{
		if (!model.getCurrentFloor().getFloorNodes().contains(mapNode))
		{
			System.out.println("Node " + mapNode.getIconType().name() + " added to: " + mapNode.getPosX() + " " + mapNode.getPosY());
			mapNode.setFloor(model.getCurrentFloor());
			model.getCurrentFloor().getFloorNodes().add(mapNode);
		}

		importNode(mapNode); //must occur after adding node to model floor
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
	 * @param mapNode
	 * @author Benjamin Hylak
	 */
	public void addEventHandlersToNode(MapNode mapNode)
	{
		makeMapNodeDraggable(mapNode); //make it draggable

		/***Handles deletion from a popup menu**/
		mapNode.setOnDeleteRequested(event -> {
			removeNode(event.getSource());
		});

		/**Handles when the node is clicked
		 *
		 * Right Click -> Popover
		 * Double click -> Start Drawing
		 * Single Click + Drawing -> Set location
		 */
		mapNode.getNodeToDisplay().setOnMouseClicked(ev -> {

			if(ev.getButton() == MouseButton.SECONDARY) //if right click
			{
				PopOver popOver = mapNode.getEditPopover();

				/***If the name is set, at it to the tree*/
				popOver.setOnHiding(event -> {
					getCurrentTreeView().refresh(); //refresh the treeview once the popup editor closes
				});

				popOver.show(mapNode.getNodeToDisplay(),
						ev.getScreenX(),
						ev.getScreenY());

			}
			else if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse clicks
				if(ev.getClickCount() == 2) // double click
				{
					onStartEdgeDrawing(mapNode);
				} //could add code to print location changes here.
			}

			/*** if...
			 * 1. We are drawing
			 * 2. This node was clicked
			 * 3. This node isn't the source of the edge we are drawing
			 */
			if (drawingEdge!=null && !drawingEdge.getSource().equals(mapNode))
			{
				drawingEdge.setTarget(mapNode);
				onEdgeComplete();
			}
		});

		mapNode.getNodeToDisplay().setOnMouseEntered(ev->
		{
			mapNode.getNodeToDisplay().setOpacity(.65);
		});

		mapNode.getNodeToDisplay().setOnMouseExited(ev->
		{
			mapNode.getNodeToDisplay().setOpacity(1);
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
			if(mapItems.getChildren().contains(drawingEdge.getNodeToDisplay())) //and the right pane has the drawing edge as child
			{
				mapItems.getChildren().remove(drawingEdge.getNodeToDisplay()); //remove from the right pane
			}
		}

		drawingEdge = new NodeEdge();
		drawingEdge.setSource(mapNode);

		mapItems.getChildren().add(drawingEdge.getNodeToDisplay());
		drawingEdge.toBack();
		mapImage.toBack();

		mapNode.getNodeToDisplay().setOnMouseDragEntered(null); //sets drag handlers to null so they can't be repositioned during line drawing
		mapNode.getNodeToDisplay().setOnMouseDragged(null);

		root_pane.setOnKeyPressed(keyEvent-> { //handle escaping from edge creation
			if (drawingEdge != null && keyEvent.getCode() == KeyCode.ESCAPE) {
				if(mapItems.getChildren().contains(drawingEdge.getNodeToDisplay())) //and the right pane has the drawing edge as child
				{
					mapItems.getChildren().remove(drawingEdge.getNodeToDisplay()); //remove from the right pane
				}
				drawingEdge = null;

				mapPane.setOnMouseMoved(null);

				makeMapNodeDraggable(mapNode);
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
	/*
	 removes the node from the model
	 removes the node.getNodeToDisplay() from the map pane
	 removes the node edges associated with that node from the model
	 removes the node edge.getNodeToDisplay() associated with from the map pane
	 */
	private void removeNode(MapNode node)
	{
		for (Iterator<NodeEdge> i = node.getEdges().iterator(); i.hasNext();) {
			NodeEdge edge = (NodeEdge)i.next();
			mapItems.getChildren().remove(edge.getNodeToDisplay()); //remove edge from pane

			model.removeMapEdge(edge); //remove edge from model

			i.remove();
		}

		mapItems.getChildren().remove(node.getNodeToDisplay()); //remove the node

		if(drawingEdge!=null)
		{
			drawingEdge.getNodeToDisplay().setVisible(false); //hide the drawing edge if drawing
			drawingEdge = null; //no longer drawing
		}

		model.removeMapNodeFromCurrentFloor(node); //remove node from mode

		/*********REALLY SHITTY CODEEEE, should specifically use iterator for removal**************/

		TreeItem<MapTreeItem> toDelete = null;

		if(node instanceof Destination)
		{
			for (TreeItem<MapTreeItem> floorItem : getCurrentTreeView().getRoot().getChildren())
			{
				for(TreeItem<MapTreeItem>  nodeItem : floorItem.getChildren())
				{
					if (nodeItem.getValue().getValue().equals((node)))
					{
						toDelete = nodeItem;
						break;
					}
				}
			}

			if(toDelete != null)
			{
				toDelete.getParent().getChildren().remove(toDelete);
				getCurrentTreeView().refresh();
			}
		}
	}

	public void updateEdgeWeights(){
		for(NodeEdge e : model.getCurrentFloor().getFloorEdges()){
			e.updateCost();
		}
	}

	/**Handles saving out all of the map info
	 * @TODO Save directory changes
	 * @throws IOException
	 */

	@FXML
	public void saveInfoAndExit() throws IOException, SQLException
	{
		//removeHandlers();
		updateEdgeWeights();

		refreshNodePositions();

		int i = 0;
		for(NodeEdge e: model.getCurrentFloor().getFloorEdges()){
			System.out.println(Integer.toString(i) + ": Node " + e.getSource().getNodeID() + " Finalized to: " + e.getTarget().getNodeID());
			i++;
		}

		if(model.getCurrentFloor().getKioskNode() == null && model.getCurrentFloor().getFloorNodes().size() > 0){
			System.out.println("ERROR; NO KIOSK NODE SET; SETTING ONE RANDOMLY");
			model.getCurrentFloor().setKioskLocation(model.getCurrentFloor().getFloorNodes().get(0));
		}

		DatabaseManager.getInstance().saveData();

		SceneSwitcher.switchToUserMapView(this.getStage());
	}

	@FXML
	public void onDirectoryEditorSwitch(ActionEvent actionEvent) throws IOException
	{
		SceneSwitcher.switchToModifyDirectoryView(this.getStage());
	}
}
