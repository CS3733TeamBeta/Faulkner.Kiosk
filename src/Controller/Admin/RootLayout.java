package Controller.Admin;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import Domain.ViewElements.*;
import Domain.ViewElements.Events.EdgeCompleteEvent;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jfxtras.labs.util.event.MouseControlUtil;
import javafx.scene.input.KeyCode;

public class RootLayout extends AnchorPane{

	@FXML SplitPane base_pane;
	@FXML AnchorPane right_pane;
	@FXML VBox left_pane;

	private DragIcon mDragOverIcon = null;
	
	private EventHandler<DragEvent> mIconDragOverRoot = null;
	private EventHandler<DragEvent> mIconDragDropped = null;
	private EventHandler<DragEvent> mIconDragOverRightPane = null;

	private LinkedList<EdgeCompleteEventHandler> edgeCompleteHandlers;

	GraphicalNodeEdge drawingEdge;

	boolean isDrawingEdge = false;

	public RootLayout() {
		
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/Admin/MapBuilder/RootLayout.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}

		edgeCompleteHandlers = new LinkedList<EdgeCompleteEventHandler>(); //instantiate empty linked list for handlers;

		edgeCompleteHandlers.add(event->
		{

			right_pane.setOnMouseMoved(null);

			GraphicalNodeEdge newEdge = new GraphicalNodeEdge();

			right_pane.getChildren().add(newEdge.getEdgeLine());

			newEdge.setSource(event.getNodeEdge().getSource());
			newEdge.setTarget(event.getNodeEdge().getTarget());

			event.getNodeEdge().getSource().addEdge(newEdge); // add the current drawing edge to the list of this node's edges
			event.getNodeEdge().getTarget().addEdge(newEdge); // add the current drawing edge to the list of this node's edges

			/*MouseControlUtil.makeDraggable(event.getNodeEdge().getSource(),
					ev->{

					},
					ev
					 ->{;

					});*/

			MouseControlUtil.makeDraggable(event.getNodeEdge().getTarget());


			drawingEdge.toBack();
			newEdge.toBack();

			event.getNodeEdge().getSource().toFront();
			event.getNodeEdge().getTarget().toFront();

			drawingEdge.resetEdge();
		});

	}

	public void onEdgeComplete() {
		for(EdgeCompleteEventHandler handler : edgeCompleteHandlers)
		{
			handler.handle(new EdgeCompleteEvent(drawingEdge));
		}
	}

	@FXML
	private void initialize() {
		
		//Add one icon that will be used for the drag-drop process
		//This is added as a child to the root anchorpane so it can be visible
		//on both sides of the split pane.
		mDragOverIcon = new DragIcon();
		
		mDragOverIcon.setVisible(false);
		mDragOverIcon.setOpacity(0.65);

		getChildren().add(mDragOverIcon);
		
		//populate left pane with multiple colored icons for testing
		for (int i = 0; i < 6; i++) {
			
			DragIcon icn = new DragIcon();
			
			addDragDetection(icn);
			
			icn.setType(DragIconType.values()[i]);
			left_pane.getChildren().add(icn);
		}

		drawingEdge = new GraphicalNodeEdge();
		right_pane.getChildren().add(drawingEdge);

		buildDragHandlers();
	}
	
	private void addDragDetection(DragIcon dragIcon) {
		
		dragIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				// set drag event handlers on their respective objects
				base_pane.setOnDragOver(mIconDragOverRoot);
				right_pane.setOnDragOver(mIconDragOverRightPane);
				right_pane.setOnDragDropped(mIconDragDropped);
				
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
		mIconDragOverRoot = new EventHandler <DragEvent>() {

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
		
		mIconDragOverRightPane = new EventHandler <DragEvent> () {

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
				
		mIconDragDropped = new EventHandler <DragEvent> () {

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
				
				right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
				right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
				base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);
								
				mDragOverIcon.setVisible(false);
				
				DragContainer container = (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);
				
				if (container != null) {

					if (container.getValue("scene_coords") != null) {

						GraphicalMapNode droppedNode = new GraphicalMapNode();
						MouseControlUtil.makeDraggable(droppedNode);
						
						droppedNode.setType(DragIconType.valueOf(container.getValue("type")));
						right_pane.getChildren().add(droppedNode);

						Point2D cursorPoint = container.getValue("scene_coords");

						droppedNode.relocateToPoint(new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32));

						droppedNode.setOnMouseClicked(ev -> {
							if(ev.getButton() == MouseButton.SECONDARY)
							{
								isDrawingEdge = true;

								drawingEdge.setVisible(true);

								/*drawingEdge = new GraphicalNodeEdge(); CAUTION: Breaks code for unknown reasons
								right_pane.getChildren().add(drawingEdge);*/ //Uncomment to strategically destroy code

								droppedNode.setOnMouseDragEntered(null);
								droppedNode.setOnMouseDragged(null);

								Bounds boundsInScene = droppedNode.getBoundsInLocal();

								Point2D startPoint = new Point2D(
										boundsInScene.getMinX() + (boundsInScene.getWidth() / 2),
										boundsInScene.getMinY() + (boundsInScene.getHeight() / 2)
								);

								drawingEdge.setSource(droppedNode);

								right_pane.setOnMouseMoved(mouseEvent->{

									Point p = MouseInfo.getPointerInfo().getLocation(); // get the absolute current loc of the mouse on screen
									Point2D mouseCoords = drawingEdge.screenToLocal(p.x, p.y); // convert coordinates to relative within the window
									drawingEdge.setEnd(mouseCoords);

									getParent().setOnKeyPressed(keyEvent-> {
										if (keyEvent.getCode() == KeyCode.ESCAPE) {
											isDrawingEdge = false;
											drawingEdge.setVisible(false);
											drawingEdge.resetEdge();
										}

										isDrawingEdge = false;
									});

								});
							}
							else if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse buttons

								if(ev.getClickCount() == 2){ // double click

									System.out.println("Removing node and edges");

									for (GraphicalNodeEdge edge: droppedNode.getEdges())
									{
										System.out.print("Removing Edge: ");
										System.out.println(right_pane.getChildren().remove(edge.getEdgeLine()));
									}

									System.out.print("Removing Node: ");
									System.out.println(right_pane.getChildren().remove(droppedNode));

									drawingEdge.setVisible(false);
								}
							}

							if (isDrawingEdge && !drawingEdge.getSource().equals(droppedNode))
							{
								isDrawingEdge = false;
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

	/*
	public void buildSplitPaneDragHandlers() {
		
		//drag detection for widget in the left-hand scroll pane to create a node in the right pane 
		mWidgetDragDetected = new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {

				fs_right_pane.setOnDragDropped(null);
				fs_root.setOnDragOver(null);
				fs_right_pane.setOnDragOver(null);
				
				fs_right_pane.setOnDragDropped(mRightPaneDragDropped);
				fs_root.setOnDragOver(mRootDragOver);
				
                //begin drag ops

                mDragObject = ((IFileSystemObject) (event.getSource())).getDragObject();
                
                if (!fs_root.getChildren().contains((Node)mDragObject))
                	fs_root.getChildren().add((Node)mDragObject);
                
                mDragObject.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));
                
                ClipboardContent content = new ClipboardContent();
                content.putString(mDragObject.getFileSystemType().toString());

                mDragObject.startDragAndDrop (TransferMode.ANY).setContent(content);
                mDragObject.setVisible(true);
                
                event.consume();					
			}					
		};
		
		//drag over transition to move widget form left pane to right pane
		mRootDragOver = new EventHandler <DragEvent>() {

			@Override
			public void handle(DragEvent event) {
				
				Point2D p = fs_right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

				if (!fs_right_pane.boundsInLocalProperty().get().contains(p)) {
					mDragObject.relocateToPoint(new Point2D(event.getX(), event.getY()));
					return;
				}

				fs_root.removeEventHandler(DragEvent.DRAG_OVER, this);
				fs_right_pane.setOnDragOver(mRightPaneDragOver);
				event.consume();

			}
		};
		
		//drag over in the right pane
		mRightPaneDragOver = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {

				event.acceptTransferModes(TransferMode.ANY);
				mDragObject.relocateToPoint(mDragObject.getParent().sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY())));
				
				event.consume();
			}
		};		
		
		//drop action in the right pane to create a new node
		mRightPaneDragDropped = new EventHandler <DragEvent> () {

			@Override
			public void handle(DragEvent event) {
				Point2D p = fs_right_pane.sceneToLocal(new Point2D (event.getSceneX(), event.getSceneY()));	
				
				self.addFileSystemNode(mDragObject.getFileSystemType(), p);
				event.setDropCompleted(true);

				fs_right_pane.setOnDragOver(null);
				fs_right_pane.setOnDragDropped(null);
				fs_root.setOnDragOver(null);
				
				event.consume();
			}
		};		
	}
	*/
}
