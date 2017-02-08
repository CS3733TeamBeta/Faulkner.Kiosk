package Controller.Admin;

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;

import Controller.AbstractController;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Domain.ViewElements.*;
import Domain.ViewElements.Events.EdgeCompleteEvent;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Model.MapEditorModel;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import jfxtras.labs.util.event.MouseControlUtil;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.PopOver;
import org.controlsfx.samples.HelloPopOver;

public class MapEditorController extends AbstractController {

	@FXML SplitPane base_pane;
	@FXML AnchorPane mapPane;
	@FXML HBox bottom_bar;
	@FXML AnchorPane root_pane;
	@FXML ImageView mapImage;

	private DragIcon mDragOverIcon = null;
	
	private EventHandler<DragEvent> onIconDragOverRoot = null;
	private EventHandler<DragEvent> onIconDragDropped = null;
	private EventHandler<DragEvent> onIconDragOverRightPane = null;
	private EventHandler<DragEvent> onLineSyncNeeded = null;
	private MapEditorModel model;

	NodeEdge drawingEdge;

	public MapEditorController() {
		
		/*FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/Admin/MapBuilder/MapEditor.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
		*/
		model = new MapEditorModel();

		//Runs once the edge is drawn from one node to another
		//connects the two, sends sources, positions them etc.
		model.addEdgeCompleteHandler(event->
		{
			NodeEdge completedEdge = drawingEdge;

			completedEdge.getNodeToDisplay().setOnMouseEntered(deEvent->{
				if (completedEdge != null)
				{
					completedEdge.getEdgeLine().setStroke(Color.RED);
				}
			});

			completedEdge.getNodeToDisplay().setOnMouseExited(deEvent->{
				if (completedEdge != null) {
					completedEdge.getEdgeLine().setStroke(Color.BLACK);
				}
			});

			completedEdge.getNodeToDisplay().setOnMouseClicked(deEvent->{
				if (completedEdge != null) {
					if (deEvent.getClickCount() == 2) {
						completedEdge.getSource().getEdges().remove(completedEdge);
						completedEdge.getTarget().getEdges().remove(completedEdge);
						mapPane.getChildren().remove(completedEdge.getNodeToDisplay()); //remove from the right pane
						model.removeMapEdge(completedEdge);
					}
				}
			});

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

	public void onEdgeComplete() {
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

		root_pane.getChildren().add(mDragOverIcon);
		
		//populate left pane with multiple colored icons for testing
		for (int i = 0; i < DragIconType.values().length; i++)
		{
			DragIcon icn = new DragIcon();

			icn.setStyle("-fx-background-size: 64 64");

			addDragDetection(icn);
			icn.setType(DragIconType.values()[i]);

			model.addSideBarIcon(icn);
			bottom_bar.getChildren().add(icn);
		}

		//bottom_bar.getChildren().setAll(model.getSideBarIcons());

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

	private void buildDragHandlers() {
		
		//drag over transition to move widget form left pane to right pane
		onIconDragOverRoot = new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				
				Point2D p = mapPane.sceneToLocal(event.getSceneX(), event.getSceneY());

				//turn on transfer mode and track in the right-pane's context 
				//if (and only if) the mouse cursor falls within the right pane's bounds.
				if (!mapPane.boundsInLocalProperty().get().contains(p)) {
					
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

		root_pane.setOnDragDone (new EventHandler <DragEvent> (){
			
			@Override
			public void handle (DragEvent event) {
				
				mapPane.removeEventHandler(DragEvent.DRAG_OVER, onIconDragOverRightPane); //remove the event handlers created on drag start
				mapPane.removeEventHandler(DragEvent.DRAG_DROPPED, onIconDragDropped);
				base_pane.removeEventHandler(DragEvent.DRAG_OVER, onIconDragOverRoot);
								
				mDragOverIcon.setVisible(false);
				
				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode); //information from the drop
				
				if (container != null) {

					if (container.getValue("scene_coords") != null) {

						MapNode droppedNode = new MapNode(); // make a new  map node
						makeMapNodeDraggable(droppedNode); //make it draggable
						
						droppedNode.setType(DragIconType.valueOf(container.getValue("type"))); //set the type
						mapPane.getChildren().add(droppedNode.getNodeToDisplay()); //add to right panes children
						model.addMapNode(droppedNode); //add node to model

						droppedNode.toFront(); //send the node to the front

						Point2D cursorPoint = container.getValue("scene_coords"); //cursor point

						/* Build up event handlers for this droppedNode */

						((DragIcon)droppedNode.getNodeToDisplay()).relocateToPoint(new Point2D(cursorPoint.getX() - 32,
								cursorPoint.getY()-32)); //32 is half of 64, so half the height/width... @TODO

						droppedNode.getNodeToDisplay().setOnMouseClicked(ev -> {
							if(ev.getButton() == MouseButton.SECONDARY) //if right click
							{
								PopOver popOver = new PopOver();

								popOver.show(droppedNode.getNodeToDisplay(),
										ev.getScreenX(),
										ev.getScreenY());

								if(drawingEdge != null) //if currently drawing... handles case of right clicking to start a new node
								{
									if(mapPane.getChildren().contains(drawingEdge.getNodeToDisplay())) //and the right pane has the drawing edge as child
									{
										mapPane.getChildren().remove(drawingEdge.getNodeToDisplay()); //remove from the right pane
									}
								}

								drawingEdge = new NodeEdge();
								drawingEdge.setSource(droppedNode);

								mapPane.getChildren().add(drawingEdge.getNodeToDisplay());
								drawingEdge.toBack();
								mapImage.toBack();

								droppedNode.getNodeToDisplay().setOnMouseDragEntered(null); //sets drag handlers to null so they can't be repositioned during line drawing
								droppedNode.getNodeToDisplay().setOnMouseDragged(null);

								root_pane.setOnKeyPressed(keyEvent-> { //handle escaping from edge creation
									if (drawingEdge!=null && keyEvent.getCode() == KeyCode.ESCAPE) {
										if(mapPane.getChildren().contains(drawingEdge.getNodeToDisplay())) //and the right pane has the drawing edge as child
										{
											mapPane.getChildren().remove(drawingEdge.getNodeToDisplay()); //remove from the right pane
										}
										drawingEdge = null;

										mapPane.setOnMouseMoved(null);

										makeMapNodeDraggable(droppedNode);
									}
								});

								mapPane.setOnMouseMoved(mouseEvent->{ //handle mouse movement in the right pane

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
										mapPane.getChildren().remove(edge.getNodeToDisplay()); //remove edge from pane

										model.removeMapEdge(edge); //remove edge from model

										i.remove();
									}

									mapPane.getChildren().remove(droppedNode.getNodeToDisplay()); //remove the node

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
