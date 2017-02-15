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
		setUpNodeGraph();
		
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

	public void setUpNodeGraph() {

		myHospital = new Hospital();

		myBuilding = new Building();

		floor1 = new Floor(1,1);
		floor2 = new Floor(2,2);
		nodeA = new MapNode(1, 00 + offset, 100 + offset);
		nodeB = new MapNode(2, 100 + offset, 100 + offset);
		nodeC = new MapNode(3, 150 + offset, 100 + offset);
		nodeD = new MapNode(4, 200 + offset, 100 + offset);
		nodeE = new MapNode(5, 100 + offset, 50 + offset);
		nodeF = new MapNode(6, 150 + offset, 50 + offset);
		nodeG = new MapNode(7, 200 + offset, 50 + offset);
		nodeH = new MapNode(8, 00 + offset, 00 + offset);
		nodeI = new MapNode(9, 50 + offset, 00 + offset);
		nodeJ = new MapNode(10, 100 + offset, 00 + offset);
		nodeK = new MapNode(11, 200 + offset, 00 + offset);
		floor1.addNode(nodeA);
		floor1.addNode(nodeB);
		floor1.addNode(nodeC);
		floor1.addNode(nodeD);
		floor1.addNode(nodeE);
		floor1.addNode(nodeF);
		floor1.addNode(nodeG);
		floor1.addNode(nodeH);
		floor1.addNode(nodeI);
		floor1.addNode(nodeJ);
		floor1.addNode(nodeK);
		edgeAB = new NodeEdge(nodeA, nodeB, 8);
		edgeBC = new NodeEdge(nodeB, nodeC, 5);
		edgeCD = new NodeEdge(nodeC, nodeD, 5);
		edgeBE = new NodeEdge(nodeB, nodeE, 3);
		edgeCF = new NodeEdge(nodeC, nodeF, 2);
		edgeDG = new NodeEdge(nodeD, nodeG, 6);
		edgeEJ = new NodeEdge(nodeE, nodeJ, 2);
		edgeGK = new NodeEdge(nodeG, nodeK, 1);
		edgeAH = new NodeEdge(nodeA, nodeH, 5);
		edgeHI = new NodeEdge(nodeH, nodeI, 3);
		edgeIJ = new NodeEdge(nodeI, nodeJ, 5);
		edgeJK = new NodeEdge(nodeJ, nodeK, 10);
		floor1.addEdge(edgeAB);
		floor1.addEdge(edgeBC);
		floor1.addEdge(edgeCD);
		floor1.addEdge(edgeBE);
		floor1.addEdge(edgeCF);
		floor1.addEdge(edgeDG);
		floor1.addEdge(edgeEJ);
		floor1.addEdge(edgeGK);
		floor1.addEdge(edgeAH);
		floor1.addEdge(edgeHI);
		floor1.addEdge(edgeIJ);
		floor1.addEdge(edgeJK);
		floor1.setKioskLocation(nodeA);


		nodeL = new MapNode(12, 00, 100);
		nodeM = new MapNode(13, 100, 100);
		nodeN = new MapNode(14, 200, 100);
		nodeO = new MapNode(15, 50, 50);
		nodeP = new MapNode(16, 100, 50);
		nodeQ = new MapNode(17, 200, 50);
		nodeR = new MapNode(18, 00, 00);
		nodeS = new MapNode(19, 10, 00);
		nodeT = new MapNode(20, 200, 00);
		nodeX = new MapNode(22, 250, 00);
		nodeY = new MapNode(23, 250, 50);
		nodeZ = new MapNode(24, 150, 50);
		edgeLM = new NodeEdge(nodeL, nodeM, 5);
		edgeMN = new NodeEdge(nodeM, nodeN, 10);
		edgeMP = new NodeEdge(nodeM, nodeP, 8);
		edgeNQ = new NodeEdge(nodeN, nodeQ, 3);
		edgeOP = new NodeEdge(nodeO, nodeP, 2);
		edgePZ = new NodeEdge(nodeP, nodeZ, 4);
		edgePS = new NodeEdge(nodeP, nodeS, 2);
		edgeQT = new NodeEdge(nodeQ, nodeT, 2);
		edgeLR = new NodeEdge(nodeL, nodeR, 6);
		edgeRS = new NodeEdge(nodeR, nodeS, 9);
		edgeST = new NodeEdge(nodeS, nodeT, 8);
		edgeYX = new NodeEdge(nodeY, nodeX, 3);
		edgeEZ = new NodeEdge(nodeE, nodeZ, 0);
		floor2.addNode(nodeL);
		floor2.addNode(nodeM);
		floor2.addNode(nodeN);
		floor2.addNode(nodeO);
		floor2.addNode(nodeP);
		floor2.addNode(nodeZ);
		floor2.addNode(nodeQ);
		floor2.addNode(nodeR);
		floor2.addNode(nodeS);
		floor2.addNode(nodeT);
		floor2.addNode(nodeY);
		floor2.addNode(nodeX);


		mvm = new MapModel();
		mvm.setCurrentFloor(floor1);
	}
}
