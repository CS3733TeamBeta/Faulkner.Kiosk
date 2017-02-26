package Controller.Admin;

import Boundary.AdminMapBoundary;
import Controller.AbstractController;
import Controller.MapController;
import Controller.SceneSwitcher;
import Domain.Map.*;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DragIconType;
import Domain.ViewElements.Events.EdgeCompleteEvent;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Model.DataSourceClasses.MapTreeItem;
import Model.DataSourceClasses.TreeViewWithItems;
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

public class MapEditorController extends MapController
{

	@FXML SplitPane base_pane;
	@FXML AnchorPane mapPane;
	@FXML HBox bottom_bar;
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

	public MapEditorController()
	{
		super();

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
		drawingEdgeLine = new Line();
		drawingEdgeLine.setStrokeWidth(5);

		mapPane.getChildren().add(mapImage);
		mapPane.getChildren().add(mapItems);

		drawingEdgeLine.setVisible(false);

		BuildingTabPane.getTabs().clear();

		initBoundary();

		adminBoundary.addEdgeSetChangeHandler(change->
		{
			System.out.println("Edge list modified");

			if(change.wasAdded())
			{
				NodeEdge edge = change.getElementAdded();
				Line line = new Line();
				line.setStrokeWidth(5);

				mapPane.getChildren().add(line);
				line.toBack();
				mapImage.toBack();

				edgeEntityMap.put(line, change.getElementAdded());

				addHandlersToEdge(line);
				updateEdgeLine(edge);
			}
			else if(change.wasRemoved())
			{
				Line l = edgeEntityMap.inverse().get(change.getElementRemoved());
				mapPane.getChildren().remove(l);
				edgeEntityMap.remove(l);
			}
		});

		adminBoundary.setInitialFloor();

		menu = new CirclePopupMenu(mapPane, null);

		/*mapPane.setOnMouseClicked(event->
		{
			menu.show(event.getScreenX(), event.getScreenY());

			target.setVisible(true);

			target.setX(event.getX()-target.getFitWidth()/2);
			target.setY(event.getY()-target.getFitHeight()/2);

			menuOpenLoc = new Point2D(event.getX(), event.getY());
		});*/

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
				adminBoundary.newNode(icn.getType(), menuOpenLoc);
				target.setVisible(false);
			});

			menu.getItems().add(item);
		}

		target.setVisible(false);
		target.setFitWidth(14);
		target.setFitHeight(14);
		mapPane.getChildren().add(target);
	}

	@Override
	protected DragIcon importMapNode(MapNode n)
	{
		DragIcon icon = super.importMapNode(n);
		addEventHandlersToNode(icon);

		return icon;
	}

	/*public void createCampusTab()
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
	*/

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
		if(!f.equals(boundary.getCurrentFloor()))
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

			boundary.setCurrentFloor(f);

			System.out.println("Changed floor to " + f);
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
					adminBoundary.removeEdge(edgeEntityMap.get(edge));
				}
			}
		});
	}

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
	}

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
	    makeIconDraggable(n); //make it draggable

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

		Point2D startPoint = mapPane.sceneToLocal(mapPane.localToScene(mapNode.getPosX(), mapNode.getPosY()));
		drawingEdgeLine.setStartY(startPoint.getY());
		drawingEdgeLine.setStartX(startPoint.getX());
		drawingEdgeLine.setEndX(startPoint.getX());
		drawingEdgeLine.setEndY(startPoint.getY());

		drawingEdgeLine.setVisible(true);

		System.out.println("Set source to " + mapNode.getPosX());

		drawingEdgeLine.toBack();
		mapImage.toBack();

		iconEntityMap.inverse().get(mapNode).setOnMouseDragEntered(null); //sets drag handlers to null so they can't be repositioned during line drawing
		iconEntityMap.inverse().get(mapNode).setOnMouseDragged(null);

		root_pane.setOnKeyPressed(keyEvent-> { //handle escaping from edge creation
			if (drawingEdge != null && keyEvent.getCode() == KeyCode.ESCAPE) {
				if(drawingEdgeLine.isVisible()) //and the right pane has the drawing edge as child
				{
					drawingEdgeLine.setVisible(false);
				}

				drawingEdge = null;
				mapPane.setOnMouseMoved(null);
			}
		});

		mapPane.setOnMouseMoved(mouseEvent->{ //handle mouse movement in the right pane

			if (drawingEdge != null)
			{
				Point p = MouseInfo.getPointerInfo().getLocation(); // get the absolute current loc of the mouse on screen
				Point2D mouseCoords = drawingEdgeLine.screenToLocal(p.x, p.y); // convert coordinates to relative within the window
				drawingEdgeLine.setEndX(mouseCoords.getX());
				drawingEdgeLine.setEndY(mouseCoords.getY());
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
		//new DatabaseManager().saveData(model.getHospital());

		SceneSwitcher.switchToUserMapView(this.getStage());
	}

	@FXML
	public void onDirectoryEditorSwitch(ActionEvent actionEvent) throws IOException
	{
		//SceneSwitcher.switchToModifyDirectoryView(this.getStage(), model.getHospital());
	}
}
