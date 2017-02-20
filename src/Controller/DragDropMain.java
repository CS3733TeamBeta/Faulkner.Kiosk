package Controller;

import Controller.Admin.MapEditorController;
import Domain.Map.*;
import Model.MapModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DragDropMain extends Application {

	MapNode nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH, nodeI, nodeJ, nodeK, nodeL, nodeM, nodeN, nodeO, nodeP, nodeQ, nodeR, nodeS, nodeT, nodeX, nodeY, nodeZ;
	NodeEdge edgeAB, edgeBC, edgeCD, edgeBE, edgeCF, edgeDG, edgeEJ, edgeGK, edgeAH, edgeHI, edgeIJ, edgeJK, edgeLM, edgeMN, edgeMP, edgeNQ, edgeOP, edgePZ, edgePS, edgeQT, edgeLR, edgeRS, edgeST, edgeYX, edgeEZ;

	Hospital myHospital;
	Building myBuilding;
	Floor floor1;
	Floor floor2;

	int offset = 200;
	//@TODO  Add code to calculate offset

	public static MapModel mvm = null;
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();

		try {
			
			Scene scene = new Scene(root,640,480);
			//scene.getStylesheets().add(getClass().getResource("../Admin/MapBuilder/application.css").toExternalForm());
			SceneSwitcher.switchToScene(primaryStage, "../Admin/MapBuilder/MapEditorView.fxml");
			//primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//root.setCenter(new MapEditorController());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
