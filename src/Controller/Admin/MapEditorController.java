package Controller.Admin;

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;

import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Domain.ViewElements.*;
import Domain.ViewElements.Events.EdgeCompleteEvent;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Model.MapEditorModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jfxtras.labs.util.event.MouseControlUtil;
import javafx.scene.input.KeyCode;

public class MapEditorController extends AnchorPane{

	@FXML SplitPane base_pane;
	@FXML AnchorPane right_pane;
	@FXML VBox left_pane;

	private DragIcon mDragOverIcon = null;
	
	private EventHandler<DragEvent> onIconDragOverRoot = null;
	private EventHandler<DragEvent> onIconDragDropped = null;
	private EventHandler<DragEvent> onIconDragOverRightPane = null;
	private EventHandler<DragEvent> onLineSyncNeeded = null;
	private MapEditorModel model;

	NodeEdge drawingEdge;

	public MapEditorController() {
		
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/Admin/MapBuilder/MapEditor.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}

		//Runs once the edge is drawn from one node to another
		//connects the two, sends sources, positions them etc.
		model.addEdgeCompleteHandler(event->
		{
			MapNode sourceNode = event.getNodeEdge().getSource();
			MapNode targetNode = event.getNodeEdge().getTarget();

			model.addMapEdge(drawingEdge);

			right_pane.setOnMouseMoved(null);

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
		});
	}

	public void onEdgeComplete()
	{
		for(EdgeCompleteEventHandler handler : model.getEdgeCompleteHandlers())
		{
			handler.handle(new EdgeCompleteEvent(drawingEdge));
		}
	}

	@FXML
	private void initialize() {

		if(model==null) model = new MapEditorModel();

		//Add one icon that will be used for the drag-drop process
		//This is added as a child to the root anchorpane so it can be visible
		//on both sides of the split pane.
		mDragOverIcon = new DragIcon();
		mDragOverIcon.setVisible(false);
		mDragOverIcon.setOpacity(0.65);

		getChildren().add(mDragOverIcon);
		
		//populate left pane with multiple colored icons for testing
		for (int i = 0; i < 6; i++)
		{
			DragIcon icn = new DragIcon();
			
			addDragDetection(icn);
			icn.setType(DragIconType.values()[i]);

			model.addSideBarIcon(icn);
		}

		left_pane.getChildren().setAll(model.getSideBarIcons());

		buildDragHandlers();
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
						((NodeEdge)edge).updatePosViaNode(n);
					}

					n.setPosX(event.getSceneX());
					n.setPosY(event.getSceneY());

					System.out.println("Node " + n.getIconType().name() + " moved to (X: "+ event.getSceneX() + ", Y: " + event.getSceneY() + ")");
				},
				null);
	}

	private void addDragDetection(DragIcon dragIcon) {
		
		dragIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				// set drag event handlers on their respective objects
				base_pane.setOnDragOver(onIconDragOverRoot);
				right_pane.setOnDragOver(onIconDragOverRightPane);
				right_pane.setOnDragDropped(onIconDragDropped);
				
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
	
	private void buildDragHandlers() {
		
		//drag over transition to move widget form left pane to right pane
		onIconDragOverRoot = new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				
				Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

				//turn on transfer mode and track in the right-pane's context 
				//if (and only if) the mouse cursor falls within the right pane's bounds.
				if (!right_pane.boundsInLocalProperty().get().contains(p)) {
					
					event.acceptTransferModes(TransferMode.ANY);
					mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
					return;
				}

				event.consume();
			}
		};
		
		onIconDragOverRightPane = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

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
				
		onIconDragDropped = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {
				
				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);
				
				container.addData("scene_coords", new Point2D(event.getSceneX(), event.getSceneY()));
				
				ClipboardContent content = new ClipboardContent();
				content.put(DragContainer.AddNode, container);
				
				event.getDragboard().setContent(content);
				event.setDropCompleted(true);
			}
		};

		this.setOnDragDone (new EventHandler <DragEvent> (){
			
			@Override
			public void handle (DragEvent event) {
				
				right_pane.removeEventHandler(DragEvent.DRAG_OVER, onIconDragOverRightPane); //remove the event handlers created on drag start
				right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, onIconDragDropped);
				base_pane.removeEventHandler(DragEvent.DRAG_OVER, onIconDragOverRoot);
								
				mDragOverIcon.setVisible(false);
				
				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode); //information from the drop
				
				if (container != null) {

					if (container.getValue("scene_coords") != null) {

						MapNode droppedNode = new MapNode(); // make a new  map node
						makeMapNodeDraggable(droppedNode); //make it draggable
						
						droppedNode.setType(DragIconType.valueOf(container.getValue("type"))); //set the type
						right_pane.getChildren().add(droppedNode.getNodeToDisplay()); //add to right panes children
						model.addMapNode(droppedNode); //add node to model

						droppedNode.toFront(); //send the node to the front

						Point2D cursorPoint = container.getValue("scene_coords"); //cursor point

						/* Build up event handlers for this droppedNode */

						((DragIcon)droppedNode.getNodeToDisplay()).relocateToPoint(new Point2D(cursorPoint.getX() - 32,
								cursorPoint.getY()-32)); //32 is half of 64, so half the height/width... @TODO

						droppedNode.getNodeToDisplay().setOnMouseClicked(ev -> {
							if(ev.getButton() == MouseButton.SECONDARY) //if right click
							{
								if(drawingEdge != null) //if currently drawing... handles case of right clicking to start a new node
								{
									if(right_pane.getChildren().contains(drawingEdge.getNodeToDisplay())) //and the right pane has the drawing edge as child
									{
										right_pane.getChildren().remove(drawingEdge.getNodeToDisplay()); //remove from the right pane
									}
								}

								drawingEdge = new NodeEdge();
								drawingEdge.setSource(droppedNode);

								right_pane.getChildren().add(drawingEdge.getNodeToDisplay());
								drawingEdge.toBack();

								droppedNode.getNodeToDisplay().setOnMouseDragEntered(null); //sets drag handlers to null so they can't be repositioned during line drawing
								droppedNode.getNodeToDisplay().setOnMouseDragged(null);

								setOnKeyPressed(keyEvent-> { //handle escaping from edge creation
									if (drawingEdge!=null && keyEvent.getCode() == KeyCode.ESCAPE) {
										if(right_pane.getChildren().contains(drawingEdge.getNodeToDisplay())) //and the right pane has the drawing edge as child
										{
											right_pane.getChildren().remove(drawingEdge.getNodeToDisplay()); //remove from the right pane
										}
										drawingEdge = null;

										right_pane.setOnMouseMoved(null);

										makeMapNodeDraggable(droppedNode);
									}
								});

								right_pane.setOnMouseMoved(mouseEvent->{ //handle mouse movement in the right pane

									if(drawingEdge!=null)
									{
										Point p = MouseInfo.getPointerInfo().getLocation(); // get the absolute current loc of the mouse on screen
										Point2D mouseCoords = drawingEdge.getEdgeLine().screenToLocal(p.x, p.y); // convert coordinates to relative within the window
										drawingEdge.setEndPoint(mouseCoords); //set the end point
									}
								});
							}
							else if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse clicks

								if(ev.getClickCount() == 2){ // double click

									for (Iterator<NodeEdge> i = droppedNode.getEdges().iterator(); i.hasNext();) {
										NodeEdge edge = (NodeEdge)i.next();
										right_pane.getChildren().remove(edge.getNodeToDisplay()); //remove edge from pane

										model.removeMapEdge(edge); //remove edge from model

										i.remove();
									}

									right_pane.getChildren().remove(droppedNode.getNodeToDisplay()); //remove the node

									if(drawingEdge!=null)
									{
										drawingEdge.getNodeToDisplay().setVisible(false); //hide the drawing edge if drawing
										drawingEdge = null; //no longer drawing
									}

									model.removeMapNodeFromCurrentFloor(droppedNode); //remove node from model
								}
							}

							if (drawingEdge!=null && !drawingEdge.getSource().equals(droppedNode))
							{
								drawingEdge.setTarget(droppedNode);
								onEdgeComplete();
							}
						});

						droppedNode.getNodeToDisplay().setOnMouseEntered(ev->
						{
							droppedNode.getNodeToDisplay().setOpacity(.65);
						});

						droppedNode.getNodeToDisplay().setOnMouseExited(ev->
						{
							droppedNode.getNodeToDisplay().setOpacity(1);
						});
					}
				}
				event.consume();
			}
		});

	}
}
