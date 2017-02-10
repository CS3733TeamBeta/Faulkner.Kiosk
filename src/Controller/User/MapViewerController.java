package Controller.User;

import Controller.AbstractController;
import Controller.Admin.PopUp.OfficeEditController;
import Controller.DragDropMain;
import Controller.Main;
import Controller.SceneSwitcher;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Domain.Navigation.Path;
import Domain.ViewElements.DragContainer;
import Domain.ViewElements.DragIcon;
import Domain.ViewElements.DragIconType;
import Domain.ViewElements.Events.EdgeCompleteEvent;
import Domain.ViewElements.Events.EdgeCompleteEventHandler;
import Exceptions.PathFindingException;
import Model.MapModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import jfxtras.labs.util.event.MouseControlUtil;
import org.controlsfx.control.PopOver;

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class MapViewerController extends AbstractController {

	@FXML SplitPane base_pane;
	@FXML AnchorPane mapPane;
	@FXML HBox bottom_bar;
	@FXML AnchorPane root_pane;
	@FXML ImageView mapImage;

	private DragIcon mDragOverIcon = null;
	private MapModel model;

	NodeEdge drawingEdge;

	@FXML
	public void saveInfoAndExit() throws IOException{
		//DragDropMain.mvm.setCurrentFloor(this.model.getCurrentFloor());
		//SceneSwitcher.switchToModifyLocationsView(this.getStage());
	}

	@FXML
	public void goToAdminSide() throws IOException{
		SceneSwitcher.switchToLoginView(this.getStage());
	}

	public MapViewerController() {
		model = new MapModel();
	}

	protected void renderInitialMap(){
			if(DragDropMain.mvm != null) {
				//System.out.println("Nodes to add: " + DragDropMain.mvm.getCurrentFloor().getFloorNodes().size());
				//import a model if one exists
				model.setCurrentFloor(DragDropMain.mvm.getCurrentFloor());
			}
			else if(Main.mvm != null) {
				model.setCurrentFloor(Main.mvm.getCurrentFloor());
			}
			if(Main.mvm != null || DragDropMain.mvm != null){
				//and then set all the existing nodes up
				for(MapNode n : model.getCurrentFloor().getFloorNodes()){
					//System.out.println("Adding node");
					setupImportedNode(n);
				}
				//System.out.println("Edges to add: " + DragDropMain.mvm.getCurrentFloor().getFloorEdges().size());
				for(NodeEdge edge : model.getCurrentFloor().getFloorEdges()){
					drawingEdge = edge;
					drawingEdge.changeColor(Color.RED);
					//Opacity goes from 0 to 1
					drawingEdge.changeOpacity(0.0);

					MapNode sourceNode = edge.getSource();
					MapNode targetNode = edge.getTarget();

					drawingEdge.setSource(sourceNode);
					drawingEdge.setTarget(targetNode);

					drawingEdge.updatePosViaNode(sourceNode);
					drawingEdge.updatePosViaNode(targetNode);

					model.addMapEdge(drawingEdge);

					drawingEdge.updatePosViaNode(sourceNode);
					drawingEdge.updatePosViaNode(targetNode);

					drawingEdge.updatePosViaNode(sourceNode);
					drawingEdge.updatePosViaNode(targetNode);

					sourceNode.toFront();
					targetNode.toFront();

					drawingEdge.updatePosViaNode(sourceNode);
					drawingEdge.updatePosViaNode(targetNode);

					//System.out.println("drawingedge goes from " + sourceNode.getNodeID() + " to " + targetNode.getNodeID());

					mapPane.getChildren().add(drawingEdge.getNodeToDisplay());

					drawingEdge.updatePosViaNode(sourceNode);
					drawingEdge.updatePosViaNode(targetNode);
					drawingEdge = null;
				}

			}
			else{
				model = new MapModel();
			}

	}

	@FXML
	private void initialize() {

		renderInitialMap();

		//Add one icon that will be used for the drag-drop process
		//This is added as a child to the root anchorpane so it can be visible
		//on both sides of the split pane.
		mDragOverIcon = new DragIcon();
		mDragOverIcon.setVisible(false);
		mDragOverIcon.setOpacity(0.65);

		//populate left pane with multiple colored icons for testing
		for (int i = 0; i < DragIconType.values().length; i++)
		{
			DragIcon icn = new DragIcon();

			icn.setStyle("-fx-background-size: 64 64");

			icn.setType(DragIconType.values()[i]);

			if (icn.getType().equals(DragIconType.connector))
			{
				//System.out.println("Adding Connector");
				icn.setStyle("-fx-background-size: 30 30");
			}

			model.addSideBarIcon(icn);
			bottom_bar.getChildren().add(icn);
		}
	}

	private void setupImportedNode(MapNode droppedNode){

		//droppedNode.setType(droppedNode.getIconType()); //set the type
		mapPane.getChildren().add(droppedNode.getNodeToDisplay()); //add to right panes children
		model.addMapNode(droppedNode); //add node to model

		droppedNode.toFront(); //send the node to the front

		droppedNode.getNodeToDisplay().setOnMouseClicked(ev -> {
			if (ev.getButton() == MouseButton.PRIMARY) { // deal with other types of mouse clicks
				try{
					findPathToNode(droppedNode);
				}catch(PathFindingException e){

				}
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

	protected void findPathToNode(MapNode endPoint) throws PathFindingException {
		System.out.println("In path finding");
		Path newRoute;
		MapNode startPoint = model.getCurrentFloor().getKioskNode();
		if (endPoint == startPoint) {
			System.out.println("ERROR; CANNOT FIND PATH BETWEEN SAME NODES");
			return;//TODO add error message of some kind
		}
		try {
			newRoute = new Path(startPoint, endPoint, false);
		} catch (PathFindingException e) {
			return;//TODO add error message throw
		}

		for (NodeEdge edge : model.getCurrentFloor().getFloorEdges()) {
			if(newRoute.getPathEdges().contains(edge)) {
				edge.changeOpacity(1.0);
			}
			else{
				edge.changeOpacity(0.0);
			}
		}
	}

}
