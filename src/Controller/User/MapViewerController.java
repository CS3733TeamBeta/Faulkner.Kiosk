package Controller.User;

import Controller.AbstractController;
import Controller.Admin.PopUp.OfficeEditController;
import Controller.DragDropMain;
import Controller.Main;
import Controller.SceneSwitcher;
import Domain.Map.MapNode;
import Domain.Map.NodeEdge;
import Domain.Navigation.Guidance;
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
import java.util.HashSet;
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



}
