package Controller.Admin;

import java.awt.*;
import java.io.IOException;

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

	private MapEditorModel model;

	GraphicalNodeEdge drawingEdge;

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

		model.addEdgeCompleteHandler(event->
		{
			model.addMapEdge(drawingEdge);

			right_pane.setOnMouseMoved(null);

			drawingEdge.setSource(event.getNodeEdge().getSource());
			drawingEdge.setTarget(event.getNodeEdge().getTarget());

			event.getNodeEdge().getSource().addEdge(drawingEdge); // add the current drawing edge to the list of this node's edges
			event.getNodeEdge().getTarget().addEdge(drawingEdge); // add the current drawing edge to the list of this node's edges

			/*MouseControlUtil.makeDraggable(event.getNodeEdge().getSource(), //could be used to track node and update line
					ev->{

					},
					ev
					 ->{;

					});*/

			MouseControlUtil.makeDraggable(event.getNodeEdge().getSource());
			MouseControlUtil.makeDraggable(event.getNodeEdge().getTarget());

			drawingEdge.toBack(); //send drawing edge to back
			drawingEdge = null;

			event.getNodeEdge().getSource().toFront();
			event.getNodeEdge().getTarget().toFront();
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

						GraphicalMapNode droppedNode = new GraphicalMapNode(); // make a new graphical map node
						MouseControlUtil.makeDraggable(droppedNode); //make it draggable
						
						droppedNode.setType(DragIconType.valueOf(container.getValue("type"))); //set the type
						right_pane.getChildren().add(droppedNode); //add to right panes children
						model.addMapNode(droppedNode); //add node to model

						droppedNode.toFront(); //send the node to the front

						Point2D cursorPoint = container.getValue("scene_coords"); //cursor point

						droppedNode.relocateToPoint(new Point2D(cursorPoint.getX() - 32,
								cursorPoint.getY()-32)); //32 is half of 64, so half the height/width... @TODO

						droppedNode.setOnMouseClicked(ev -> {
							if(ev.getButton() == MouseButton.SECONDARY) //if right click
							{
								drawingEdge = new GraphicalNodeEdge();
								drawingEdge.setSource(droppedNode);

								right_pane.getChildren().add(drawingEdge);
								drawingEdge.toBack();

								droppedNode.setOnMouseDragEntered(null); //sets drag handlers to null so they can't be repositioned during line drawing
								droppedNode.setOnMouseDragged(null);

								setOnKeyPressed(keyEvent-> { //handle escaping from edge creation
									if (drawingEdge!=null && keyEvent.getCode() == KeyCode.ESCAPE) {
										drawingEdge.setVisible(false);
										drawingEdge = null;
										right_pane.setOnMouseMoved(null);

										MouseControlUtil.makeDraggable(droppedNode);
									}
								});

								right_pane.setOnMouseMoved(mouseEvent->{ //handle mouse movement in the right pane

									Point p = MouseInfo.getPointerInfo().getLocation(); // get the absolute current loc of the mouse on screen
									Point2D mouseCoords = drawingEdge.screenToLocal(p.x, p.y); // convert coordinates to relative within the window
									drawingEdge.setEnd(mouseCoords); //set the end point
								});
							}
							else if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse buttons

								if(ev.getClickCount() == 2){ // double click

									for (GraphicalNodeEdge edge: droppedNode.getEdges())
									{
										right_pane.getChildren().remove(edge); //remove edge from pane
										model.removeMapEdge(edge); //remove edge from model
									}

									right_pane.getChildren().remove(droppedNode); //remove the node

									if(drawingEdge!=null)
									{
										drawingEdge.setVisible(false); //hide the drawing edge if drawing
										drawingEdge = null; //no longer drawing
									}

									model.removeMapNode(droppedNode); //remove node from model
								}
							}

							if (drawingEdge!=null && !drawingEdge.getSource().equals(droppedNode))
							{
								drawingEdge.setTarget(droppedNode);
								onEdgeComplete();
							}
						});

						droppedNode.setOnMouseEntered(ev->
						{
							droppedNode.setOpacity(.65);
						});

						droppedNode.setOnMouseExited(ev->
						{
							droppedNode.setOpacity(1);
						});
					}
				}
				event.consume();
			}
		});

	}
}
