//Testing
package Controller;

import Domain.Map.*;
import Domain.ViewElements.DragIconType;
import Model.Database.DatabaseManager;
import Controller.Admin.AdminList;
import Model.DoctorProfile;
import Model.MapModel;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Main extends Application {

    MapNode nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH, nodeI, nodeJ,
            nodeK, nodeL, nodeM;
    NodeEdge edgeA, edgeB, edgeC, edgeD, edgeE, edgeF, edgeG, edgeH, edgeI, edgeJ,
            edgeK, edgeL, edgeM;

    Hospital myHospital;
    Building myBuilding;
    Floor floor1;
    static final int xoffset = -60;
    static final int yoffset = -80;
    static final int BATHROOM = 0;
    static final int DOCTOR = 1;
    static final int ELEVATOR = 2;
    static final int FOOD = 3;
    static final int INFO = 4;
    static final int HELP = 5;
    static final int CONNECTOR = 6;

    public static MapModel mvm;

    public static AdminList admins;    // For testing purposes
    public static final ObservableList<String> departments =
            FXCollections.observableArrayList();

    public static final ObservableList<DoctorProfile> FaulknerHospitalDirectory = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        departments.addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

        mvm = new MapModel();
        mvm.setCurrentFloor(new Floor(3));
        setUpNodeGraph();

        SceneSwitcher.switchToLoginView(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            launch(args);
            DatabaseManager test = new DatabaseManager();

//            test.testDatabase();
//            test.addRow("services", "Admitting/Registration", 1);
//            test.deleteRow("departments", "floor", 4);
//            test.addRow("services", "ATM", 3);
//            test.addRow("services", "Atrium Cafe", 1);
        }
        catch (NullPointerException e){
            System.out.println("We know what we are, but know not what we may be.");
        }
    }

    public void setUpNodeGraph() {

        myHospital = new Hospital();

        myBuilding = new Building();

        floor1 = new Floor(1);
        nodeA = new MapNode(1, 489 + xoffset, 151 + yoffset, BATHROOM);
        nodeB = new MapNode(2, 517 + xoffset, 149 + yoffset, BATHROOM);
        nodeC = new MapNode(3, 471 + xoffset, 517 + yoffset, BATHROOM);
        nodeD = new MapNode(4, 457 + xoffset, 262 + yoffset, BATHROOM);
        nodeE = new MapNode(5, 458 + xoffset, 298 + yoffset, CONNECTOR);
        nodeF = new MapNode(6, 488 + xoffset, 299 + yoffset, CONNECTOR);
        nodeG = new MapNode(7, 485 + xoffset, 214 + yoffset, CONNECTOR);
        nodeH = new MapNode(8, 539 + xoffset, 211 + yoffset, CONNECTOR);
        nodeI = new MapNode(9, 501 + xoffset, 187 + yoffset, CONNECTOR);
        nodeJ = new MapNode(10, 500 + xoffset, 476 + yoffset, CONNECTOR);
        nodeK = new MapNode(11, 464 + xoffset, 478 + yoffset, ELEVATOR);
        nodeL = new MapNode(12, 538 + xoffset, 473 + yoffset, ELEVATOR);
        nodeM = new MapNode(13, 496 + xoffset, 530 + yoffset, INFO);

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
        floor1.addNode(nodeL);
        floor1.addNode(nodeM);

        edgeA = new NodeEdge(nodeI, nodeG, 8);
        edgeB = new NodeEdge(nodeI, nodeA, 5);
        edgeC = new NodeEdge(nodeI, nodeB, 5);
        edgeD = new NodeEdge(nodeH, nodeI, 3);
        edgeE = new NodeEdge(nodeG, nodeH, 2);
        edgeF = new NodeEdge(nodeF, nodeG, 6);
        edgeG = new NodeEdge(nodeE, nodeD, 2);
        edgeH = new NodeEdge(nodeF, nodeJ, 1);
        edgeI = new NodeEdge(nodeJ, nodeK, 5);
        edgeJ = new NodeEdge(nodeC, nodeJ, 3);
        edgeK = new NodeEdge(nodeE, nodeF, 5);
        edgeL = new NodeEdge(nodeJ, nodeL, 3);
        edgeM = new NodeEdge(nodeJ, nodeM, 5);
        floor1.addEdge(edgeA);
        floor1.addEdge(edgeB);
        floor1.addEdge(edgeC);
        floor1.addEdge(edgeD);
        floor1.addEdge(edgeE);
        floor1.addEdge(edgeF);
        floor1.addEdge(edgeG);
        floor1.addEdge(edgeH);
        floor1.addEdge(edgeI);
        floor1.addEdge(edgeJ);
        floor1.addEdge(edgeK);
        floor1.addEdge(edgeL);
        floor1.addEdge(edgeM);
        floor1.setKioskLocation(nodeM);


        mvm = new MapModel();
        mvm.setCurrentFloor(floor1);
    }
}
